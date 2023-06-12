package dev.starstruck.module.movement;

import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.player.EventWalkingUpdate;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;
import dev.starstruck.util.player.MoveUtils;

/**
 * @author aesthetical
 * @since 06/11/23
 */
public class InstantSpeed extends Module {
    private final Setting<Mode> mode = new Setting<>(Mode.STRAFE, "Mode");
    private final Setting<Boolean> zeroOut = new Setting<>(true, "ZeroOut");
    private final Setting<Boolean> onGround = new Setting<>(true, "OnGround");
    private final Setting<Double> reduction = new Setting<>(
            0.09, 0.01, 0.0, 1.0, "Reduction");

    public InstantSpeed() {
        super("InstantSpeed",
                "Makes you go faster instantly",
                ModuleCategory.MOVEMENT);
    }

    @Listener
    public void onWalkingUpdate(EventWalkingUpdate event) {
        if (!MoveUtils.isMoving() && zeroOut.getValue()) {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
            return;
        }

        if (onGround.getValue() && !mc.player.onGround) return;

        double speed = mode.getValue() == Mode.STRAFE
                ? MoveUtils.getSpeed()
                : MoveUtils.getBaseNCPSpeed() - reduction.getValue();

        if (MoveUtils.isMoving()) MoveUtils.strafe(speed);
    }

    public enum Mode {
        STRAFE, INSTANT
    }
}
