package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Units;
import io.iqpizza.starcraft.utils.UnitProductUtils;

//TODO: 2024-09-19 테란만 정의한다.
//TODO: 기본적으로 수행 가능 조건은 (해당 유닛의 요구되는 인구수) <= 현재 남은 인구수 + 미네랄 + 가스 + 생산될 건물 유무
public enum TrainActionType implements InteractiveActionType {
    TRAIN_SCV(Abilities.TRAIN_SCV, Units.TERRAN_COMMAND_CENTER, Units.TERRAN_SCV) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 50 && currentState.hasBuildiing(BuildingType.TERRAN_COMMAND_CENTER);
        }
    },
    TRAIN_MARINE(Abilities.TRAIN_MARINE, Units.TERRAN_BARRACKS, Units.TERRAN_MARINE) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 50 && currentState.hasBuildiing(BuildingType.TERRAN_BARRACKS);
        }
    },
    TRAIN_REAPER(Abilities.TRAIN_REAPER, Units.TERRAN_BARRACKS, Units.TERRAN_REAPER) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 50 && currentState.gas() >= 50 && currentState.hasBuildiing(BuildingType.TERRAN_BARRACKS);
        }
    },
    TRAIN_MARAUDER(Abilities.TRAIN_MARAUDER, Units.TERRAN_BARRACKS, Units.TERRAN_MARAUDER) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 100 && currentState.gas() >= 25
                    && currentState.hasBuildiing(BuildingType.TERRAN_BARRACKS)
                    && currentState.hasUnit(Units.TERRAN_BARRACKS_TECHLAB); // 배럭에 기술실 부착되어 있어야함
        }
    },
    TRAIN_GHOST(Abilities.TRAIN_GHOST, Units.TERRAN_BARRACKS, Units.TERRAN_GHOST) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 150 && currentState.gas() >= 125
                    && currentState.hasBuildiing(BuildingType.TERRAN_BARRACKS)
                    && currentState.hasUnit(Units.TERRAN_BARRACKS_TECHLAB)
                    && currentState.hasBuildiing(BuildingType.TERRAN_GHOST_ACADEMY);
        }
    },
    TRAIN_HELLION(Abilities.TRAIN_HELLION, Units.TERRAN_FACTORY, Units.TERRAN_HELLION) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 100 && currentState.hasBuildiing(BuildingType.TERRAN_FACTORY);
        }
    },
    TRAIN_HELLBAT(Abilities.TRAIN_HELLBAT, Units.TERRAN_FACTORY, Units.TERRAN_FIREBAT) {
        // 사실 얘가 맞는 지도 약간 의문임
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 50 && currentState.hasBuildiing(BuildingType.TERRAN_FACTORY)
                    && currentState.hasBuildiing(BuildingType.TERRAN_ARMORY);   // 아모리 필요
        }
    },
    TRAIN_MINE(Abilities.TRAIN_WIDOWMINE, Units.TERRAN_FACTORY, Units.TERRAN_WIDOWMINE) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 75 && currentState.gas() >= 25
                    && currentState.hasBuildiing(BuildingType.TERRAN_FACTORY);
        }
    },
    TRAIN_CYCLONE(Abilities.TRAIN_CYCLONE, Units.TERRAN_FACTORY, Units.TERRAN_CYCLONE) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 125 && currentState.gas() >= 50
                    && currentState.hasBuildiing(BuildingType.TERRAN_FACTORY);
        }
    },
    TRAIN_TANK(Abilities.TRAIN_SIEGE_TANK, Units.TERRAN_FACTORY, Units.TERRAN_SIEGE_TANK) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 150 && currentState.gas() >= 125
                    && currentState.hasBuildiing(BuildingType.TERRAN_FACTORY)
                    && currentState.hasUnit(Units.TERRAN_FACTORY_TECHLAB);
        }
    },
    TRAIN_THOR(Abilities.TRAIN_THOR, Units.TERRAN_FACTORY, Units.TERRAN_THOR) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 300 && currentState.gas() >= 200
                    && currentState.hasBuildiing(BuildingType.TERRAN_FACTORY)
                    && currentState.hasUnit(Units.TERRAN_FACTORY_TECHLAB)
                    && currentState.hasBuildiing(BuildingType.TERRAN_ARMORY);
        }
    },
    TRAIN_VIKING(Abilities.TRAIN_VIKING_FIGHTER, Units.TERRAN_STARPORT, Units.TERRAN_VIKING_FIGHTER) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 150 && currentState.gas() >= 75
                    && currentState.hasBuildiing(BuildingType.TERRAN_STARPORT);
        }
    },
    TRAIN_MEDIVAC(Abilities.TRAIN_MEDIVAC, Units.TERRAN_STARPORT, Units.TERRAN_MEDIVAC) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 100 && currentState.gas() >= 100
                    && currentState.hasBuildiing(BuildingType.TERRAN_STARPORT);
        }
    },
    TRAIN_LIBERATOR(Abilities.TRAIN_LIBERATOR, Units.TERRAN_STARPORT, Units.TERRAN_LIBERATOR) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 150 && currentState.gas() >= 125
                    && currentState.hasBuildiing(BuildingType.TERRAN_STARPORT);
        }
    },
    TRAIN_BANSEE(Abilities.TRAIN_BANSHEE, Units.TERRAN_STARPORT, Units.TERRAN_BANSHEE) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 150 && currentState.gas() >= 100
                    && currentState.hasBuildiing(BuildingType.TERRAN_STARPORT)
                    && currentState.hasUnit(Units.TERRAN_STARPORT_TECHLAB);
        }
    },
    TRAIN_RAVEN(Abilities.TRAIN_RAVEN, Units.TERRAN_STARPORT, Units.TERRAN_RAVEN) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 100 && currentState.gas() >= 150
                    && currentState.hasBuildiing(BuildingType.TERRAN_STARPORT)
                    && currentState.hasUnit(Units.TERRAN_STARPORT_TECHLAB);
        }
    },
    TRAIN_BATTLE(Abilities.TRAIN_BATTLECRUISER, Units.TERRAN_STARPORT, Units.TERRAN_BATTLECRUISER) {
        @Override
        public boolean isExecutable(State currentState) {
            return UnitProductUtils.requiresSupply(getChild()) <= currentState.getSupplyLeft()
                    && currentState.minerals() >= 400 && currentState.gas() >= 300
                    && currentState.hasBuildiing(BuildingType.TERRAN_STARPORT)
                    && currentState.hasUnit(Units.TERRAN_STARPORT_TECHLAB)
                    && currentState.hasBuildiing(BuildingType.TERRAN_FUSION_CORE);
        }
    }
    ;

    private final Abilities ability;
    private final Units selfType;
    private final Units productType;

    TrainActionType(Abilities ability, Units selfType, Units productType) {
        this.ability = ability;
        this.selfType = selfType;
        this.productType = productType;
    }

    @Override
    public Abilities getAbility() {
        return ability;
    }

    @Override
    public boolean isTrain() {
        return true;
    }

    @Override
    public Units getParent() {
        return selfType;
    }

    @Override
    public Units getChild() {
        return productType;
    }
}
