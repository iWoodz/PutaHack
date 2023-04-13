package me.dev.putahacknn.features.modules.player;

import me.dev.putahacknn.features.modules.Module;
import net.minecraft.network.play.client.*;

public class AutoSelfCrash extends Module {

    public AutoSelfCrash() {
        super("AutoSelfCrash", "listen to the bad baiters", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        mc.player.connection.sendPacket(new CPacketChatMessage());
        mc.player.connection.sendPacket(new CPacketHeldItemChange());
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock());
        mc.player.connection.sendPacket(new CPacketCustomPayload());
        mc.player.connection.sendPacket(new CPacketUseEntity());
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem());
        mc.player.connection.sendPacket(new CPacketPlayer());
        mc.player.connection.sendPacket(new CPacketEnchantItem());
        mc.player.connection.sendPacket(new CPacketPlayerDigging());
        this.disable();
    }

    @Override
    public void onLogout() {
        this.disable();
    }

}
