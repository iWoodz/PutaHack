package dev.starstruck.mixin.mixins.network.packet.server;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author aesthetical
 * @since 06/10/23
 */
@Mixin(SPacketPlayerPosLook.class)
public interface ISPacketPlayerPosLook {

    @Accessor("yaw")
    void setYaw(float yaw);

    @Accessor("pitch")
    void setPitch(float pitch);
}
