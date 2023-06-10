package dev.starstruck.mixin.mixins.entity;

import dev.starstruck.Starstruck;
import dev.starstruck.listener.event.player.EventEntityCollide;
import dev.starstruck.listener.event.player.EventPushOutOfBlocks;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author aesthetical
 * @since 06/06/23
 */
@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    public void hookPushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
        EventPushOutOfBlocks event = new EventPushOutOfBlocks((Entity) (Object) this);
        if (Starstruck.getBus().dispatch(event)) info.setReturnValue(false);
    }

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void hookApplyEntityCollision(Entity entityIn, CallbackInfo info) {
        EventEntityCollide event = new EventEntityCollide((Entity) (Object) this, entityIn);
        if (Starstruck.getBus().dispatch(event)) info.cancel();
    }

//    @Inject(method = "isPushedByWater", at = @At("RETURN"), cancellable = true)
//    public void hookIsPushedByWater(CallbackInfoReturnable<Boolean> info) {
//        EventWaterPush event = new EventWaterPush((Entity) (Object) this);
//        if (PutaHack.getBus().dispatch(event)) info.setReturnValue(false);
//    }
}
