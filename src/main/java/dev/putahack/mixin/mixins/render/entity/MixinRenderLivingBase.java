package dev.putahack.mixin.mixins.render.entity;

import dev.putahack.PutaHack;
import dev.putahack.mixin.duck.IEntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase {
    private float rotationYaw, rotationPitch;
    private float prevRotationYaw, prevRotationPitch;

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At("HEAD"))
    public void hookDoRender$HEAD(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (entity.equals(Minecraft.getMinecraft().player)) {
            if (!PutaHack.get().getRotations().isSpoofingRotations()) return;

            rotationYaw = entity.rotationYawHead;
            rotationPitch = entity.rotationPitch;
            prevRotationYaw = entity.prevRotationYawHead;
            prevRotationPitch = entity.prevRotationPitch;

            IEntityLivingBase living = (IEntityLivingBase) entity;

            entity.rotationYawHead = living.getRenderYaw();
            entity.rotationPitch = living.getRenderPitch();
            entity.prevRotationYawHead = living.getPrevRenderYaw();
            entity.prevRotationPitch = living.getPrevRenderPitch();
        }
    }

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At("RETURN"))
    public void hookDoRender$TAIL(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (entity.equals(Minecraft.getMinecraft().player)) {

            if (!PutaHack.get().getRotations().isSpoofingRotations()) return;

            entity.rotationYawHead = rotationYaw;
            entity.rotationPitch = rotationPitch;
            entity.prevRotationYawHead = prevRotationYaw;
            entity.prevRotationPitch = prevRotationPitch;
        }
    }
}
