package io.iqpizza.starcraft;

import com.github.ocraft.s2client.bot.gateway.ActionInterface;
import com.github.ocraft.s2client.bot.gateway.ObservationInterface;
import com.github.ocraft.s2client.bot.gateway.QueryInterface;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import io.iqpizza.starcraft.qlearning.rl.ActionExecutor;
import lombok.Getter;

@SuppressWarnings("squid:S6548")
public final class GameManager {
    @Getter
    private static final GameManager instance = new GameManager();
    private ObservationInterface observer;
    private ActionInterface action;
    private QueryInterface query;
    private boolean latestState;

    public static synchronized ObservationInterface getObserver() {
        return getInstance().observer;
    }

    public static synchronized ActionInterface getAction() {
        return getInstance().action;
    }

    public static synchronized QueryInterface getQuery() {
        return getInstance().query;
    }

    public synchronized void updateObservers(ObservationInterface observer, ActionInterface action, QueryInterface query) {
        this.observer = observer;
        this.action = action;
        this.query = query;
        this.latestState = true;
    }

    /**
     * S2 기준의 가장 최신 데이터를 사용했을 때, 즉
     * {@link ActionExecutor#getCurrentState()} 을 호출했을 때 내부에서 호출하도록 합니다.
     */
    public synchronized void useCurrentState() {
        this.latestState = false;
    }

    public synchronized boolean isLatestState() {
        return latestState;
    }

    public static Race getRace(boolean self) {
        ResponseGameInfo gameInfo = getObserver().getGameInfo();
        return gameInfo.getPlayersInfo().stream()
                .filter(playerInfo -> (self == (playerInfo.getPlayerId() == getObserver().getPlayerId())))
                .findFirst()
                .map(playerInfo -> playerInfo.getActualRace().orElse(Race.RANDOM))
                .orElse(Race.RANDOM);
    }

    public static String getMapName() {
        ResponseGameInfo gameInfo = getObserver().getGameInfo();
        return gameInfo.getMapName();
    }
}
