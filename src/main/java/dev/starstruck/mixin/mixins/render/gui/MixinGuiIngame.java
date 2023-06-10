package dev.starstruck.mixin.mixins.render.gui;

import dev.starstruck.Starstruck;
import dev.starstruck.listener.event.render.EventRenderPortal;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderPortal", at = @At("HEAD"), cancellable = true)
    public void hookRenderPortal(float timeInPortal, ScaledResolution scaledRes, CallbackInfo info) {
        if (Starstruck.getBus().dispatch(new EventRenderPortal())) info.cancel();
    }

    @Redirect(method = "renderHotbar", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/InventoryPlayer;currentItem:I"))
    public int hookRenderHotbar$currentItem(InventoryPlayer inventoryPlayer) {
        int serverSlot = Starstruck.get().getInventory().getServerSlot();
        return serverSlot == -1 ? inventoryPlayer.currentItem : serverSlot;
    }
}
