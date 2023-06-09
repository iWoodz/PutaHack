package dev.putahack.module.combat;

import dev.putahack.PutaHack;
import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.player.EventUpdate;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;
import dev.putahack.util.timing.Timer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public class AutoEXP extends Module {
    private final Setting<Double> delay = new Setting<>(
            0.0, 0.01, 0.0, 5.0, "Delay");

    private final Timer timer = new Timer();

    public AutoEXP() {
        super("AutoEXP",
                "Automatically throws down exp",
                ModuleCategory.COMBAT);
    }

    @Listener
    public void onUpdate(EventUpdate event) {
        if (timer.hasPassed((long) (delay.getValue() * 1000.0), false)) {
            int slot = -1;
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
                if (!itemStack.isEmpty()
                        && itemStack.getItem().equals(Items.EXPERIENCE_BOTTLE)) {
                    slot = i;
                }
            }

            if (slot == -1) return;

            timer.resetTime();
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.player.connection.sendPacket(
                    new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            PutaHack.get().getInventory().sync();
        }
    }
}
