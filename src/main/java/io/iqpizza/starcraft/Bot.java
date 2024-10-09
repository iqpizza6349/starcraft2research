package io.iqpizza.starcraft;

import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Unit;
import io.iqpizza.starcraft.debug.GameDebugDrawer;
import io.iqpizza.starcraft.simcity.Building;
import io.iqpizza.starcraft.simcity.BuildingSizeUtils;
import io.iqpizza.starcraft.simcity.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.data.Abilities.SMART;

public class Bot extends S2Agent {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    @Override
    public void onGameStart() {
        // 게임이 시작. 정확히는 로깅 상태일때는 단순 로그만 남기면 된다.
        log.info("Starcraft Ⅱ Bot says: 'Hello, World!'");
        GameManager.getInstance().updateObservers(observation(), actions(), query());   // 옵저버, 프록시 등 최초 초기화
        GameManager.getInstance().initializeMap();  // Map 초기화
        initializeStaticUnits();
    }

    // 정적 유닛인 미네랄, 가스, 파괴가능한 조형(바위 등)을 초기화한다.
    private void initializeStaticUnits() {
        List<UnitInPool> allUnits = observation().getUnits();
        for (UnitInPool unitPool : allUnits) {
            Unit unit = unitPool.unit();
            Point2d position = unit.getPosition().toPoint2d();

            if (unit.getType() instanceof Units units) {
                int posX = (int) position.getX();
                int posY = (int) position.getY();

                if (BuildingSizeUtils.isMineralType(units)) {
                    GameManager.getInstance().updateMineralField(posX, posY, true);
                }
                else if (BuildingSizeUtils.isGasGeyserType(units)) {
                    GameManager.getInstance().updateGasField(posX, posY, true);
                }
                else if (BuildingSizeUtils.isDestructable(units)) {
                    GameManager.getInstance().updateDestructableStructure(posX, posY, true);
                }
            }
        }
    }

    @Override
    public void onGameEnd() {
        log.info("End game...");
    }

    @Override
    public void onStep() {
        if (observation().getGameLoop() % 24 != 0) {
            return;
        }

        // 디버깅 :: 미네랄과 가스 위치 그리기
        GameDebugDrawer.drawResources(debug(), GameManager.getInstance().getGameMap());
    }

    @Override
    public void onUnitIdle(UnitInPool unitInPool) {
        Unit unit = unitInPool.unit();
        if (Objects.requireNonNull((Units) unit.getType()) == Units.TERRAN_SCV) {
            findNearestMineralPatch(unit.getPosition().toPoint2d())
                    .ifPresent(mineralPath -> actions().unitCommand(unit, SMART, mineralPath, false));
        }
    }

    // 게임을 재시작하는 메서드
    public void restartGame() {
        log.info("Restarting game...");
        // S2Client를 통해 새 게임 시작
        try {
            agentControl().restart(); // S2Client에 있는 게임 재시작 메서드 호출
        } catch (Exception e) {
            log.error("Error occurs while restart.", e);
        }
    }

    private Optional<Unit> findNearestMineralPatch(Point2d start) {
        List<UnitInPool> units = observation().getUnits(Alliance.NEUTRAL);
        double distance = Double.MAX_VALUE;
        Unit target = null;
        for (UnitInPool unitInPool : units) {
            Unit unit = unitInPool.unit();
            if (unit.getType().equals(Units.NEUTRAL_MINERAL_FIELD)) {
                double d = unit.getPosition().toPoint2d().distance(start);
                if (d < distance) {
                    distance = d;
                    target = unit;
                }
            }
        }
        return Optional.ofNullable(target);
    }

    @Override
    public void onUnitDestroyed(UnitInPool unitInPool) {
        Unit unit = unitInPool.unit();
        Units units = (Units) unit.getType();
        Point2d position = unit.getPosition().toPoint2d();

        if (BuildingSizeUtils.isMineralType(units) || BuildingSizeUtils.isGasGeyserType(units)) {
            if (BuildingSizeUtils.isMineralType(units)) {
                GameManager.getInstance().updateMineralField((int) position.getX(), (int) position.getY(), false);
            } else {
                GameManager.getInstance().updateGasField((int) position.getX(), (int) position.getY(), false);
            }
        }
        else if (BuildingSizeUtils.isDestructable(units)) {
            GameManager.getInstance().updateDestructableStructure((int) position.getX(), (int) position.getY(), false);
        }
        else if (BuildingSizeUtils.isBuildingType(units)) {
            Size buildingSize = BuildingSizeUtils.getBuildingSize(units);

            GameManager.getInstance().destroyBuilding((int) position.getX(), (int) position.getY(), buildingSize);
        }
    }

    @Override
    public void onBuildingConstructionComplete(UnitInPool unitInPool) {
        Unit unit = unitInPool.unit();
        Units units = (Units) unit.getType();
        if (BuildingSizeUtils.isBuildingType(units)) {
            Point2d position = unit.getPosition().toPoint2d();
            Size buildingSize = BuildingSizeUtils.getBuildingSize(units);

            GameManager.getInstance().placeBuliding((int) position.getX(), (int) position.getY(), new Building(units, buildingSize));
        }
    }
}
