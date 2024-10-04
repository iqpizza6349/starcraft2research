package io.iqpizza.starcraft.rl;

import com.github.ocraft.s2client.protocol.game.Race;
import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.QLearningAgent;
import io.iqpizza.starcraft.qlearning.QLearningAgentFactory;
import io.iqpizza.starcraft.qlearning.protocol.State;
import io.iqpizza.starcraft.qlearning.qtable.QTable;
import io.iqpizza.starcraft.qlearning.utils.StateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * QLearningAgent 에 대한 유닛 테스트를 정의한다.
 * 다음은 주요 시나리오다. <br/>
 * 1. e-greedy 탐색 전략
 *   a. 탐색 비율이 0일 때, 항상 Q값이 가장 큰 행동을 선택하는 지 확인
 *   b. 탐색 비율이 1일 때, 랜덤하게 선택되는 지 체크
 *   c. 탐색 비율이 0 < e < 1인 경우 (카이chi 테스트가 필요하나 skip...)
 * 2. 학습률 테스트
 *   a. alpha(학습률)이 0일 때 이전 Q값이 그대로 유지되는 지 확인
 *   b. alpha가 1일 때 새로운 보상값이 Q값에 반영되는 지 확인
 * 3. Q값 경계선 테스트
 *   a. 매우 큰 보상 혹은 매우 작은 보상에 대해 Q값을 업데이트했을 때
 *     오버플로우나 언더플로우 발생 여부 확인
 *   b. 할인율가 계산에 정상 반영되는 지 확인
 * 4. 동시성(선택사항): ExecutorService 로 병렬 스트림 수행을 통해 동시 수정 검증 <br/>
 */
class LearningAgentTest {
    private static final State COMMON_STATE = StateUtils.getInitialState(Race.TERRAN, Race.ZERG);


    @Nested
    @DisplayName("e-greedy 탐색 전략 테스트")
    class EpsilonGreedyPolicyTests {
        private QTable qtable;

        @BeforeEach
        void setUp() {
            qtable = mock(QTable.class);
        }

        @Test
        @DisplayName("탐험률이 0.0 으로 최적의 행동만 조회하는 지 확인")
        void testOnlyOptimalAction() {
            // given
            QLearningAgent agent = new QLearningAgent(qtable);
            ActionType optimalAction = new DummyAction("최적의 행동");

            // when
            when(qtable.getBestAction(COMMON_STATE)).thenReturn(Optional.of(optimalAction));

            // then
            // 탐색률이 0일 때, 항상 최적의 행동을 반환
            agent.setEpsilon(0.0);
            assertThat(agent.chooseAction(COMMON_STATE)).isEqualTo(optimalAction);

            // 탐험률이 1일 때, 항상 무작위 행동을 반환
            agent.setEpsilon(1.0);
            ActionType actionType = agent.chooseAction(COMMON_STATE);
            assertThat(actionType).isNotEqualTo(optimalAction);
        }
    }

    @Nested
    @DisplayName("학습률(alpha) 테스트")
    class AlphaPolicyTests {
        @Test
        @DisplayName("학습률이 0일 때 Q값이 유지되는 지 확인")
        void testAlphaIsZero() {
            // given
            ActionType action = new DummyAction();
            double prevValue = 0.7;
            QTable table = new QTable(QTableTestUtils.stub(COMMON_STATE, action, prevValue));
            QLearningAgent agent = QLearningAgentFactory.createCustomAgent(table, 0.0, 0.9, 0.0);

            // when
            agent.updateQTable(COMMON_STATE, action, 3, null);

            // then
            // 새로운 Q 값이 갱신되지 않는 지 테스트
            assertThat(table.getQValue(COMMON_STATE, action)).isEqualTo(prevValue);
        }

        @Test
        @DisplayName("학습률이 1일 때 Q값이 새로운 보상값으로 반영되는 지 확인")
        void testAlphaIsOne() {
            // given
            ActionType action = new DummyAction();
            QTable table = new QTable(QTableTestUtils.stub(COMMON_STATE, action, 0));
            QLearningAgent agent = QLearningAgentFactory.createCustomAgent(table, 1.0, 0.9, 0.0);

            // when
            agent.updateQTable(COMMON_STATE, action, 3, null);

            // then
            // 새로운 Q 값이 반영되어 기존 값인 0 을 유지하면 안됨
            assertThat(table.getQValue(COMMON_STATE, action)).isNotEqualTo(0.0);
        }
    }

    @Nested
    class BoundaryPolicyTests {
        private QTable qtable;

        @BeforeEach
        void setUp() {
            qtable = new QTable();
        }

        @Test
        void testQValueUpdateWithGammaZero() {
            ActionType action = new DummyAction("Action1");
            QLearningAgent agent = QLearningAgentFactory.createCustomAgent(qtable, QLearningAgent.DEFAULT_ALPHA, 0.0);

            qtable.setQValue(COMMON_STATE, action, 1.0);

            // 할인율이 0일 때, 미래 보상은 고려되지 않음
            int reward = 10;
            agent.updateQTable(COMMON_STATE, action, reward, null);

            double updatedQValue = qtable.getQValue(COMMON_STATE, action);
            double expectedQValue = 1.0 + agent.getAlpha() * (reward - 1.0);  // Q(s, a) = Q(s, a) + alpha * (reward - Q(s, a))

            System.out.println(expectedQValue);
            System.out.println(updatedQValue);
            assertThat(updatedQValue).as("할인율 0에서 Q값 계산이 올바르지 않습니다.").isEqualTo(expectedQValue);
        }

        @Test
        void testQValueUpdateWithGammaOne() {
            ActionType action = new DummyAction("Action1");
            QLearningAgent agent = QLearningAgentFactory.createCustomAgent(qtable, QLearningAgent.DEFAULT_ALPHA, 1.0);

            qtable.setQValue(COMMON_STATE, action, 1.0);

            // 할인율이 1일 때, 모든 미래 보상이 동일하게 반영됨
            int reward = 10;
            double maxFutureQValue = 5.0;  // 임의의 미래 상태 Q값
            qtable.setQValue(COMMON_STATE , new DummyAction("Action2"), maxFutureQValue);

            agent.updateQTable(COMMON_STATE, action, reward, COMMON_STATE); // 테스트이므로 nextState 도 그대로 유지

            double updatedQValue = qtable.getQValue(COMMON_STATE, action);
            double expectedQValue = 1.0 + agent.getAlpha() * (reward + maxFutureQValue - 1.0);  // Q(s, a) = Q(s, a) + alpha * (reward + gamma * maxFutureQValue - Q(s, a))
            assertThat(updatedQValue).as("할인율 0에서 Q값 계산이 올바르지 않습니다.").isEqualTo(expectedQValue);
        }
    }




}
