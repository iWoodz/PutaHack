package dev.putahack.mixin.mixins.render.gui;

import dev.putahack.PutaHack;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Redirect(method = "renderHotbar", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/InventoryPlayer;currentItem:I"))
    public int hookRenderHotbar$currentItem(InventoryPlayer inventoryPlayer) {
        int serverSlot = PutaHack.get().getInventory().getServerSlot();
        return serverSlot == -1 ? inventoryPlayer.currentItem : serverSlot;
    }
}
