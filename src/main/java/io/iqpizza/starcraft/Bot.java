package io.iqpizza.starcraft;

import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Unit;
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
        GameManager.getInstance().updateObservers(observation(), actions(), query());
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

        GameManager.getInstance().updateObservers(observation(), actions(), query());
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
}
