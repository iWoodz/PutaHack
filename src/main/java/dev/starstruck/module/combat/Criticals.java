package dev.starstruck.module.combat;

import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.network.EventPacket;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;
import dev.starstruck.util.timing.Timer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class Criticals extends Module {
    private final Setting<Mode> mode = new Setting<>(Mode.PACKET, "Mode");
    private final Setting<Double> delay = new Setting<>(0.5, 0.01, 0.0, 5.0, "Delay");

    private final Timer timer = new Timer();

    public Criticals() {
        super("Criticals", "Makes your attacks critical hits", ModuleCategory.COMBAT);
    }

    @Listener
    public void onPacketOut(EventPacket.Out event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = event.getPacket();
            if (packet.getAction() != Action.ATTACK
                    || !(packet.getEntityFromWorld(mc.world) instanceof EntityLivingBase)
                    || !timer.hasPassed((long) (delay.getValue() * 1000.0), false)) return;

            if (!mc.player.onGround
                    || mc.player.isInWater()
                    || mc.player.isInLava()
                    || mc.player.isOnLadder()
                    || mc.player.isPotionActive(MobEffects.BLINDNESS)
                    || mc.player.isRiding()) return;

            timer.resetTime();
            switch (mode.getValue()) {
                case PACKET:
                    mc.player.connection.sendPacket(new Position(
                            mc.player.posX, mc.player.posY + 0.11, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new Position(
                            mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    break;

                case NEW_NCP:
                    // TODO: find offsets
                    break;

                case MOTION:
                    mc.player.motionY = 0.11;
                    break;
            }
        }
    }

    public enum Mode {
        PACKET, NEW_NCP, MOTION
    }
}
