package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.Units;

public interface EffectActionType extends ActionType {
    /**
     * 해당 능력을 수행하는 시전자 타입
     * @return 능력 시전자 타입
     */
    Units getCaster();

    @Override
    default boolean isExecutable(State currentState) {
        return currentState.hasUnit(getCaster());
    }
}
