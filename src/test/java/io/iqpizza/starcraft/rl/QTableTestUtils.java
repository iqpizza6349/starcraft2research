package io.iqpizza.starcraft.rl;

import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.State;

import java.util.HashMap;
import java.util.Map;

public class QTableTestUtils {

    public static Map<State, Map<ActionType, Double>> stub(State state, ActionType actionType, double defaultValue) {
        Map<State, Map<ActionType, Double>> map = new HashMap<>();
        map.put(state, new HashMap<>());
        map.get(state).put(actionType, defaultValue);
        return map;
    }

}
