package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.util.Util;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

public class CoordLeaker extends Module
{
        public CoordLeaker() {
            super("CoordLeaker", "leaks u coords to the world", Category.WOODZ, true, false, false);
        }

    @Override
    public void onEnable() {
        Util.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("MY COORDS ARE  " + Math.floor(Util.mc.player.posX) + ", " + Math.floor(Util.mc.player.posY) + ", " + Math.floor(Util.mc.player.posZ) + "! COME AND KILL ME LOL"));
    }
}
