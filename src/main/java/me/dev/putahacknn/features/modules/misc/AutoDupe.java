package me.dev.putahacknn.features.modules.misc;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

public class AutoDupe extends Module {

    public AutoDupe() {
        super("AutoDupe", "dupe but auto (also fuck you woodz)", Category.MISC, true, false, false);
    }

    /**
     Credits: By Primooctopus33
     */

    public final Setting<Integer> delay = this.register(new Setting("Delay", 250, 0, 2500));
    public Timer timer = new Timer();

    @Override
    public void onUpdate() {
        for (Entity entities : mc.world.loadedEntityList) {
            if (entities instanceof EntityItemFrame) {
                if (timer.passedMs(delay.getValue())) {
                    mc.playerController.interactWithEntity(mc.player, entities, EnumHand.MAIN_HAND);
                    if (timer.passedMs(delay.getValue())) {
                        EntityItemFrame frame = (EntityItemFrame) entities;
                        if (frame.getDisplayedItem() == null || frame.getDisplayedItem().getItem() == Items.AIR) {
                            return;
                        } else {
                            mc.player.connection.sendPacket(new CPacketUseEntity(entities));
                            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                        }
                    }
                    timer.reset();
                }
            }
        }
    }

}
