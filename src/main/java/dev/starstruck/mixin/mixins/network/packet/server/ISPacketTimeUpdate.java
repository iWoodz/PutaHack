package dev.starstruck.mixin.mixins.network.packet.server;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author aesthetical
 * @since 06/10/23
 */
@Mixin(SPacketTimeUpdate.class)
public interface ISPacketTimeUpdate {

    @Accessor("worldTime")
    void setWorldTime(long worldTime);
}
