package dev.starstruck.mixin.mixins.render.entity;

import dev.starstruck.Starstruck;
import dev.starstruck.listener.event.EventStage;
import dev.starstruck.listener.event.render.EventFOV;
import dev.starstruck.listener.event.render.EventRenderHurt;
import dev.starstruck.listener.event.render.EventRender3D;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "renderWorldPass", at = @At("HEAD"), cancellable = true)
    public void hookRenderWorldPass$HEAD(int pass, float partialTicks, long finishTimeNano, CallbackInfo info) {
        if (Starstruck.getBus().dispatch(new EventRender3D(EventStage.PRE, partialTicks))) info.cancel();
    }

    @Inject(method = "renderWorldPass", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V",
            ordinal = 19,
            shift = Shift.AFTER))
    public void hookRenderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo info) {
        Starstruck.getBus().dispatch(new EventRender3D(EventStage.POST, partialTicks));
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hookHurtCameraEffect(float partialTicks, CallbackInfo info) {
        if (Starstruck.getBus().dispatch(new EventRenderHurt())) info.cancel();
    }

    @Inject(method = "getFOVModifier", at = @At("RETURN"), cancellable = true)
    public void hookGetFOVModifier(float partialTicks, boolean useFOVSetting, CallbackInfoReturnable<Float> info) {
        EventFOV event = new EventFOV(info.getReturnValueF(), useFOVSetting);
        if (Starstruck.getBus().dispatch(event)) info.setReturnValue(event.getValue());
    }
}