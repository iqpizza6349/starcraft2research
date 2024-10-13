package io.iqpizza.starcraft.utils;

import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.Race;

import static io.iqpizza.starcraft.utils.BuildingSizeUtils.BUILDING_SIZES;

public final class UnitTypeUtils {
    private UnitTypeUtils() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 해당 유닛 타입이 건물 유무를 확인하여 반환
     * @param unitType 확인하고자 하는 유닛 타입
     * @return 건물이라면 true, 그외에는 false
     * @apiNote {@link BuildingSizeUtils}에 정의되어 있지 않다면 false 를 반환한다.
     */
    public static boolean isBuildingType(Units unitType) {
        return BUILDING_SIZES.containsKey(unitType);
    }

    /**
     * 해당 유닛 타입이 미네랄인지 확인하여 반환
     * @param unitType 확인하고자 하는 유닛 타입
     * @return 미네랄이라면 true, 그외에는 false
     */
    public static boolean isMineralType(Units unitType) {
        return unitType.name().contains("MINERAL");
    }

    /**
     * 해당 유닛 타입이 가스 지형인지 확인하여 반환
     * @param unitType 확인하고자 하는 유닛 타입
     * @return 가스 지형이라면 true, 그외에는 false
     * @apiNote 가스 추출 건물의 경우에는 false 반환
     */
    public static boolean isGasGeyserType(Units unitType) {
        return unitType.name().contains("VESPENE_GEYSER");
    }

    /**
     * 파괴 가능한 지형인지 확인하여 반환
     * @param unitType 확인하고자 하는 유닛 타입
     * @return 파괴 가능한 지형이라면 true, 그외에는 false
     */
    public static boolean isDestructable(Units unitType) {
        return unitType.name().contains("DESTRUCTIBLE");
    }

    /**
     * 저그 유닛 중 하나인지 확인하여 반환
     * @param unitType 확인하고자 하는 유닛 타입
     * @return 저그 유닛이라면 true, 그외에는 false
     */
    public static boolean isZerg(Units unitType) {
        return unitType.name().startsWith("ZERG");
    }

    /**
     * 테란 유닛 중 하나인지 확인하여 반환
     * @param unitType 확인하고자 하는 유닛 타입
     * @return 테란 유닛이라면 true, 그외에는 false
     */
    public static boolean isTerran(Units unitType) {
        return unitType.name().startsWith("TERRAN");
    }

    /**
     * 프로토스 유닛 중 하나인지 확인하여 반환
     * @param unitType 확인하고자 하는 유닛 타입
     * @return 프로토스 유닛이라면 true, 그외에는 false
     */
    public static boolean isProtoss(Units unitType) {
        return unitType.name().startsWith("PROTOSS");
    }

    /**
     * 해당 유닛이 어떤 종족의 유닛인지 확인하여 종족 반환
     * 정적이거나 특정 종족에 해당되지 않으면 {@link Race#NO_RACE} 반환
     * @param unitType 확인하고자 하는 유닛 타입
     * @return 종족 반환
     */
    public static Race getRace(Units unitType) {
        if (isZerg(unitType)) {
            return Race.ZERG;
        }
        else if (isTerran(unitType)) {
            return Race.TERRAN;
        }
        else if (isProtoss(unitType)) {
            return Race.PROTOSS;
        }
        else {
            return Race.NO_RACE;
        }
    }

    public static boolean isWorker(Units unitType) {
        return unitType == Units.ZERG_DRONE || unitType == Units.TERRAN_SCV || unitType == Units.PROTOSS_PROBE;
    }
}
