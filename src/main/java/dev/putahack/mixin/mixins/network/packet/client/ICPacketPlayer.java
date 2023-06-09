package dev.putahack.mixin.mixins.network.packet.client;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author aesthetical
 * @since 06/05/23
 */
@Mixin(CPacketPlayer.class)
public interface ICPacketPlayer {

    @Accessor("rotating")
    boolean isRotating();
}
