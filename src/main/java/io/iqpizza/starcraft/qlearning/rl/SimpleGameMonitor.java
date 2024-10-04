package io.iqpizza.starcraft.qlearning.rl;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import io.iqpizza.starcraft.GameManager;

public class SimpleGameMonitor implements GameProgressMonitor {

    @Override
    public boolean isGameEnd() {
        ResponseGameInfo info = GameManager.getObserver().getGameInfo();
        // 게임이 종료되었거나 게임 자체가 종료된 경우
        return info.getStatus() == GameStatus.ENDED || info.getStatus() == GameStatus.QUIT;
    }

    @Override
    public boolean isGameStarted() {
        ResponseGameInfo info = GameManager.getObserver().getGameInfo();
        return !isGameEnd() && !isGameReplay() && info.getStatus() == GameStatus.IN_GAME;
    }

    @Override
    public boolean isGameReplay() {
        ResponseGameInfo info = GameManager.getObserver().getGameInfo();
        return info.getStatus() == GameStatus.IN_REPLAY;
    }
}
