package io.iqpizza.starcraft.qlearning.rl;

import com.github.ocraft.s2client.protocol.data.UnitType;
import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.BuildingType;
import io.iqpizza.starcraft.qlearning.protocol.State;

import java.util.Map;

public interface RewardCalculator {

    /**
     * 보상을 계산합니다.
     * @param previousState 행위를 수행했던 시점
     * @param action 수행한 행위
     * @param currentState 행위를 수행한 이후의 현재 상태
     * @return 계산된 보상을 반환합니다.
     */
    int calculateReward(State previousState, ActionType action, State currentState);

    default int calculateDestroyedUnits(Map<UnitType, Integer> previousUnits, Map<UnitType, Integer> currentUnits) {
        int destroyedUnits = 0;
        for (UnitType type : previousUnits.keySet()) {
            int previousCount = previousUnits.getOrDefault(type, 0);
            int currentCount = currentUnits.getOrDefault(type, 0);
            if (previousCount > currentCount) {
                destroyedUnits += (previousCount - currentCount);
            }
        }
        return destroyedUnits;
    }

    default int calculateDestroyedBuildings(Map<BuildingType, Integer> previousBuildings, Map<BuildingType, Integer> currentBuildings) {
        int destroyedUnits = 0;
        for (BuildingType type : previousBuildings.keySet()) {
            int previousCount = previousBuildings.getOrDefault(type, 0);
            int currentCount = currentBuildings.getOrDefault(type, 0);
            if (previousCount > currentCount) {
                destroyedUnits += (previousCount - currentCount);
            }
        }
        return destroyedUnits;
    }
}
