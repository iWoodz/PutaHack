package me.dev.putahacknn.features.modules.misc;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.InventoryUtil;
import me.dev.putahacknn.util.Timer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class ElytraSwap extends Module {

    public ElytraSwap() {
        super("ElytraSwap", "swap but elytra", Category.MISC, true, false, false);
    }

    /**
    Credits: By Primooctopus33
     */

    public final Setting<Integer> moveDelay = this.register(new Setting("Move Delay", 10, 0, 100));
    public Timer timer = new Timer();

    @Override
    public void onEnable() {
        if (mc.player.inventoryContainer.getSlot(6).getStack().getItem() == Items.ELYTRA) {
            int chestSlot = InventoryUtil.findItemInventorySlot(Items.DIAMOND_CHESTPLATE, false, true);
            if (chestSlot != -1) {
                if (timer.passedMs(moveDelay.getValue())) {
                    moveItem(chestSlot, 6);
                    timer.reset();
                }
            }
            this.disable();
        } else if (mc.player.inventoryContainer.getSlot(6).getStack().getItem() != Items.ELYTRA) {
            int elytraSlot = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, true);
            if (elytraSlot != -1) {
                if (timer.passedMs(moveDelay.getValue())) {
                    moveItem(elytraSlot, 6);
                    timer.reset();
                }
            }
            this.disable();
        }
    }

    public static void moveItem(int from, int to) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, to, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, mc.player);
    }

}
