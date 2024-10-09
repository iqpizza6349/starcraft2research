package io.iqpizza.starcraft.simcity;

import com.github.ocraft.s2client.protocol.data.Units;

import java.util.EnumMap;
import java.util.Map;

import static io.iqpizza.starcraft.simcity.Size.*;

public final class BuildingSizeUtils {
    private static final Map<Units, Size> buildingSizes = new EnumMap<>(Units.class);

    static {
        // terran
        buildingSizes.put(Units.TERRAN_COMMAND_CENTER, BIG_SIZE);
        buildingSizes.put(Units.TERRAN_ORBITAL_COMMAND, BIG_SIZE);
        buildingSizes.put(Units.TERRAN_PLANETARY_FORTRESS, BIG_SIZE);
        buildingSizes.put(Units.TERRAN_REACTOR, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_BARRACKS_REACTOR, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_FACTORY_REACTOR, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_STARPORT_REACTOR, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_TECHLAB, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_BARRACKS_TECHLAB, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_FACTORY_TECHLAB, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_STARPORT_TECHLAB, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_BARRACKS, MIDDLE_SIZE);
        buildingSizes.put(Units.TERRAN_FACTORY, MIDDLE_SIZE);
        buildingSizes.put(Units.TERRAN_STARPORT, MIDDLE_SIZE);
        buildingSizes.put(Units.TERRAN_REFINERY, MIDDLE_SIZE);
        buildingSizes.put(Units.TERRAN_SUPPLY_DEPOT, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_SUPPLY_DEPOT_LOWERED, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_ENGINEERING_BAY, MIDDLE_SIZE);
        buildingSizes.put(Units.TERRAN_BUNKER, MIDDLE_SIZE);
        buildingSizes.put(Units.TERRAN_MISSILE_TURRET, SMALL_SIZE);
        buildingSizes.put(Units.TERRAN_SENSOR_TOWER, TINY_SIZE);
        buildingSizes.put(Units.TERRAN_GHOST_ACADEMY, MIDDLE_SIZE);
        buildingSizes.put(Units.TERRAN_ARMORY, MIDDLE_SIZE);
        buildingSizes.put(Units.TERRAN_FUSION_CORE, MIDDLE_SIZE);

        buildingSizes.put(Units.ZERG_HATCHERY, BIG_SIZE);
        buildingSizes.put(Units.ZERG_LAIR, BIG_SIZE);
        buildingSizes.put(Units.ZERG_HIVE, BIG_SIZE);
        buildingSizes.put(Units.ZERG_EXTRACTOR, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_SPAWNING_POOL, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_EVOLUTION_CHAMBER, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_ROACH_BURROWED, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_BANELING_NEST, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_SPINE_CRAWLER, SMALL_SIZE);
        buildingSizes.put(Units.ZERG_SPORE_CRAWLER, SMALL_SIZE);
        buildingSizes.put(Units.ZERG_HYDRALISK_DEN, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_INFESTATION_PIT, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_LURKER_DEN_MP, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_SPIRE, SMALL_SIZE);
        buildingSizes.put(Units.ZERG_NYDUS_NETWORK, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_NYDUS_CANAL, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_ULTRALISK_CAVERN, MIDDLE_SIZE);
        buildingSizes.put(Units.ZERG_CREEP_TUMOR, TINY_SIZE);
        buildingSizes.put(Units.ZERG_CREEP_TUMOR_BURROWED, TINY_SIZE);
        buildingSizes.put(Units.ZERG_CREEP_TUMOR_QUEEN, TINY_SIZE);
        buildingSizes.put(Units.ZERG_GREATER_SPIRE, MIDDLE_SIZE);

        buildingSizes.put(Units.PROTOSS_NEXUS, BIG_SIZE);
        buildingSizes.put(Units.PROTOSS_GATEWAY, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_WARP_GATE, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_ROBOTICS_FACILITY, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_STARGATE, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_ASSIMILATOR, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_PYLON, SMALL_SIZE);
        buildingSizes.put(Units.PROTOSS_FORGE, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_CYBERNETICS_CORE, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_PHOTON_CANNON, SMALL_SIZE);
        buildingSizes.put(Units.PROTOSS_SHIELD_BATTERY, SMALL_SIZE);
        buildingSizes.put(Units.PROTOSS_TWILIGHT_COUNCIL, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_TEMPLAR_ARCHIVE, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_DARK_SHRINE, SMALL_SIZE);
        buildingSizes.put(Units.PROTOSS_FLEET_BEACON, MIDDLE_SIZE);
        buildingSizes.put(Units.PROTOSS_ROBOTICS_BAY, MIDDLE_SIZE);
    }

    private BuildingSizeUtils() {
        throw new IllegalAccessError("Utility class");
    }


    public static Size getBuildingSize(Units unitType) {
        // 존재하지는 않지만, 오류 발생 하지않도록 가장 많이 사용되는 중간 사이즈로 반환하도록
        return buildingSizes.getOrDefault(unitType, MIDDLE_SIZE);
    }

    public static boolean isBuildingType(Units unitType) {
        return buildingSizes.containsKey(unitType);
    }

    public static boolean isMineralType(Units unitType) {
        return unitType.name().contains("MINERAL");
    }

    public static boolean isGasGeyserType(Units unitType) {
        return unitType.name().contains("VESPENE_GEYSER");
    }

    public static boolean isDestructable(Units unitType) {
        return unitType.name().contains("DESTRUCTIBLE");
    }
}
