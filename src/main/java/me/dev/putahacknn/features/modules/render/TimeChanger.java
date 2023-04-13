package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.event.events.GameLoopEvent;
import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TimeChanger extends Module {

    public TimeChanger() {
        super("TimeChanger", "Changes the time of your world", Category.RENDER, true, false, false);
    }

    public final Setting<Integer> time = this.register(new Setting("Time", 300, 0, 2000));
    public final Setting<Boolean> cancelTimeSync = this.register(new Setting("Cancel Time Sync", true));

    @SubscribeEvent
    public void onGameLoop(GameLoopEvent event) {
        if (fullNullCheck()) {
            return;
        }
        mc.world.setWorldTime(time.getValue());
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate && cancelTimeSync.getValue()) {
            event.setCanceled(true);
        }
    }

}
