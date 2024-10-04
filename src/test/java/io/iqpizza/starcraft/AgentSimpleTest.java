package io.iqpizza.starcraft;

import com.github.ocraft.s2client.protocol.game.Race;
import io.iqpizza.starcraft.qlearning.Agent;
import io.iqpizza.starcraft.qlearning.QLearningAgent;
import io.iqpizza.starcraft.qlearning.protocol.State;
import io.iqpizza.starcraft.qlearning.rl.SimpleActionExecutor;
import io.iqpizza.starcraft.qlearning.rl.SimpleGameMonitor;
import io.iqpizza.starcraft.qlearning.rl.SimpleRewardCalculator;
import io.iqpizza.starcraft.qlearning.utils.StateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Agent 클래스가 정상적으로 수행되는 지 체크합니다.
 * 비즈니스 로직 외 순수하게 예외가 발생하지 않는 지 검증하는 것이 목적인 Unit Test 입니다.
 */
class AgentSimpleTest {

    static Agent agent;

    static State initialState = StateUtils.getInitialState(Race.TERRAN, Race.ZERG);

    @BeforeAll
    static void initializeAgent() {
        agent = new Agent(new Bot(), new QLearningAgent(), new SimpleGameMonitor(), new SimpleActionExecutor(), new SimpleRewardCalculator(), initialState);
    }

    @Test
    void testAgentConstructor() {
        //TODO: Debug 을 어떻게 할 것인가 고민하기
        Assertions.assertNotNull(agent);
    }
}
