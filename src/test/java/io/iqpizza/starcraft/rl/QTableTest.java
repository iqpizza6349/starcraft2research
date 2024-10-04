package io.iqpizza.starcraft.rl;

import com.github.ocraft.s2client.protocol.game.Race;
import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.AttackActionType;
import io.iqpizza.starcraft.qlearning.protocol.State;
import io.iqpizza.starcraft.qlearning.qtable.QTable;
import io.iqpizza.starcraft.qlearning.utils.StateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * QTable 에 대한 유닛 테스트를 정의한다.
 * 다음은 주요 시나리오다. <br/>
 * 1. 초기화 테스트: QTable 에서 null 이 나오는 지 확인 <br/>
 * 2. 상태 추가: 새로운 상태가 정상적으로 저장되었는 지와 원본 데이터가 잘못 덮어쓰워졌는 지 확인 <br/>
 * 3. Q값 업데이트: 갱신이 정상적인지와 State 가 없는 상태에서 행위-Q값 쌍 넣어더라도 정상이어야함 <br/>
 * 5. 동시성(선택사항): ExecutorService 로 병렬 스트림 수행을 통해 동시 수정 검증 <br/>
 */
class QTableTest {
    private QTable table;

    @BeforeEach
    void setUp() {
        table = new QTable();
    }

    @Test
    @DisplayName("초기화 테스트")
    void testTableInitialize() {
        assertThat(table).isNotNull();
        assertThat(table.getTable()).isNotNull();
    }

    @Nested
    @DisplayName("상태 추가 테스트")
    class AddStateActionPairTests {
        @Test
        @DisplayName("정상 추가 확인")
        void testAddStateActionPair() {
            // given
            State state = StateUtils.getInitialState(Race.TERRAN, Race.ZERG);
            ActionType actionType = new DummyAction();
            double value = 1.0;

            // when
            table.setQValue(state, actionType, value);

            // then
            double qValue = table.getQValue(state, actionType);
            assertThat(qValue).isEqualTo(value);
        }

        @Test
        @DisplayName("원본 데이터가 잘못 덮어쓰워지는 지 확인")
        void testDuplicateStateActionPair() {
            // given
            State state = StateUtils.getInitialState(Race.TERRAN, Race.ZERG);
            ActionType action1 = new DummyAction();
            ActionType action2 = AttackActionType.ATTACK;

            table.setQValue(state, action1, 1.0);
            table.setQValue(state, action2, 1.0);

            // when
            table.setQValue(state, action1, 2.0);

            // then
            assertThat(table.getQValue(state, action1)).isEqualTo(2.0);
            assertThat(table.getQValue(state, action2)).isEqualTo(1.0);
        }
    }

    @Nested
    @DisplayName("Q값 업데이트 테스트")
    class UpdateQValueTests {
        private final Race myRace = Race.TERRAN;
        private final Race enemyRace = Race.PROTOSS;

        @BeforeEach
        void addInitialValue() {
            table.setQValue(StateUtils.getInitialState(myRace, enemyRace), AttackActionType.ATTACK, 1.0);
        }

        @Test
        @DisplayName("정상 갱신 확인")
        void testUpdateExistingValue() {
            // given
            State state = StateUtils.getInitialState(myRace, enemyRace);
            ActionType action = AttackActionType.ATTACK;
            double value = 2.5;

            // when
            table.setQValue(state, action, value);

            // then
            double qValue = table.getQValue(state, action);
            assertThat(qValue).isEqualTo(value);
        }

        @Test
        @DisplayName("정상 비갱신 및 추가 확인: State 다름")
        void testUpdateNotExistingState() {
            // given: State 를 다른 걸로 했을 때
            State state = StateUtils.getInitialState(myRace, Race.ZERG);
            ActionType action = AttackActionType.ATTACK;

            // when
            table.setQValue(state, action, 3.0);

            // then
            assertThat(table.getQValue(StateUtils.getInitialState(myRace, enemyRace), action)).isEqualTo(1.0);
            assertThat(table.getQValue(state, action)).isEqualTo(3.0);
        }

        @Test
        @DisplayName("정상 비갱신 및 추가 확인: Action 다름")
        void testUpdateNotExistingAction() {
            // given: State 를 다른 걸로 했을 때
            State state = StateUtils.getInitialState(myRace, enemyRace);
            ActionType action = new DummyAction();

            // when
            table.setQValue(state, action, 2.0);

            // then
            assertThat(table.getQValue(state, AttackActionType.ATTACK)).isEqualTo(1.0);
            assertThat(table.getQValue(state, action)).isEqualTo(2.0);
        }
    }
}
