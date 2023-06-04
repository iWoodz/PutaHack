package dev.putahack.mixin.network;

import dev.putahack.PutaHack;
import dev.putahack.listener.event.network.EventPacket.In;
import dev.putahack.listener.event.network.EventPacket.Out;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author aesthetical
 * @since 06/04/23
 */
@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/NetworkManager;flushOutboundQueue()V",
            shift = Shift.BEFORE), cancellable = true)
    public void hookSendPacket(Packet<?> packet, CallbackInfo info) {
        if (PutaHack.getBus().dispatch(new Out(packet))) info.cancel();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V",
                    shift = Shift.BEFORE), cancellable = true)
    public void hookChannelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo info) {
        if (PutaHack.getBus().dispatch(new In(packet))) info.cancel();
    }
}
