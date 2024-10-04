package io.iqpizza.starcraft.qlearning;

import io.iqpizza.starcraft.qlearning.protocol.*;
import io.iqpizza.starcraft.qlearning.qtable.QTable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class QLearningAgent {
    public static final double DEFAULT_ALPHA = 0.1;    // 학습률
    public static final double DEFAULT_GAMMA = 0.9;    // 할인율
    public static final double DEFAULT_EPSILON = 1.0;  // 탐험율
    public static final double DEFAULT_EPSILON_DECAY = 0.995;  // 탐험률 감소용
    public static final double DEFAULT_MIN_EPSILON = 0.995;    // 최소 탐험률

    private final List<ActionType> allActions;

    /**
     * 학습한 결과를 저징하는 테이블
     */
    @Getter
    private final QTable qtable;

    /**
     * 학습률
     */
    @Getter
    private final double alpha;

    /**
     * 할인율
     */
    @Getter
    private final double gamma;

    /**
     * 탐험률
     */
    @Getter @Setter
    private double epsilon;

    /**
     * 탐험률 감소율
     */
    @Getter
    private final double epsilonDecay;

    /**
     * 최소 탐험률
     */
    @Getter
    private final double minEpsilon;

    private final Random random;

    public QLearningAgent() {
        this(new QTable(), DEFAULT_ALPHA, DEFAULT_GAMMA, DEFAULT_EPSILON, DEFAULT_EPSILON_DECAY, DEFAULT_MIN_EPSILON);
    }

    public QLearningAgent(QTable qtable) {
        this(qtable, DEFAULT_ALPHA, DEFAULT_GAMMA, DEFAULT_EPSILON, DEFAULT_EPSILON_DECAY, DEFAULT_MIN_EPSILON);
    }

    public QLearningAgent(QTable qtable, double alpha, double gamma, double epsilon, double epsilonDecay, double minEpsilon) {
        this.qtable = qtable;
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.epsilonDecay = epsilonDecay;
        this.minEpsilon = minEpsilon;
        this.allActions = new ArrayList<>();

        allActions.addAll(Arrays.asList(AttackActionType.values()));
        allActions.addAll(Arrays.asList(ConstructActionType.values()));
        allActions.addAll(Arrays.asList(TrainActionType.values()));
//        allActions.addAll(Arrays.asList(SkillActionType.values()));   // 스킬은 나중에 추가하도록 한다.

        this.random = new Random();
    }

    /**
     * ε-greedy policy 을 기반으로 행동 선택 (탐험 vs Q 테이블 활용)
     * @param state 현재 상태
     * @return 행해질 행동
     */
    public ActionType chooseAction(State state) {
        if (Math.random() < epsilon) {
            return getRandomAction(state);
        }

        return qtable.getBestAction(state).orElse(getRandomAction(state));
    }

    public void updateQTable(State currentState, ActionType action, int reward, State nextState) {
        // 다음 상태에서 최적의 행동 찾기
        ActionType bestNextAction = qtable.getBestAction(nextState).orElse(null);

        // Q 값 업데이트
        double newQValue = qtable.getQValue(currentState, action)
                + alpha * ((reward + gamma * qtable.getQValue(nextState, bestNextAction))
                - qtable.getQValue(currentState, action));

        qtable.setQValue(currentState, action, newQValue);
    }

    // 가능한 행동 목록에서 무작위로 선택 (탐험을 위해 사용)
    private ActionType getRandomAction(State currentState) {
        List<ActionType> executableActions = new ArrayList<>();

        for (ActionType actionType : allActions) {
            if (actionType.isExecutable(currentState)) {
                executableActions.add(actionType);
            }
        }

        // 수행 가능한 행동이 없으면 null 반횐
        if (executableActions.isEmpty()) {
            return null;
        }
        // 수행 가능한 행동 중 무작위로 하나 선택
        return executableActions.get(random.nextInt(executableActions.size()));
    }

    /**
     * 탐험률을 점진적으로 감소한다.
     */
    public void gradualDecreaseExplorationRate() {
        this.epsilon = Math.min(minEpsilon, epsilon * epsilonDecay);
    }
}
