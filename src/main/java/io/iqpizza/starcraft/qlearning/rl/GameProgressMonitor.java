package io.iqpizza.starcraft.qlearning.rl;

public interface GameProgressMonitor {
    /**
     * 게임이 종료된 지 체크
     * @return 게임이 종료된 경우 true, 그외 false
     */
    boolean isGameEnd();

    /**
     * 게임이 시작된 지 체크
     * @return 게임이 시작되었거나 진행 중이라면 true, 그 외 false.
     */
    boolean isGameStarted();

    /**
     * 게임이 replay 상태인지 체크
     * @return Replay 모드라면 true, 그 외 false
     */
    boolean isGameReplay();
}
