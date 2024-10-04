package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.Abilities;

public enum AttackActionType implements ActionType {
    ATTACK {
        @Override
        public boolean isExecutable(State currentState) {
            // 공격 유닛으로 S2 에서 자동으로 잡아주는 걸로 20 이 넘어가면 공격 가능
            //TODO: 상대방의 공격 등을 위한 보다 유연한 작업이 요구됨
            return currentState.armySupply() >= 20;
        }
    };


    @Override
    public Abilities getAbility() {
        return Abilities.ATTACK_ATTACK;
    }

    @Override
    public boolean isAttack() {
        return true;
    }
}
