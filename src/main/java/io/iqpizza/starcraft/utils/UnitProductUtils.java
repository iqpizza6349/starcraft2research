package io.iqpizza.starcraft.utils;

import com.github.ocraft.s2client.bot.gateway.ObservationInterface;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.unit.Unit;

import static com.github.ocraft.s2client.protocol.data.Abilities.*;
import static com.github.ocraft.s2client.protocol.data.Units.*;

/**
 * Availables 로 해야할 지 유닛 타입별로 해야할지 긴가민가해서
 * 일단 유닛 타입별로 해봄
 */
public final class UnitProductUtils {
    private UnitProductUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static Units getProduction(Units unitType, Race race, boolean isWrap) {
        return switch (race) {
            case TERRAN -> getTerranProduction(unitType);
            case ZERG -> getZergProduction(unitType);
            case PROTOSS -> getProtossProduction(unitType, isWrap);
            default -> throw new IllegalArgumentException("Unknown race: " + race);
        };
    }

    private static Units getZergProduction(Units unitType) {
        // 변태를 별도로 진행하는 럴커나 맹독충, 궤멸충 등이 아닌 경우, 전부 해처리에서 부화 가능
        return switch (unitType) {
            case ZERG_BANELING_COCOON -> ZERG_ZERGLING;
            case ZERG_RAVAGER_COCOON -> ZERG_ROACH;
            case ZERG_OVERLORD_COCOON -> ZERG_OVERLORD;       // 감시군주 코쿤
            case ZERG_LURKER_MP_EGG -> ZERG_HYDRALISK;
            case ZERG_BROODLORD_COCOON -> ZERG_CORRUPTOR;
            default -> ZERG_HATCHERY;
        };
    }

    private static Units getTerranProduction(Units unitType) {
        return switch (unitType) {
            case TERRAN_SCV -> TERRAN_COMMAND_CENTER;
            case TERRAN_MULE -> TERRAN_ORBITAL_COMMAND; // 지게로봇
            case TERRAN_MARINE, TERRAN_REAPER, TERRAN_MARAUDER, TERRAN_GHOST -> TERRAN_BARRACKS;
            case TERRAN_HELLION, TERRAN_HELLION_TANK, TERRAN_WIDOWMINE, TERRAN_CYCLONE, TERRAN_SIEGE_TANK, TERRAN_THOR -> TERRAN_FACTORY;
            case TERRAN_VIKING_FIGHTER, TERRAN_MEDIVAC, TERRAN_LIBERATOR, TERRAN_BANSHEE, TERRAN_RAVEN, TERRAN_BATTLECRUISER -> TERRAN_STARPORT;
            case TERRAN_NUKE -> TERRAN_GHOST_ACADEMY; // 유령 사관 학교
            default -> throw new IllegalArgumentException(getErrorMessage(unitType));
        };
    }

    private static String getErrorMessage(Units unitType) {
        return String.format("Unknown unit: '%s'", unitType);
    }

    private static Units getProtossProduction(Units unitType, boolean isWrap) {
        return switch (unitType) {
            case PROTOSS_PROBE, PROTOSS_MOTHERSHIP -> Units.PROTOSS_NEXUS;
            case PROTOSS_ZEALOT, PROTOSS_SENTRY, PROTOSS_STALKER, PROTOSS_ADEPT, PROTOSS_HIGH_TEMPLAR, PROTOSS_DARK_TEMPLAR ->
                    (isWrap) ? Units.PROTOSS_WARP_GATE : Units.PROTOSS_GATEWAY;
            case PROTOSS_OBSERVER, PROTOSS_WARP_PRISM, PROTOSS_IMMORTAL, PROTOSS_COLOSSUS, PROTOSS_DISRUPTOR -> Units.PROTOSS_ROBOTICS_FACILITY;
            case PROTOSS_PHOENIX, PROTOSS_VOIDRAY, PROTOSS_ORACLE, PROTOSS_CARRIER, PROTOSS_TEMPEST -> Units.PROTOSS_STARGATE;
            default -> throw new IllegalArgumentException(getErrorMessage(unitType));
        };
    }

    public static Abilities getAbilities(Units unitType) {
        return switch (unitType) {
            case TERRAN_SCV -> Abilities.TRAIN_SCV;
            case TERRAN_MULE -> Abilities.EFFECT_CALL_DOWN_MULE;
            case TERRAN_MARINE -> TRAIN_MARINE;
            case TERRAN_REAPER -> TRAIN_REAPER;
            case TERRAN_GHOST -> TRAIN_GHOST;
            case TERRAN_MARAUDER -> TRAIN_MARAUDER;
            case TERRAN_HELLION -> TRAIN_HELLION;
            case TERRAN_HELLION_TANK -> TRAIN_HELLBAT;
            case TERRAN_WIDOWMINE -> TRAIN_WIDOWMINE;
            case TERRAN_CYCLONE -> TRAIN_CYCLONE;
            case TERRAN_SIEGE_TANK -> TRAIN_SIEGE_TANK;
            case TERRAN_THOR -> TRAIN_THOR;
            case TERRAN_VIKING_FIGHTER -> TRAIN_VIKING_FIGHTER;
            case TERRAN_MEDIVAC -> TRAIN_MEDIVAC;
            case TERRAN_LIBERATOR -> TRAIN_LIBERATOR;
            case TERRAN_BANSHEE -> TRAIN_BANSHEE;
            case TERRAN_RAVEN -> TRAIN_RAVEN;
            case TERRAN_BATTLECRUISER -> TRAIN_BATTLECRUISER;
            case TERRAN_NUKE -> BUILD_NUKE;
            default -> throw new IllegalArgumentException(getErrorMessage(unitType));
        };
    }

    //TODO: Abilities 하고 Units 를 서로 잇는 Enum 을 별도로 만들어야할 듯

    public static Units getUnitType(Abilities abilities) {
        return switch (abilities) {
            case TRAIN_SCV -> TERRAN_SCV;
            case EFFECT_CALL_DOWN_MULE -> TERRAN_MULE;
            case TRAIN_MARINE -> TERRAN_MARINE;
            case TRAIN_REAPER -> TERRAN_REAPER;
            case TRAIN_GHOST -> TERRAN_GHOST;
            case TRAIN_MARAUDER -> TERRAN_MARAUDER;
            case TRAIN_HELLION -> TERRAN_HELLION;
            case TRAIN_HELLBAT -> TERRAN_HELLION_TANK;
            case TRAIN_WIDOWMINE -> TERRAN_WIDOWMINE;
            case TRAIN_CYCLONE -> TERRAN_CYCLONE;
            case TRAIN_SIEGE_TANK -> TERRAN_SIEGE_TANK;
            case TRAIN_THOR -> TERRAN_THOR;
            case TRAIN_VIKING_FIGHTER -> TERRAN_VIKING_FIGHTER;
            case TRAIN_MEDIVAC -> TERRAN_MEDIVAC;
            case TRAIN_LIBERATOR -> TERRAN_LIBERATOR;
            case TRAIN_BANSHEE -> TERRAN_BANSHEE;
            case TRAIN_RAVEN -> TERRAN_RAVEN;
            case TRAIN_BATTLECRUISER -> TERRAN_BATTLECRUISER;
            case BUILD_NUKE -> TERRAN_NUKE;
            case BUILD_SUPPLY_DEPOT -> TERRAN_SUPPLY_DEPOT;
            default -> throw new IllegalArgumentException("Unknown ability: " + abilities);
        };
    }

    public static int requiresSupply(Units unitType) {
        return switch (unitType) {
            case TERRAN_SCV, TERRAN_REAPER, TERRAN_MARINE -> 1;
            case TERRAN_MARAUDER, TERRAN_GHOST,
                 TERRAN_HELLION, TERRAN_HELLION_TANK, TERRAN_FIREBAT, TERRAN_WIDOWMINE, TERRAN_CYCLONE,
                 TERRAN_VIKING_FIGHTER, TERRAN_MEDIVAC, TERRAN_RAVEN -> 2;
            case TERRAN_SIEGE_TANK, TERRAN_LIBERATOR, TERRAN_BANSHEE -> 3;
            case TERRAN_THOR, TERRAN_BATTLECRUISER -> 6;
            default -> 0;
        };
    }

    public static boolean isSelfUnit(Unit unit, ObservationInterface observation) {
        return (unit.getOwner() == observation.getPlayerId());
    }

}
