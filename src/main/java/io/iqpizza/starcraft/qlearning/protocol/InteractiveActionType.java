package io.iqpizza.starcraft.qlearning.protocol;

import com.github.ocraft.s2client.protocol.data.Units;

/**
 * 상호작용될 매개체와 탄생될 타입을 정의
 */
public interface InteractiveActionType extends ActionType {

    /**
     * 해당 유닛을 생산하게 되는 주체
     * @return 생산 주체
     */
    Units getParent();

    /**
     * 탄생되게 될 유닛
     */
    Units getChild();
}
