package me.dev.putahacknn.features.modules.misc;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class LifeSaver extends Module {

    public LifeSaver() {
        super("LifeSaver", "something you can- cant* trust, gonna save you more times than dbear can anyway", Category.MISC, true, false, false);
    }

    public final Setting<Integer> slot = this.register(new Setting("Slot", 1, 2, 9));
    public final Setting<Boolean> pauseNotInTotSlot = this.register(new Setting("Pause Not In Tot Slot", true));

    @Override
    public void onUpdate() {
        if (mc.player.getHeldItemMainhand().getItem() == Items.TOTEM_OF_UNDYING || pauseNotInTotSlot.getValue() && mc.player.inventory.currentItem != (slot.getValue() - 1) || InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING, false, true) == -1) {
            return;
        }
        int totemSlot = InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING, false, true);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemSlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, getTranslatedSlotId(), 0, ClickType.PICKUP, mc.player);
    }

    public int getTranslatedSlotId() {
        if (slot.getValue() == 1) {
            return 36;
        } else if (slot.getValue() == 2) {
            return 37;
        } else if (slot.getValue() == 3) {
            return 38;
        } else if (slot.getValue() == 4) {
            return 39;
        } else if (slot.getValue() == 5) {
            return 40;
        } else if (slot.getValue() == 6) {
            return 41;
        } else if (slot.getValue() == 7) {
            return 42;
        } else if (slot.getValue() == 8) {
            return 43;
        } else if (slot.getValue() == 9) {
            return 44;
        }
        return -1;
    }

}
