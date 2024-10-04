package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Units;

//TODO: 일단 테란만, 그리고 몇가지만 작성
//TODO: 또한 업그레이드의 경우, 2024.10.01 기준 State 에 업그레이드 관련 정보가 없어서 구현 불가.
public enum SkillActionType implements EffectActionType {
    // Immediately
    CALLDOWN_MULE(Abilities.EFFECT_CALL_DOWN_MULE, Units.TERRAN_ORBITAL_COMMAND),
    CALLDOWN_SUPPLY(Abilities.EFFECT_SUPPLY_DROP, Units.TERRAN_ORBITAL_COMMAND),
    SCAN(Abilities.EFFECT_SCAN, Units.TERRAN_ORBITAL_COMMAND),

    // Upgrades
    RESEARCH_COMBAT_SHIELD(Abilities.RESEARCH_COMBAT_SHIELD, Units.TERRAN_BARRACKS_TECHLAB),
    RESEARCH_STIMPACK(Abilities.RESEARCH_STIMPACK, Units.TERRAN_BARRACKS_TECHLAB),
    RESEARCH_CONCUSSIVE(Abilities.RESEARCH_CONCUSSIVE_SHELLS, Units.TERRAN_BARRACKS_TECHLAB),

    ;

    private final Abilities ability;
    private final Units caster;

    SkillActionType(Abilities ability, Units caster) {
        this.ability = ability;
        this.caster = caster;
    }

    @Override
    public Units getCaster() {
        return caster;
    }

    @Override
    public Abilities getAbility() {
        return ability;
    }

    @Override
    public boolean isSkill() {
        return true;
    }
}
