package dev.starstruck.mixin.mixins.render.gui;

import dev.starstruck.Starstruck;
import dev.starstruck.listener.event.render.EventRenderToast;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.toasts.GuiToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(GuiToast.class)
public class MixinGuiToast {

    @Inject(method = "drawToast", at = @At("HEAD"), cancellable = true)
    public void hookDrawToast(ScaledResolution resolution, CallbackInfo info) {
        if (Starstruck.getBus().dispatch(new EventRenderToast())) info.cancel();
    }
}
