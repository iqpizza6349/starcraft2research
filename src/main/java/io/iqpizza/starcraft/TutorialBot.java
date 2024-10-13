package io.iqpizza.starcraft;

import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;
import io.iqpizza.starcraft.qlearning.Agent;
import io.iqpizza.starcraft.qlearning.QLearningAgent;
import io.iqpizza.starcraft.qlearning.qtable.QTable;
import io.iqpizza.starcraft.qlearning.qtable.QTableLoader;
import io.iqpizza.starcraft.qlearning.rl.*;
import io.iqpizza.starcraft.qlearning.utils.QTableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

public class TutorialBot {
    private static final Logger log = LoggerFactory.getLogger(TutorialBot.class);

    public static void main(String[] args) {
        // 봇 생성 (단순 S2Coordinator 연결 및 데이터 갱신용)
        Bot bot = new Bot();
        QTable mergedTable = new QTable();

        log.info("Read CSV files from directory to initialize q-table...");
        List<QTable> qTables = QTableLoader.loadAllQTablesFromDirectory(PropertiesUtils.getVariable(PropertiesUtils.PropertyVariable.CSV));
        if (!qTables.isEmpty()) {
            mergedTable = QTableUtils.mergeQTables(qTables);
            log.info("Complete load q-values from csv... total file count = {}", qTables.size());
        }
        else {
            log.info("There are no csv files... new beginning");
        }

        log.info("Initializing Complete.");

        S2Coordinator coordinator = S2Coordinator.setup()
                .loadSettings(args)
                .setRealtime(true)
                .setMultithreaded(true)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.TERRAN, bot),
                        S2Coordinator.createComputer(Race.RANDOM, Difficulty.HARDER)
                )
                .launchStarcraft()
                .startGame(LocalMap.of(Path.of(String.format("%s5.13/Equilibrium513AIE.SC2Map", PropertiesUtils.getVariable(PropertiesUtils.PropertyVariable.MAP)))));

        // GameManager, Agent 설정
        QLearningAgent learningAgent = new QLearningAgent(mergedTable);
        GameProgressMonitor progressMonitor = new SimpleGameMonitor();
        ActionExecutor actionExecutor = new SimpleActionExecutor();
        RewardCalculator rewardCalculator = new SimpleRewardCalculator();

        Agent agent = new Agent(bot, learningAgent, progressMonitor, actionExecutor, rewardCalculator);
        agent.train(coordinator, 1);

        coordinator.quit();
    }
}
