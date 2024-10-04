package io.iqpizza.starcraft.qlearning.utils;

import com.github.ocraft.s2client.protocol.data.Abilities;
import io.iqpizza.starcraft.qlearning.protocol.*;

public class ActionUtils {
    private ActionUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static ActionType toActionType(String abilityName) {
        Abilities abilities = Abilities.valueOf(abilityName);

        AttackActionType attackType = convertToAttackType(abilities);
        if (attackType != null) {
            return attackType;
        }

        ConstructActionType constructType = convertToConstructType(abilities);
        if (constructType != null) {
            return constructType;
        }

        SkillActionType skillType = convertToSkillType(abilities);
        if (skillType != null) {
            return skillType;
        }

        TrainActionType trainType = convertToTrainType(abilities);
        if (trainType != null) {
            return trainType;
        }

        return new SerializeActiveType(abilities); // 기본 상태
    }

    private static AttackActionType convertToAttackType(Abilities abilities) {
        for (AttackActionType actionType : AttackActionType.values()) {
            if (actionType.getAbility() == abilities) {
                return actionType;
            }
        }
        return null;
    }

    private static ConstructActionType convertToConstructType(Abilities abilities) {
        for (ConstructActionType actionType : ConstructActionType.values()) {
            if (actionType.getAbility() == abilities) {
                return actionType;
            }
        }
        return null;
    }

    private static SkillActionType convertToSkillType(Abilities abilities) {
        for (SkillActionType actionType : SkillActionType.values()) {
            if (actionType.getAbility() == abilities) {
                return actionType;
            }
        }
        return null;
    }

    private static TrainActionType convertToTrainType(Abilities abilities) {
        for (TrainActionType actionType : TrainActionType.values()) {
            if (actionType.getAbility() == abilities) {
                return actionType;
            }
        }
        return null;
    }
}
