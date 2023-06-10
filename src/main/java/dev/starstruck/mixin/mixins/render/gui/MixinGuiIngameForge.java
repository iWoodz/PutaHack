package dev.starstruck.mixin.mixins.render.gui;

import dev.starstruck.Starstruck;
import dev.starstruck.listener.event.render.EventRender2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author aesthetical
 * @since 06/04/23
 */
@Mixin(GuiIngameForge.class)
public class MixinGuiIngameForge {

    @Shadow private ScaledResolution res;

    @Inject(method = "renderGameOverlay", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/client/GuiIngameForge;renderPlayerList(II)V",
            shift = Shift.AFTER))
    public void hookRenderGameOverlay(float partialTicks, CallbackInfo info) {
        Starstruck.getBus().dispatch(new EventRender2D(res, partialTicks));
    }
}
