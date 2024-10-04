package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static com.github.ocraft.s2client.protocol.data.Units.*;

@Getter
public enum BuildingType {
    // Terran
    TERRAN_ARMORY(Units.TERRAN_ARMORY),
    TERRAN_BARRACKS(Units.TERRAN_BARRACKS, TERRAN_BARRACKS_FLYING, List.of(TERRAN_BARRACKS_REACTOR, TERRAN_BARRACKS_TECHLAB)),
    TERRAN_BUNKER(Units.TERRAN_BUNKER),
    TERRAN_COMMAND_CENTER(Units.TERRAN_COMMAND_CENTER, TERRAN_COMMAND_CENTER_FLYING, List.of(TERRAN_ORBITAL_COMMAND, TERRAN_ORBITAL_COMMAND_FLYING, TERRAN_PLANETARY_FORTRESS)),
    TERRAN_ENGINEERING_BAY(Units.TERRAN_ENGINEERING_BAY),
    TERRAN_FACTORY(Units.TERRAN_FACTORY, TERRAN_FACTORY_FLYING, List.of(TERRAN_FACTORY_REACTOR, TERRAN_FACTORY_TECHLAB)),
    TERRAN_FUSION_CORE(Units.TERRAN_FUSION_CORE),
    TERRAN_GHOST_ACADEMY(Units.TERRAN_GHOST_ACADEMY),
    TERRAN_MISSILE_TURRET(Units.TERRAN_MISSILE_TURRET),
    TERRAN_REFINERY(Units.TERRAN_REFINERY),
    TERRAN_REFINERY_RICH(Units.TERRAN_REFINERY_RICH),      /* 실제로 사용되는 지는 미상 */
    TERRAN_SENSOR_TOWER(Units.TERRAN_SENSOR_TOWER),
    TERRAN_STARPORT(Units.TERRAN_STARPORT, TERRAN_STARPORT_FLYING, List.of(TERRAN_STARPORT_REACTOR, TERRAN_STARPORT_TECHLAB)),
    TERRAN_SUPPLY_DEPOT(Units.TERRAN_SUPPLY_DEPOT, Units.TERRAN_ARMORY),

    // Zerg
    ZERG_BANELING_NEST(Units.ZERG_BANELING_NEST),
    ZERG_CREEP_TUMOR(Units.ZERG_CREEP_TUMOR, Units.ZERG_CREEP_TUMOR_BURROWED),
    ZERG_EVOLUTION_CHAMBER(Units.ZERG_EVOLUTION_CHAMBER),
    ZERG_EXTRACTOR(Units.ZERG_EXTRACTOR),
    ZERG_EXTRACTOR_RICH(Units.ZERG_EXTRACTOR_RICH),    /* 실제로 사용되는 지는 미상 */
    ZERG_HATCHERY(Units.ZERG_HATCHERY, null, List.of(ZERG_LAIR, ZERG_HIVE)),
    ZERG_HYDRALISK_DEN(Units.ZERG_HYDRALISK_DEN),
    ZERG_LURKER_DEN_MP(Units.ZERG_LURKER_DEN_MP), /* 히드라리스크 굴에서 진화라고 명시되어 있지만 미상 */
    ZERG_NYDUS_CANAL(Units.ZERG_NYDUS_CANAL, null, List.of(ZERG_NYDUS_NETWORK)),
    ZERG_ROACH_WARREN(Units.ZERG_ROACH_WARREN),
    ZERG_SPAWNING_POOL(Units.ZERG_SPAWNING_POOL, Units.TERRAN_ARMORY),
    ZERG_SPINE_CRAWLER(Units.ZERG_SPINE_CRAWLER, Units.ZERG_SPINE_CRAWLER_UPROOTED),
    ZERG_SPIRE(Units.ZERG_SPIRE, null, List.of(ZERG_GREATER_SPIRE)),
    ZERG_SPORE_CRAWLER(Units.ZERG_SPORE_CRAWLER, Units.ZERG_SPORE_CRAWLER_UPROOTED),
    ZERG_ULTRALISK_CAVERN(Units.ZERG_ULTRALISK_CAVERN, Units.TERRAN_ARMORY),

    // Protoss
    PROTOSS_ASSIMILATOR(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_ASSIMILATOR_RICH(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_CYBERNETICS_CORE(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_DARK_SHRINE(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_FLEET_BEACON(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_FORGE(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_GATEWAY(Units.PROTOSS_ASSIMILATOR, null, List.of(PROTOSS_WARP_GATE)),
    PROTOSS_NEXUS(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_PHOTON_CANNON(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_PYLON(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_ROBOTICS_BAY(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_ROBOTICS_FACILITY(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_SHIELD_BATTERY(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_TEMPLAR_ARCHIVE(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_TWILIGHT_COUNCIL(Units.PROTOSS_ASSIMILATOR),
    PROTOSS_STARGATE(Units.PROTOSS_ASSIMILATOR),

    ;

    private final Units unitType;
    private final Units oppsType; // 지상 공중 둘 다 존재하는 경우
    private final List<Units> morphsTo; // 변형 모드 (부착되는 에드온은 별도로 취급)

    BuildingType(Units unitType, Units oppsType, List<Units> morphsTo) {
        this.unitType = unitType;
        this.oppsType = oppsType;
        this.morphsTo = morphsTo;
    }

    BuildingType(Units unitType) {
        this.unitType = unitType;
        this.oppsType = null;
        this.morphsTo = Collections.emptyList();
    }

    BuildingType(Units unitType, Units oppsType) {
        this.unitType = unitType;
        this.oppsType = oppsType;
        this.morphsTo = Collections.emptyList();
    }

    public static BuildingType of(Units unitType) {
        for (BuildingType type : BuildingType.values()) {
            if (type.unitType == unitType) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown building type: " + unitType);
    }

    public static BuildingType of(UnitType unitType) {
        return of((Units) unitType);
    }

    public static boolean isBuildingType(UnitType unitType) {
        try {
            of(unitType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
