package dev.putahack.mixin.mixins.entity;

import dev.putahack.PutaHack;
import dev.putahack.listener.event.render.EventArmAnimationSpeed;
import dev.putahack.mixin.duck.IEntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author aesthetical
 * @since 06/05/23
 */
@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity implements IEntityLivingBase {

    private float renderYaw, renderPitch;
    private float prevRenderYaw, prevRenderPitch;

    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void hookConstructor(World world, CallbackInfo info) {
        renderYaw = rotationYaw;
        renderPitch = rotationPitch;
        prevRenderYaw = renderYaw;
        prevRenderPitch = renderPitch;
    }

    @Inject(method = "onEntityUpdate", at = @At("TAIL"))
    public void hookOnEntityUpdate(CallbackInfo info) {
        prevRenderYaw = renderYaw;
        prevRenderPitch = renderPitch;
    }

    @Inject(method = "getArmSwingAnimationEnd", at = @At("RETURN"), cancellable = true)
    public void hookGetArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
        EventArmAnimationSpeed event = new EventArmAnimationSpeed(info.getReturnValue());
        PutaHack.getBus().dispatch(event);
        if (event.isCanceled()) info.setReturnValue(event.getSpeed());
    }

    @Override
    public float getRenderYaw() {
        return renderYaw;
    }

    @Override
    public float getRenderPitch() {
        return renderPitch;
    }

    @Override
    public float getPrevRenderYaw() {
        return prevRenderYaw;
    }

    @Override
    public float getPrevRenderPitch() {
        return prevRenderPitch;
    }

    @Override
    public void setRenderYaw(float yaw) {
        renderYaw = yaw;
    }

    @Override
    public void setRenderPitch(float pitch) {
        renderPitch = pitch;
    }
}
