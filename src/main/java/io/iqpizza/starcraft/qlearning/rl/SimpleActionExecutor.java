package io.iqpizza.starcraft.qlearning.rl;

import com.github.ocraft.s2client.bot.gateway.ObservationInterface;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Unit;
import io.iqpizza.starcraft.GameManager;
import io.iqpizza.starcraft.qlearning.protocol.*;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleActionExecutor implements ActionExecutor {

    @Override
    public State execute(ActionType action) {
        // 행동을 수행할 유닛이나 건물 등을 선택 (여기선 예시로 첫 번째 유닛 선택)
        Units unitType = Units.INVALID;
        if (action.isAttack()) {
            //TODO: 공격은 나중에 개발 예정
            return getCurrentState();
        }

        if (action instanceof InteractiveActionType interactiveActionType) {
            unitType = interactiveActionType.getParent();
        }
        else if (action instanceof EffectActionType effectActionType) {
            unitType = effectActionType.getCaster();
        }

        Optional<Unit> unitOptional = getFirstAvailableUnit(unitType);
        if (unitOptional.isEmpty()) {
            // 만약 유닛이 없다면, 상태를 그대로 반환하거나 에러 처리
            return getCurrentState();
        }

        Unit unit = unitOptional.get();

        // 행동을 실행
        if (action.isConstruct()) {
            //TODO: 건물 건설 위치 지정을 보다 효과적으로 지정해줘야함
            GameManager.getAction().unitCommand(unit, action.getAbility(),
                    unit.getPosition().toPoint2d().add(Point2d.of(getRandomScalar(), getRandomScalar()).mul(15.0f)),
                    false);
        }
        else if (action.isTrain()) {
            GameManager.getAction().unitCommand(unit, action.getAbility(), false);
        }

        // 행동 이후의 새로운 상태를 반환
        return getCurrentState();
    }

    /**
     * 현재 게임의 상태를 가져옵니다.
     * @return State 현재 상태
     */
    @Override
    public State getCurrentState() {
        // 현재 게임 관측 데이터를 통해 상태를 얻습니다.
        ObservationInterface observation = GameManager.getObserver();

        // 자원 상태, 유닛 상태 등 게임의 현재 상태를 갱신
        int minerals = observation.getMinerals();
        int gas = observation.getVespene();
        int supplyUsed = observation.getFoodUsed();
        int supplyMax = observation.getFoodCap();
        int workerSupply = observation.getFoodWorkers();
        int armySupply = observation.getFoodArmy();

        // 유닛 및 건물 정보 갱신
        Map<UnitType, Integer> myUnits = getUnitCounts(observation, true);
        Map<UnitType, Integer> enemyUnits = getUnitCounts(observation, false);
        Map<BuildingType, Integer> myBuildings = getBuildingCounts(observation);
        Point2d enemyBaseLocation = getEnemyBaseLocation(observation);

        // 현재 종족
        Race myRace = GameManager.getRace(true);    // 내 종족
        Race enemyRace = GameManager.getRace(false);// 상대 종족

        // 갱신된 상태를 반환
        return new State(minerals, gas, supplyUsed, supplyMax, workerSupply, armySupply,
                myUnits, enemyUnits, myBuildings, enemyBaseLocation, myRace, enemyRace);
    }

    /**
     * 게임에서 첫 번째 유닛을 가져옵니다.
     * @return Optional<Unit> 첫 번째 유닛
     */
    private Optional<Unit> getFirstAvailableUnit(Units units) {
        Optional<UnitInPool> first = GameManager.getObserver().getUnits().stream()
                .filter(unitInPool -> unitInPool.unit().getType() == units)
                .findFirst(); // 첫 번째 유닛 선택
        return first.stream().findFirst().map(UnitInPool::unit);
    }

    /**
     * 주어진 관측 데이터에서 유닛 개수를 세는 메서드
     * @param observation 관측 데이터
     * @param isSelf true이면 내 유닛, false이면 적 유닛
     * @return 각 유닛 타입과 해당 유닛 수를 맵으로 반환
     */
    private Map<UnitType, Integer> getUnitCounts(ObservationInterface observation, boolean isSelf) {
        Map<UnitType, Integer> unitCounts = new HashMap<>();
        for (UnitInPool unit : observation.getUnits()) {
            Unit rawUnit = unit.unit();
            UnitType unitType = rawUnit.getType();
            if ((isSelf && rawUnit.getOwner() == observation.getPlayerId()) ||
                    (!isSelf && rawUnit.getOwner() != observation.getPlayerId())) {
                unitCounts.put(unitType, unitCounts.getOrDefault(unitType, 0) + 1);
            }
        }
        return unitCounts;
    }

    /**
     * 주어진 관측 데이터에서 건물 개수를 세는 메서드
     * @param observation 관측 데이터
     * @return 각 건물 타입과 해당 건물 수를 맵으로 반환
     */
    private Map<BuildingType, Integer> getBuildingCounts(ObservationInterface observation) {
        Map<BuildingType, Integer> buildingCounts = new EnumMap<>(BuildingType.class);
        for (UnitInPool unit : observation.getUnits()) {
            Unit rawUnit = unit.unit();
            if (BuildingType.isBuildingType(rawUnit.getType())) {
                BuildingType buildingType = BuildingType.of(rawUnit.getType());
                buildingCounts.put(buildingType, buildingCounts.getOrDefault(buildingType, 0) + 1);
            }
        }
        return buildingCounts;
    }

    /**
     * 적 기지의 위치를 반환하는 메서드
     * @param observation 관측 데이터
     * @return Point2d 적 기지 위치
     */
    private Point2d getEnemyBaseLocation(ObservationInterface observation) {
        // 적 기지의 위치를 추적하는 로직 (예시: 관측된 적 유닛의 위치 기반)
        return observation.getUnits(Alliance.ENEMY).stream()
                .map(unitInPool -> unitInPool.unit().getPosition())
                .findFirst()
                .map(Point::toPoint2d)
                .orElse(Point2d.of(0, 0)); // 적 기지를 못 찾았을 때 기본값
    }

    private float getRandomScalar() {
        return ThreadLocalRandom.current().nextFloat() * 2 - 1;
    }
}
