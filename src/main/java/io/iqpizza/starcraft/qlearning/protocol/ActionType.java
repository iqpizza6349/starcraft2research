package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.Abilities;

public interface ActionType {
    /**
     * 실제 Abilities enum 을 참조
     * @return 수행될 Abiltiy
     * @see Abilities
     * @see com.github.ocraft.s2client.protocol.data.Ability
     */
    Abilities getAbility();

    default boolean isAttack() {
        return false;
    }

    default boolean isTrain() {
        return false;
    }

    default boolean isConstruct() {
        return false;
    }

    default boolean isSkill() {
        return false;
    }

    /**
     * Interface 는 toString 을 구현하지 못하므로 getString 으로 직접 구현
     * @return 수행할 행위
     * @see Abilities
     */
    default String getString() {
        return getAbility().name();
    }

    /**
     *
     * @param currentState
     * @return
     * @apiNote effect 의 경우, 시전자 유닛인 caster 가 존재할 때만 수행 가능하도록 하고, 에너지는 수행 직접에만 체크하도록 하기
     */
    default boolean isExecutable(State currentState) {
        // 기본 구현: 모든 상태에서 실행 불가능, 직접 하위에서 true 혹은 구현해서 허용하도록 함
        return false;
    }
}
