package dev.putahack.mixin.entity;

import dev.putahack.PutaHack;
import dev.putahack.listener.event.player.EventUpdate;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author aesthetical
 * @since 06/04/23
 */
@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "onUpdate", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/AbstractClientPlayer;onUpdate()V",
            shift = Shift.AFTER))
    public void hookOnUpdate(CallbackInfo info) {
        PutaHack.getBus().dispatch(new EventUpdate());
    }
}
