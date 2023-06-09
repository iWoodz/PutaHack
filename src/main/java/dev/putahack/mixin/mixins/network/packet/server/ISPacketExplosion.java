package dev.putahack.mixin.mixins.network.packet.server;

import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author aesthetical
 * @since 06/05/23
 */
@Mixin(SPacketExplosion.class)
public interface ISPacketExplosion {

    @Accessor("motionX")
    void setMotionX(float motionX);

    @Accessor("motionY")
    void setMotionY(float motionY);

    @Accessor("motionZ")
    void setMotionZ(float motionZ);
}
