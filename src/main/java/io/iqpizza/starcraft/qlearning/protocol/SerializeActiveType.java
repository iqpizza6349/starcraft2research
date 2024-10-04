package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.Abilities;

import java.io.Serial;
import java.io.Serializable;

/**
 * 이 클래스는 CSV 파일에 직렬화/역직렬화를 위한 Wrapper 클래스에 불가합니다.
 * 실제 사용에는 사용하지 말아주세요.
 */
public class SerializeActiveType implements ActionType, Serializable {
    @Serial
    private static final long serialVersionUID = -7705207038139104794L;

    private final Abilities abilities;

    public SerializeActiveType(Abilities abilities) {
        this.abilities = abilities;
    }

    @Override
    public Abilities getAbility() {
        return abilities;
    }
}
