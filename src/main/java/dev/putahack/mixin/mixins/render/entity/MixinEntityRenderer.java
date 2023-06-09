package dev.putahack.mixin.mixins.render.entity;

import dev.putahack.PutaHack;
import dev.putahack.listener.event.render.EventFOV;
import dev.putahack.listener.event.render.EventRenderHurt;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hookHurtCameraEffect(float partialTicks, CallbackInfo info) {
        if (PutaHack.getBus().dispatch(new EventRenderHurt())) info.cancel();
    }

    @Inject(method = "getFOVModifier", at = @At("RETURN"), cancellable = true)
    public void hookGetFOVModifier(float partialTicks, boolean useFOVSetting, CallbackInfoReturnable<Float> info) {
        EventFOV event = new EventFOV(info.getReturnValueF(), useFOVSetting);
        if (PutaHack.getBus().dispatch(event)) info.setReturnValue(event.getValue());
    }
}
