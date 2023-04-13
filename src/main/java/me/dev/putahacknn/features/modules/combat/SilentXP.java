package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.event.events.KeyEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Bind;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SilentXP extends Module {

    public SilentXP() {
        super("SilentXP", "Dont forget to punch a tree to get wood!", Category.COMBAT, true, false, false);
    }
    // now this part is very unnecessary but i like to do it so, cope and seethe
    public enum Sensitivity {
        High,
        Low
    }

    public final Setting<Sensitivity> sensitivity = this.register(new Setting("Sensitivity", Sensitivity.High));
    public final Setting<Bind> xpBind = this.register(new Setting("XP Bind", new Bind(-1)));
    // makes the setting show up in the gui and registers it ^

    @Override
    public void onUpdate() {
        // xp bind is a key on your keyboard, .isDown just means if the physical key is down and the computer recognizes it
        if (fullNullCheck()) {
            return;
            // return is basically pause/stop the script
            // okay
        }
        int xpSlot = findXPInHotbar();
        int oldSlot = mc.player.inventory.currentItem; // keeps track of the slot from before we started holding the bind
        if (xpBind.getValue().isDown()) {
            if (xpSlot != -1) {
                if (sensitivity.getValue() == Sensitivity.High) {
                    mc.player.inventory.currentItem = xpSlot; // this kind of switching is better for strict, using packets is silent but this can also be made silent if you update the player controller after you switch back to the old slot
                } else if (sensitivity.getValue() == Sensitivity.Low) { // this code is absolutely unnecessary because there is only one other option for the sensitivity setting but cope and seethe
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(xpSlot));
                }
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                if (sensitivity.getValue() == Sensitivity.High) {
                    mc.player.inventory.currentItem = oldSlot;
                    mc.playerController.updateController(); // this is required if you want to silent switch and use this kind of switching at the same time
                } else if (sensitivity.getValue() == Sensitivity.Low) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
                }
            }
        }
    }

    public int findXPInHotbar() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == Items.EXPERIENCE_BOTTLE) {
                return i;
            }
        }
        return -1;
    }

}
