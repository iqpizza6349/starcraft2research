package io.iqpizza.starcraft.qlearning.rl;

import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.State;

public class SimpleRewardCalculator implements RewardCalculator {

    @Override
    public int calculateReward(State previousState, ActionType action, State currentState) {
        int reward = 0;

        // 1. 자원 수집에 따른 보상
        int mineralDifference = currentState.minerals() - previousState.minerals();
        int gasDifference = currentState.gas() - previousState.gas();
        reward += (mineralDifference + gasDifference) * 10;  // 자원 수집에 비례한 보상

        // 2. 인구수 증가에 따른 보상
        if (currentState.supplyUsed() > previousState.supplyUsed()) {
            reward += 50;  // 인구수 증가에 따른 보상
        }

        // 3. 적 유닛 처치에 따른 보상
        int enemyUnitsDestroyed = calculateDestroyedUnits(previousState.enemyUnits(), currentState.enemyUnits());
        reward += enemyUnitsDestroyed * 100;  // 적 유닛 처치에 따른 보상

        // 4. 적 건물 파괴에 따른 보상
        int enemyBuildingsDestroyed = calculateDestroyedBuildings(previousState.myBuildings(), currentState.myBuildings());
        reward += enemyBuildingsDestroyed * 200;  // 적 건물 파괴에 따른 보상

        // 5. 공격, 건설, 훈련 등의 특수 행동에 따른 보상
        if (action.isAttack()) {
            reward += 10;  // 공격 행동에 대한 보상
        } else if (action.isConstruct()) {
            reward += 20;  // 건설 행동에 대한 보상
        } else if (action.isTrain()) {
            reward += 15;  // 유닛 훈련에 대한 보상
        }

        // 6. 추가적인 보상이나 패널티
        // 예를 들어, 자신의 유닛이 파괴되었을 때 패널티를 줄 수 있습니다.
        int myUnitsLost = calculateDestroyedUnits(previousState.myUnits(), currentState.myUnits());
        reward -= myUnitsLost * 50;  // 자신의 유닛 파괴 시 패널티

        return reward;
    }
}
