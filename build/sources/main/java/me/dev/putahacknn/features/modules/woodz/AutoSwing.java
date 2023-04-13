package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.EnumHand;

public class AutoSwing extends Module {

    public AutoSwing() {
        super("AutoSwing", "makes woodz mad", Category.WOODZ, true, false, false);
    }

    public final Setting<Mode> mode = this.register(new Setting("Mode", Mode.Offhand));

    public enum Mode {
        Offhand,
        Mainhand
    }

    @Override
    public void onUpdate() {
        EnumHand hand = mode.getValue() == Mode.Offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        mc.player.swingArm(hand);
    }

}
