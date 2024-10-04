package io.iqpizza.starcraft.qlearning;

import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.protocol.game.Race;
import io.iqpizza.starcraft.Bot;
import io.iqpizza.starcraft.GameManager;
import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.State;
import io.iqpizza.starcraft.qlearning.qtable.QTableSaver;
import io.iqpizza.starcraft.qlearning.rl.ActionExecutor;
import io.iqpizza.starcraft.qlearning.rl.GameProgressMonitor;
import io.iqpizza.starcraft.qlearning.rl.RewardCalculator;
import io.iqpizza.starcraft.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Agent {
    private final Bot bot;
    private final QLearningAgent learningAgent;

    private final GameProgressMonitor progressMonitor;

    private final ActionExecutor actionExecutor;

    private final RewardCalculator rewardCalculator;

    /**
     * 초기 상태 설정
     */
    private State initialState;

    public Agent(Bot bot, QLearningAgent learningAgent, GameProgressMonitor progressMonitor,
                 ActionExecutor actionExecutor, RewardCalculator rewardCalculator, State initialState) {
        this.bot = bot;
        this.learningAgent = learningAgent;
        this.progressMonitor = progressMonitor;
        this.actionExecutor = actionExecutor;
        this.rewardCalculator = rewardCalculator;
        this.initialState = initialState;
    }

    public Agent(Bot bot, QLearningAgent learningAgent, GameProgressMonitor progressMonitor,
                 ActionExecutor actionExecutor, RewardCalculator rewardCalculator) {
        this.bot = bot;
        this.learningAgent = learningAgent;
        this.progressMonitor = progressMonitor;
        this.actionExecutor = actionExecutor;
        this.rewardCalculator = rewardCalculator;
    }

    public void train(S2Coordinator coordinator, int numEpisodes) {
        for (int episode = 0; episode < numEpisodes; episode++) {
            log.info("Start {} episode.", (episode + 1));
            if (episode > 0) {
                bot.restartGame();
            }

            runEpsiode(coordinator, episode);
        }

        log.info("총 {} Epsiode 를 수행 완료함.", numEpisodes);
    }

    private void runEpsiode(S2Coordinator coordinator, int episode) {
        State state = getInitialState();    // 게임의 초기 상태
        String mapName = GameManager.getMapName(); // 게임의 맵
        boolean done = false;

        while (!done && coordinator.update()) {
            if (GameManager.getInstance().isLatestState()) {
                // 현재 상태에서 행동 선택
                ActionType action = learningAgent.chooseAction(state);

                if (action != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("choose action: {}. minerals: {}, gas: {}", action, state.minerals(), state.gas());
                    }

                    // 선택한 행동을 환경에 적용하고 보상과 다음 상태 받기
                    State nextState = actionExecutor.execute(action);
                    log.debug("{} action executed.", action);

                    int reward = rewardCalculator.calculateReward(state, action, nextState);
                    log.debug("give reward for action {}: {}", action, reward);

                    done = progressMonitor.isGameEnd();  // 게임이 끝났는 지 확인

                    // Q 테이블 업데이트
                    learningAgent.updateQTable(state, action, reward, nextState);

                    // 상태 갱신
                    state = nextState;
                }
                else {
                    // 행동이 null, 즉 없을 경우 행동 없이 값만 갱신
                    state = actionExecutor.getCurrentState();
                }
            }
        }

        // 탐험율을 점진적으로 감소
        learningAgent.gradualDecreaseExplorationRate();
        log.info("End {} episode.", (episode + 1));

        QTableSaver.saveQTableToCsv(learningAgent.getQtable(), createFilename(state.myRace(), state.enemyRace(), mapName));
        QTableSaver.tryMergeToSingleCsv(createMergedFilename(state.myRace()));
    }

    private State getInitialState() {
        return (initialState != null) ? initialState : actionExecutor.getCurrentState();
    }

    private String createFilename(Race myRace, Race enemyRace, String mapName) {
        // 현재 날짜 및 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // 날짜와 시간 포맷터
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss");

        // 포맷된 날짜와 시간
        String formattedDateTime = now.format(formatter);

        return String.format("%s%s %s vs %s - %s.csv", PropertiesUtils.getVariable(PropertiesUtils.PropertyVariable.CSV),
                formattedDateTime, myRace.name(), enemyRace.name(), mapName);
    }

    private String createMergedFilename(Race myRace) {
        return String.format("%s%s merged.csv", PropertiesUtils.getVariable(PropertiesUtils.PropertyVariable.CSV), myRace.name());
    }
}
