package dev.putahack.mixin.mixins.world;

import dev.putahack.PutaHack;
import dev.putahack.listener.event.player.EventWaterPush;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author aesthetical
 * @since 06/06/23
 */
@Mixin(World.class)
public class MixinWorld {

    @Redirect(method = "handleMaterialAcceleration",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByWater()Z"))
    public boolean hookHandleMaterialAcceleration$isPushedByWater(Entity entityIn) {
        return !PutaHack.getBus().dispatch(
                new EventWaterPush(entityIn)) && entityIn.isPushedByWater();
    }
}
