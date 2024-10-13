package io.iqpizza.starcraft;

import com.github.ocraft.s2client.bot.gateway.ActionInterface;
import com.github.ocraft.s2client.bot.gateway.ObservationInterface;
import com.github.ocraft.s2client.bot.gateway.QueryInterface;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.game.raw.StartRaw;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import com.github.ocraft.s2client.protocol.spatial.RectangleI;
import com.github.ocraft.s2client.protocol.spatial.Size2dI;
import io.iqpizza.starcraft.simcity.Building;
import io.iqpizza.starcraft.simcity.ConstructManager;
import io.iqpizza.starcraft.simcity.GameMap;
import io.iqpizza.starcraft.simcity.Size;
import lombok.Getter;

import java.util.Optional;

@SuppressWarnings("squid:S6548")
public final class GameManager {
    @Getter
    private static final GameManager instance = new GameManager();
    private ObservationInterface observer;
    private ActionInterface action;
    private QueryInterface query;
    @Getter
    private GameMap gameMap;
    @Getter
    private final ConstructManager constructManager;

    public GameManager() {
        this.constructManager = ConstructManager.getInstance();
    }

    public static synchronized ObservationInterface getObserver() {
        return getInstance().observer;
    }

    public static synchronized ActionInterface getAction() {
        return getInstance().action;
    }

    public static synchronized QueryInterface getQuery() {
        return getInstance().query;
    }

    public synchronized void initializeMap() {
        if (observer == null) {
            throw new IllegalStateException("Observer not initialized");
        }

        Optional<StartRaw> startRaw = observer.getGameInfo().getStartRaw();
        if (startRaw.isPresent()) {
            StartRaw raw = startRaw.get();
            Size2dI mapSize = raw.getMapSize();
            initializeMap(mapSize.getX(), mapSize.getY(), raw.getPlayableArea());
        }

        // 맵이 초기화된다면 내 건물 리스트들도 전부 초기화
        constructManager.clearBuildings();
    }

    public synchronized void updateObservers(ObservationInterface observer, ActionInterface action, QueryInterface query) {
        this.observer = observer;
        this.action = action;
        this.query = query;
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

    private static void initializeMap(int width, int height, RectangleI playable) {
        getInstance().gameMap = new GameMap(width, height, playable);
    }

    public void placeBuliding(int x, int y, Building building) {
        gameMap.placeBuilding(x, y, building);
    }

    public void destroyBuilding(int x, int y, Size buildingSize) {
        gameMap.destroyBuilding(x, y, buildingSize);
    }

    public void updateMineralField(int x, int y, boolean hasMinerals) {
        gameMap.updateMineralField(x, y, hasMinerals);
    }

    public void updateGasField(int x, int y, boolean hasGas) {
        gameMap.updateVespeneGas(x, y, hasGas);
    }

    public void updateDestructableStructure(int x, int y, boolean hasDestructable) {
        gameMap.updateDestructableStructure(x, y, hasDestructable);
    }

    public void logMap() {
        gameMap.printMap();
    }
}
