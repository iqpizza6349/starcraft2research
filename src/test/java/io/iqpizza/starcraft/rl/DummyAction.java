package io.iqpizza.starcraft.rl;

import com.github.ocraft.s2client.protocol.data.Abilities;
import io.iqpizza.starcraft.qlearning.protocol.ActionType;
import io.iqpizza.starcraft.qlearning.protocol.State;

public class DummyAction implements ActionType {
    private final String action;
    private final boolean executable;

    public DummyAction(String action, boolean executable) {
        this.action = action;
        this.executable = executable;
    }

    public DummyAction(String action) {
        this(action, false);
    }

    public DummyAction() {
        this("dummyAction", false);
    }

    @Override
    public Abilities getAbility() {
        return Abilities.INVALID;
    }

    public String getAction() {
        return action;
    }

    @Override
    public boolean isExecutable(State currentState) {
        return executable;
    }
}
