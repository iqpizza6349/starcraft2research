package io.iqpizza.starcraft.qlearning.utils;

import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.State;
import io.iqpizza.starcraft.qlearning.qtable.QTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QTableUtils {

    private QTableUtils() {
        throw new IllegalAccessError("Utility class");
    }

    // 여러 QTable을 하나로 병합하는 메서드
    public static QTable mergeQTables(List<QTable> qTables) {
        Map<State, Map<ActionType, Double>> mergedData = new HashMap<>();
        Map<State, Map<ActionType, Integer>> actionCounts = new HashMap<>();

        // 각 QTable을 순회하며 데이터를 병합
        for (QTable qTable : qTables) {
            for (Map.Entry<State, Map<ActionType, Double>> stateEntry : qTable.getTable().entrySet()) {
                State state = stateEntry.getKey();
                for (Map.Entry<ActionType, Double> actionEntry : stateEntry.getValue().entrySet()) {
                    ActionType action = actionEntry.getKey();
                    double qValue = actionEntry.getValue();

                    // 병합된 QTable에 해당 상태/행동이 있는지 확인
                    mergedData.putIfAbsent(state, new HashMap<>());
                    actionCounts.putIfAbsent(state, new HashMap<>());

                    // 기존에 있는 Q-값과 병합 (여기서는 평균을 사용)
                    mergedData.get(state).merge(action, qValue, Double::sum);  // Q-값 누적
                    actionCounts.get(state).merge(action, 1, Integer::sum);    // Q-값 카운트 누적
                }
            }
        }

        // Q-값의 평균을 계산
        for (Map.Entry<State, Map<ActionType, Double>> stateEntry : mergedData.entrySet()) {
            State state = stateEntry.getKey();
            for (Map.Entry<ActionType, Double> actionEntry : stateEntry.getValue().entrySet()) {
                ActionType action = actionEntry.getKey();
                double sumQValue = actionEntry.getValue();
                int count = actionCounts.get(state).get(action);
                double averageQValue = sumQValue / count;
                mergedData.get(state).put(action, averageQValue);
            }
        }

        // 병합된 데이터를 새로운 QTable 객체로 반환
        return new QTable(mergedData);
    }
}
