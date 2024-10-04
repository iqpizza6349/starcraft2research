package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Units;

import static io.iqpizza.starcraft.qlearning.protocol.BuildingType.*;

/**
 * 건물이 실제로 추가되는 경우에 사용된다.
 * 건물이 변형되는 것은 제외한다.
 */
public enum ConstructActionType implements InteractiveActionType {
    CONSTRUCT_COMMAND(Abilities.BUILD_COMMAND_CENTER, Units.TERRAN_SCV, Units.TERRAN_COMMAND_CENTER) {
        @Override
        public boolean isExecutable(State currentState) {
            // 커멘드 센터는 일꾼 12 초과 + 미네랄 400 원 이상
            return currentState.workerSupply() > 12 && currentState.minerals() >= 400;
        }
    },
    CONSTRUCT_BARRACKS(Abilities.BUILD_BARRACKS, Units.TERRAN_SCV, Units.TERRAN_BARRACKS) {
        @Override
        public boolean isExecutable(State currentState) {
            // 배럭은 미네랄 150 이상 + 보급고 필요
            return currentState.hasWorker() && currentState.minerals() >= 150 && currentState.hasBuildiing(TERRAN_SUPPLY_DEPOT);
        }
    },
    ADDON_REACTOR_BARRACKS(Abilities.BUILD_REACTOR_BARRACKS, Units.TERRAN_BARRACKS, Units.TERRAN_BARRACKS_REACTOR) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasEnoughResourceToAddon(TERRAN_BARRACKS, getChild());
        }
    },
    ADDON_TECHLAB_BARRACKS(Abilities.BUILD_TECHLAB_BARRACKS, Units.TERRAN_BARRACKS, Units.TERRAN_BARRACKS_TECHLAB) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasEnoughResourceToAddon(TERRAN_BARRACKS, getChild());
        }
    },
    CONSTRUCT_FACTORY(Abilities.BUILD_FACTORY, Units.TERRAN_SCV, Units.TERRAN_FACTORY) {
        @Override
        public boolean isExecutable(State currentState) {
            // 배럭만 존재 + 150 미네랄 + 100 가스
            return currentState.hasWorker() && currentState.hasBuildiing(TERRAN_BARRACKS) && currentState.minerals() >= 150 && currentState.gas() >= 100;
        }
    },
    ADDON_REACTOR_FACTORY(Abilities.BUILD_REACTOR_FACTORY, Units.TERRAN_FACTORY, Units.TERRAN_FACTORY_REACTOR) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasEnoughResourceToAddon(TERRAN_FACTORY, getChild());
        }
    },
    ADDON_TECHLAB_FACTORY(Abilities.BUILD_TECHLAB_FACTORY, Units.TERRAN_FACTORY, Units.TERRAN_FACTORY_TECHLAB) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasEnoughResourceToAddon(TERRAN_FACTORY, getChild());
        }
    },
    CONSTRUCT_STARPORT(Abilities.BUILD_STARPORT, Units.TERRAN_SCV, Units.TERRAN_STARPORT) {
        @Override
        public boolean isExecutable(State currentState) {
            // 팩토리 + 150 + 100
            return currentState.hasWorker() && currentState.hasBuildiing(TERRAN_FACTORY) && currentState.minerals() >= 150 && currentState.gas() >= 100;
        }
    },
    ADDON_REACTOR_STARPORT(Abilities.BUILD_REACTOR_STARPORT, Units.TERRAN_STARPORT, Units.TERRAN_STARPORT_REACTOR) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasEnoughResourceToAddon(TERRAN_STARPORT, getChild());
        }
    },
    ADDON_TECHLAB_STARPORT(Abilities.BUILD_TECHLAB_STARPORT, Units.TERRAN_STARPORT, Units.TERRAN_STARPORT_TECHLAB) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasEnoughResourceToAddon(TERRAN_STARPORT, getChild());
        }
    },
    CONSTRUCT_REFINERY(Abilities.BUILD_REFINERY, Units.TERRAN_SCV, Units.TERRAN_REFINERY) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasWorker() && currentState.minerals() >= 75;
        }
    },
    CONSTRUCT_SUPPLY_DEPOT(Abilities.BUILD_SUPPLY_DEPOT, Units.TERRAN_SCV, Units.TERRAN_SUPPLY_DEPOT) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasWorker() && currentState.minerals() >= 100;
        }
    },
    CONSTRUCT_ENGINEERING_BAY(Abilities.BUILD_ENGINEERING_BAY, Units.TERRAN_SCV, Units.TERRAN_ENGINEERING_BAY) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasWorker() && currentState.hasBuildiing(TERRAN_COMMAND_CENTER) && currentState.minerals() >= 125;
        }
    },
    CONSTRUCT_BUNKER(Abilities.BUILD_BUNKER, Units.TERRAN_SCV, Units.TERRAN_BUNKER) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasWorker() && currentState.hasBuildiing(TERRAN_BARRACKS) && currentState.minerals() >= 100;
        }
    },
    CONSTRUCT_TURRET(Abilities.BUILD_MISSILE_TURRET, Units.TERRAN_SCV, Units.TERRAN_MISSILE_TURRET) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasWorker() && currentState.hasBuildiing(TERRAN_ENGINEERING_BAY) && currentState.minerals() >= 100;
        }
    },
    CONSTRUCT_GHOST_ACADEMY(Abilities.BUILD_GHOST_ACADEMY, Units.TERRAN_SCV, Units.TERRAN_GHOST_ACADEMY) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasWorker() && currentState.hasBuildiing(TERRAN_BARRACKS) && currentState.minerals() >= 150 && currentState.gas() >= 50;
        }
    },
    CONSTRUCT_ARMORY(Abilities.BUILD_ARMORY, Units.TERRAN_SCV, Units.TERRAN_ARMORY) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasWorker() && currentState.hasBuildiing(TERRAN_FACTORY) && currentState.minerals() >= 150 && currentState.gas() >= 50;
        }
    },
    CONSTRUCT_FUSION_CORE(Abilities.BUILD_FUSION_CORE, Units.TERRAN_SCV, Units.TERRAN_FUSION_CORE) {
        @Override
        public boolean isExecutable(State currentState) {
            return currentState.hasWorker() && currentState.hasBuildiing(TERRAN_STARPORT) && currentState.minerals() >= 150 && currentState.gas() >= 150;
        }
    },
    ;

    private final Abilities ability;
    private final Units selfType;
    private final Units structType;

    ConstructActionType(Abilities ability, Units selfType, Units structType) {
        this.ability = ability;
        this.selfType = selfType;
        this.structType = structType;
    }

    @Override
    public Abilities getAbility() {
        return ability;
    }

    @Override
    public boolean isConstruct() {
        return true;
    }

    @Override
    public Units getParent() {
        return selfType;
    }

    @Override
    public Units getChild() {
        return structType;
    }
}
