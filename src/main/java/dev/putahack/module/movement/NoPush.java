package dev.putahack.module.movement;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.player.EventEntityCollide;
import dev.putahack.listener.event.player.EventPushOutOfBlocks;
import dev.putahack.listener.event.player.EventWaterPush;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;

/**
 * @author aesthetical
 * @since 06/06/23
 */
public class NoPush extends Module {
    private final Setting<Boolean> blocks = new Setting<>(true, "Blocks");
    private final Setting<Boolean> water = new Setting<>(true, "Water");
    private final Setting<Boolean> entities = new Setting<>(true, "Entities");

    public NoPush() {
        super("NoPush",
                "because futures for some reason doesnt work half the time",
                ModuleCategory.MOVEMENT);
    }

    @Listener
    public void onPushOutOfBlocks(EventPushOutOfBlocks event) {
        if (blocks.getValue()
                && event.getEntity().equals(mc.player)) event.cancel();
    }

    @Listener
    public void onWaterPush(EventWaterPush event) {
        if (water.getValue()
                && event.getEntity().equals(mc.player)) event.cancel();
    }

    @Listener
    public void onEntityCollide(EventEntityCollide event) {
        if (entities.getValue()
                && event.getEntity().equals(mc.player)) event.cancel();
    }
}
