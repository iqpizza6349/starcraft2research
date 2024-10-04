package io.iqpizza.starcraft.qlearning.utils;

import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import io.iqpizza.starcraft.qlearning.protocol.BuildingType;
import io.iqpizza.starcraft.qlearning.protocol.State;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * 가독성을 높이기 위한 장치
 */
public class StateUtils {

    private StateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static int getMineral(int mineral) {
        return mineral;
    }

    public static int getGas(int gas) {
        return gas;
    }

    public static int getSupplyUsed(int supplyUsed) {
        return supplyUsed;
    }

    public static int getSupplyMax(int supplyMax) {
        return supplyMax;
    }

    public static int getWorkerSupply(int workerSupply) {
        return workerSupply;
    }

    public static int getArmySupply(int armySupply) {
        return armySupply;
    }

    @SafeVarargs
    public static Map<UnitType, Integer> getMyUnits(Map<UnitType, Integer>... myUnits) {
        return getUnits(myUnits);
    }

    private static Map<UnitType, Integer> getUnits(Map<UnitType, Integer>[] myUnits) {
        Map<UnitType, Integer> allMyUnits = new HashMap<>();

        for (Map<UnitType, Integer> myUnit : myUnits) {
            myUnit.forEach((unitType, count) -> allMyUnits.merge(unitType, count, Integer::sum));
        }
        return allMyUnits;
    }

    @SafeVarargs
    public static Map<UnitType, Integer> getEnemyUnits(Map<UnitType, Integer>... enemyUnits) {
        return getUnits(enemyUnits);
    }

    public static Point2d getEnemyBaseLocation(Point2d enemyBaseLocation) {
        return enemyBaseLocation;
    }

    public static Race getMyRace(Race myRace) {
        return myRace;
    }

    public static Race getEnemyRace(Race enemyRace) {
        return enemyRace;
    }

    public static State getInitialState(Race race, Race enemyRace) {
        return new State(
                getMineral(50),
                getGas(0),
                getSupplyUsed(12),
                getSupplyMax((race == Race.ZERG) ? 14 : 15),
                getWorkerSupply(12),
                getArmySupply(0),
                getMyUnits(getWorkers(race, 12)),
                getEnemyUnits(getWorkers(enemyRace, 12), getUnitMap(getBaseBuildingType(enemyRace))),
                convertToBuildingType(getUnitMap(getBaseBuildingType(race))),
                null,       /* 정확히 어딘지 모르기에 null */
                race,
                enemyRace
        );
    }

    public static Map<UnitType, Integer> getWorkers(Race race, int workerCount) {
        return getUnitMap(getWorkerType(race), workerCount);
    }

    private static UnitType getWorkerType(Race race) {
        return switch (race) {
            case ZERG -> Units.ZERG_DRONE;
            case TERRAN -> Units.TERRAN_SCV;
            case PROTOSS -> Units.PROTOSS_PROBE;
            default -> null;
        };
    }

    private static UnitType getBaseBuildingType(Race race) {
        return switch (race) {
            case ZERG -> Units.ZERG_HATCHERY;
            case TERRAN -> Units.TERRAN_COMMAND_CENTER;
            case PROTOSS -> Units.PROTOSS_NEXUS;
            default -> null;
        };
    }

    private static Map<UnitType, Integer> getUnitMap(UnitType unitType) {
        return getUnitMap(unitType, 1);
    }

    private static Map<UnitType, Integer> getUnitMap(UnitType unitType, int count) {
        Map<UnitType, Integer> unitMap = new HashMap<>();
        unitMap.put(unitType, count);
        return unitMap;
    }

    private static Map<BuildingType, Integer> convertToBuildingType(Map<UnitType, Integer> rawMap) {
        Map<BuildingType, Integer> buildingMap = new EnumMap<>(BuildingType.class);
        rawMap.forEach((unitType, count) -> buildingMap.put(BuildingType.of(unitType), count));

        return buildingMap;
    }
}
