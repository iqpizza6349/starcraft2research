package io.iqpizza.starcraft.qlearning.qtable;

import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.SerializeActiveType;
import io.iqpizza.starcraft.qlearning.protocol.State;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class QTable {
    private static final double DEFAULT_QVALUE = 0.0;

    /**
     * 이 이중 Map은 2차원 테이블을 표현하였습니다.
     * [State][Action] = Q 값
     */
    private final Map<State, Map<ActionType, Double>> table;

    public QTable() {
        this.table = new HashMap<>();
    }

    public QTable(Map<State, Map<ActionType, Double>> table) {
        this.table = table;
    }

    /**
     * 상태와 행동에 대한 key 를 사용하고 있는 Q 값을 반환합니다.
     * @param state 현재 상태
     * @param actionType 취한 행동
     * @return 기존에 해당 상태에 취했던 행동에 대한 Q 값을 반환합니다.
     */
    public double getQValue(State state, ActionType actionType) {
        if (actionType == null) {
            return DEFAULT_QVALUE;
        }

        ensureStateExists(state);
        return table.get(state).getOrDefault(actionType, DEFAULT_QVALUE);
    }

    /**
     * 상태와 행동에 대한 Q 값을 테이블에 기록합니다.
     * @param state 상태
     * @param actionType 취한 행동
     * @param value 테이블에 저장될 Q값
     */
    public void setQValue(State state, ActionType actionType, double value) {
        ensureStateExists(state);
        table.get(state).put(actionType, value);
    }

    /**
     * 특정 상태에서 가장 높은 Q 값을 가진 행동을 반환합니다.
     * @param state 현재 상태
     * @return 기존 해당 상태에서 수행했던 행위 중 Q 값이 높았던 행위를 반환합니다.
     */
    public Optional<ActionType> getBestAction(State state) {
        ensureStateExists(state);
        Map<ActionType, Double> actions = table.get(state);

        ActionType bestAction = null;
        double maxQ = Double.NEGATIVE_INFINITY;

        for (Map.Entry<ActionType, Double> entry : actions.entrySet()) {
            if (entry.getValue() > maxQ) {
                maxQ = entry.getValue();
                bestAction = entry.getKey();
            }
        }

        return Optional.ofNullable(bestAction);
    }

    // 상태가 존재하지 않으면 생성
    private void ensureStateExists(State state) {
        table.computeIfAbsent(state, k -> new HashMap<>());
    }

    public Map<State, Map<ActionType, Double>> getTable() {
        Map<State, Map<ActionType, Double>> copy = new HashMap<>();

        // 원본 맵의 각 엔트리를 깊은 복사
        for (Map.Entry<State, Map<ActionType, Double>> entry : table.entrySet()) {
            State stateCopy = new State(entry.getKey());  // State가 복사 생성자를 가진다고 가정
            Map<ActionType, Double> actionMapCopy = new HashMap<>();

            // ActionType, Double 값도 복사
            for (Map.Entry<ActionType, Double> actionEntry : entry.getValue().entrySet()) {
                ActionType actionTypeCopy = new SerializeActiveType(actionEntry.getKey().getAbility()); // ActionType 복사 생성자
                actionMapCopy.put(actionTypeCopy, actionEntry.getValue());
            }

            copy.put(stateCopy, actionMapCopy);
        }

        return copy;
    }
}
