package dev.putahack.mixin.mixins.entity;

import dev.putahack.PutaHack;
import dev.putahack.listener.event.render.EventArmAnimationSpeed;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author aesthetical
 * @since 06/05/23
 */
@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase {

    @Inject(method = "getArmSwingAnimationEnd", at = @At("RETURN"), cancellable = true)
    public void hookGetArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
        EventArmAnimationSpeed event = new EventArmAnimationSpeed(info.getReturnValue());
        PutaHack.getBus().dispatch(event);
        if (event.isCanceled()) info.setReturnValue(event.getSpeed());
    }
}
