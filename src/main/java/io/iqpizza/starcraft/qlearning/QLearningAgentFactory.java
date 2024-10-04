package io.iqpizza.starcraft.qlearning;

import io.iqpizza.starcraft.qlearning.qtable.QTable;

public class QLearningAgentFactory {

    private QLearningAgentFactory() {
        throw new IllegalArgumentException("Factory Class");
    }

    public static QLearningAgent createDefaultAgent() {
        return new QLearningAgent();
    }

    public static QLearningAgent createCustomAgent(double alpha, double gamma) {
        return new QLearningAgent(new QTable(), alpha, gamma, QLearningAgent.DEFAULT_EPSILON,
                QLearningAgent.DEFAULT_EPSILON_DECAY, QLearningAgent.DEFAULT_MIN_EPSILON);
    }

    public static QLearningAgent createCustomAgent(QTable qTable, double alpha, double gamma) {
        return new QLearningAgent(qTable, alpha, gamma, QLearningAgent.DEFAULT_EPSILON,
                QLearningAgent.DEFAULT_EPSILON_DECAY, QLearningAgent.DEFAULT_MIN_EPSILON);
    }

    public static QLearningAgent createCustomAgent(QTable qTable, double alpha, double gamma, double epsilon) {
        return new QLearningAgent(qTable, alpha, gamma, epsilon,
                QLearningAgent.DEFAULT_EPSILON_DECAY, QLearningAgent.DEFAULT_MIN_EPSILON);
    }

    public static QLearningAgent createExplorationFocusedAgent(double epsilon, double epsilonDecay) {
        return new QLearningAgent(new QTable(), QLearningAgent.DEFAULT_ALPHA, QLearningAgent.DEFAULT_GAMMA,
                epsilon, epsilonDecay, QLearningAgent.DEFAULT_MIN_EPSILON);
    }

    public static QLearningAgent createExplorationFocusedAgent(QTable qTable, double epsilon, double epsilonDecay) {
        return new QLearningAgent(qTable, QLearningAgent.DEFAULT_ALPHA, QLearningAgent.DEFAULT_GAMMA,
                epsilon, epsilonDecay, QLearningAgent.DEFAULT_MIN_EPSILON);
    }
}
