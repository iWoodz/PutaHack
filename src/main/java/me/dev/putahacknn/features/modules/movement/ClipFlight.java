package me.dev.putahacknn.features.modules.movement;

import me.dev.putahacknn.event.events.MoveEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.EntityUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClipFlight extends Module {

    public ClipFlight() {
        super("ClipFlight", "flight, but clip", Category.MOVEMENT, true, false, false);
    }

    public final Setting<Double> horizontalSpeed = this.register(new Setting("Horizontal Speed", 30.0D, 0.0D, 100.0D));
    public final Setting<Integer> verticalSpeed = this.register(new Setting("Vertical Speed", 30, 0, 100));
    public final Setting<Integer> packets = this.register(new Setting("Packets", 50, 0, 80));
    public final Setting<Boolean> floatInAir = this.register(new Setting("Float In Air", true));
    public final Setting<Boolean> onGround = this.register(new Setting("On Ground", true));

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        double[] dirSpeed = EntityUtil.forward(horizontalSpeed.getValue());
        if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
            for (int i = 0; i < packets.getValue(); i++) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + verticalSpeed.getValue(), mc.player.posZ, onGround.getValue()));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.05, mc.player.posZ, true));
            }
        } else if (mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            for (int i = 0; i < packets.getValue(); i++) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - verticalSpeed.getValue(), mc.player.posZ, onGround.getValue()));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.05, mc.player.posZ, true));
            }
        }
        for (int i = 0; i < packets.getValue(); i++) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + dirSpeed[0], mc.player.posY, mc.player.posZ + dirSpeed[0], onGround.getValue()));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.05, mc.player.posZ, true));
        }
        if (floatInAir.getValue()) {
            mc.player.motionY = 0;
        }
    }

}
