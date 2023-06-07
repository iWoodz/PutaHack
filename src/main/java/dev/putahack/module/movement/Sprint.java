package dev.putahack.module.movement;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.player.EventUpdate;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;

/**
 * @author aesthetical
 * @since 06/06/23
 */
public class Sprint extends Module {
    private final Setting<Mode> mode = new Setting<>(Mode.LEGIT, "Mode");

    public Sprint() {
        super("Sprint", "Automatically sprints for you (ur fat)", ModuleCategory.MOVEMENT);
    }

    @Listener
    public void onUpdate(EventUpdate event) {
        // woah there - calm down partner
        if (mc.player.isSprinting()) return;

        // vanilla checks and shit this is pretty basic shit
        mc.player.setSprinting(mode.getValue() == Mode.RAGE
                || (!mc.player.isSneaking()
                    && !mc.player.isHandActive()
                    && !mc.player.collidedHorizontally
                    && mc.player.getFoodStats().getFoodLevel() > 6
                    && mc.player.movementInput.moveForward > 0.0f));
    }

    public enum Mode {
        LEGIT, RAGE
    }
}
