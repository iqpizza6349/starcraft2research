package io.iqpizza.starcraft.qlearning.rl;

import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.State;

public interface ActionExecutor {
    /**
     * 특정 행동을 수행한 이후, 그 행동에 수행 이후의 상태를 받아옵니다.
     * @param action 취할 행동
     * @return 행동을 취한 이후의 결과(상태)를 반환합니다.
     */
    State execute(ActionType action);

    State getCurrentState();
}
