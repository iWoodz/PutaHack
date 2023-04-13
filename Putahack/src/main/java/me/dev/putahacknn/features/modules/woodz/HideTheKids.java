package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.network.play.client.CPacketPlayer;

public class HideTheKids extends Module {

    public HideTheKids() {
        super("HideTheKids", "clones your average emp member", Category.WOODZ, true, false, false);
    }

    public final Setting<Boolean> autoDisable = this.register(new Setting("AutoDisable", true));
    public final Setting<Integer> bypassAmount = this.register(new Setting("Bypass Amount", -1, -10, 10));

    @Override
    public void onEnable() {
        mc.player.jump();
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.01, mc.player.posZ, mc.player.onGround));
        for (int i = 0; i < 2; i++) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + bypassAmount.getValue(), mc.player.posZ, mc.player.onGround));
        }
        if (autoDisable.getValue()) {
            this.disable();
        }
    }

}
