package dev.putahack.module.movement;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.EventStage;
import dev.putahack.listener.event.player.EventItemSlowdown;
import dev.putahack.listener.event.player.EventWalkingUpdate;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;
import net.minecraft.network.play.client.CPacketHeldItemChange;

/**
 * @author aesthetical
 * @since 06/06/23
 */
public class NoSlow extends Module {
    private final Setting<Mode> mode = new Setting<>(Mode.VANILLA, "Mode");

    public NoSlow() {
        super("NoSlow",
                "Stops items that you are using from slowing you down",
                ModuleCategory.MOVEMENT);
    }

    @Listener
    public void onItemSlowdown(EventItemSlowdown event) {
        event.getInput().moveForward *= 5.0f;
        event.getInput().moveStrafe *= 5.0f;

        if (mode.getValue() == Mode.NEW_NCP) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(
                    mc.player.inventory.currentItem));
        }
    }

    public enum Mode {
        VANILLA, NEW_NCP
    }
}
