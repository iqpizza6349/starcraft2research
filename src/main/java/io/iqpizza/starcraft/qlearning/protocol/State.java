package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.spatial.Point2d;

import java.util.Map;

import static com.github.ocraft.s2client.protocol.data.Units.*;

public record State(int minerals, int gas, int supplyUsed, int supplyMax,
                    int workerSupply, int armySupply,
                    Map<UnitType, Integer> myUnits,
                    Map<UnitType, Integer> enemyUnits,
                    Map<BuildingType, Integer> myBuildings,
                    Point2d enemyBaseLocation,
                    Race myRace,
                    Race enemyRace) {

    public State(State s) {
        this(s.minerals, s.gas, s.supplyUsed, s.supplyMax, s.workerSupply, s.armySupply, s.myUnits, s.enemyUnits, s.myBuildings, s.enemyBaseLocation, s.myRace, s.enemyRace);
    }

    @Override
    public String toString() {
        return "State{" +
                "minerals=" + minerals +
                ", gas=" + gas +
                ", supplyUsed=" + supplyUsed +
                ", supplyMax=" + supplyMax +
                ", workerSupply=" + workerSupply +
                ", armySupply=" + armySupply +
                ", myUnits=" + myUnits +
                ", enemyUnits=" + enemyUnits +
                ", myBuildings=" + myBuildings +
                ", enemyBaseLocation=" + enemyBaseLocation +
                ", myRace=" + myRace +
                ", enemyRace=" + enemyRace +
                '}';
    }

    public String toSimplizedString() {
        return "State{" +
                "myRace=" + myRace +
                ",enemyRace=" + enemyRace +
                ",minerals=" + minerals +
                ",gas=" + gas +
                ",workers=" + workerSupply +
                ",army=" + armySupply +
                ",supplyUsed=" + supplyUsed +
                ",supplyMax=" + supplyMax +
                "}";
    }

    public boolean hasBuildiing(BuildingType type) {
        return myBuildings.getOrDefault(type, 0) > 0;
    }

    public boolean hasUnit(UnitType type) {
        return myUnits.getOrDefault(type, 0) > 0;
    }

    public boolean hasWorker() {
        return workerSupply > 0 && (hasUnit(ZERG_DRONE) || hasUnit(TERRAN_SCV) || hasUnit(PROTOSS_PROBE));
    }

    public boolean hasAllAddon(BuildingType toBeAttached, Units affix) {
        int countAttachedBuilding = myBuildings.getOrDefault(toBeAttached, 0);
        int countAffixBuilding = myUnits.getOrDefault(affix, 0);    // 에드온은 건물 취급 안함

        // 에드온 부착될 건물이 1개 이상이고, 모든 건물이 에드온 부착되지 않았다면 true
        return countAttachedBuilding > 0 && countAttachedBuilding == countAffixBuilding;
    }

    // 테란 에드온 가격은 항상 동일
    public boolean hasEnoughResourceToAddon(BuildingType toBeAttached, Units affix) {
        // Reactor(반응로)가 가스 25 더 비쌈
        boolean isReactor = (affix == TERRAN_REACTOR || affix == TERRAN_BARRACKS_REACTOR || affix == TERRAN_FACTORY_REACTOR || affix == TERRAN_STARPORT_REACTOR);
        boolean isEnough = (isReactor) ? minerals >= 50 && gas >= 50 : minerals > 50 && gas > 25;
        return hasBuildiing(toBeAttached) && !hasAllAddon(toBeAttached, affix)
                && isEnough;
    }

    public int getSupplyLeft() {
        return supplyMax - supplyUsed;
    }
}
