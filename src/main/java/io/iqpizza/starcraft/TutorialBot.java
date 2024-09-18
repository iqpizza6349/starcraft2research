package io.iqpizza.starcraft;

import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;
import io.iqpizza.starcraft.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class TutorialBot {
    private static final Logger log = LoggerFactory.getLogger(TutorialBot.class);

    private static class Bot extends S2Agent {
        @Override
        public void onGameStart() {
            log.info("Hello, World of Starcraft Ⅱ Bots");
        }

        @Override
        public void onStep() {
            log.info("frame count: {}", observation().getGameLoop());
        }
    }

    public static void main(String[] args) {
        Bot bot = new Bot();
        S2Coordinator coordinator = S2Coordinator.setup()
                .loadSettings(args)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.TERRAN, bot),
                        S2Coordinator.createComputer(Race.RANDOM, Difficulty.EASY)
                )
                .launchStarcraft()
                .startGame(LocalMap.of(Path.of(String.format("%s5.13/Equilibrium513AIE.SC2Map", PropertiesUtils.getVariable(PropertiesUtils.PropertyVariable.MAP)))));
        while (coordinator.update()) {
        }

        coordinator.quit();
    }
}
