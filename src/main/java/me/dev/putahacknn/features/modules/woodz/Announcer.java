package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.Timer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Announcer extends Module {

    public Announcer() {
        super("Announcer", "no", Category.WOODZ, true, false, false);
    }

    public final Setting<Integer> delay = this.register(new Setting("Delay", 15, 0, 30));
    public Timer timer = new Timer();

    @Override
    public void onUpdate() {
        if (timer.passedS(delay.getValue())) {
            if (ThreadLocalRandom.current().nextInt(1, 21) == 1) {
                mc.player.connection.sendPacket(new CPacketChatMessage("I just got some bitches (unlike you) thanks to PutaHack.nn!"));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 2) {
                mc.player.connection.sendPacket(new CPacketChatMessage("PutaHack.nn\u306E\u304A\u304B\u3052\u3067\u3001\u79C1\u306F\uFF08\u3042\u306A\u305F\u3068\u306F\u7570\u306A\u308A\uFF09\u3044\u304F\u3064\u304B\u306E\u611A\u75F4\u3092\u624B\u306B\u5165\u308C\u307E\u3057\u305F\uFF01"));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 3) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Putahack, the client that amplifies your skill and your ego."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 4) {
                mc.player.connection.sendPacket(new CPacketChatMessage("With putahack, you'll feel like you've gained an extra inch."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 5) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Get ready to attract more attention with putahack's irresistible features."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 6) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Putahack, the secret weapon for gaining infinite pussy and victories."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 7) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Dominate the HvH scene with putahack's powerful arsenal of modules."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 8) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Experience the thrill of winning every fight with putahack by your side."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 9) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Putahack, the client that makes you the king of HvH."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 9) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Unlock your full potential and conquer the game with putahack's dominance."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 10) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Gain an edge over your opponents and leave them in awe with putahack."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 11) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Putahack: the ultimate client for leveling up your game and your confidence."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 12) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Prepare to be unstoppable with putahack's game-changing capabilities."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 13) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Putahack, the secret ingredient for success in HvH and beyond."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 14) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Experience the power of putahack and watch my victories multiply."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 15) {
                mc.player.connection.sendPacket(new CPacketChatMessage("With putahack, victory is guaranteed and defeat is a thing of the past."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 16) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Witness the power of putahack as it obliterates my opponents effortlessly."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 17) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Watch my enemies tremble in fear as putahack leads me to victory."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 18) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Unlock the full potential of your skills with putahack's unmatched capabilities."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 19) {
                mc.player.connection.sendPacket(new CPacketChatMessage("With putahack, victory is not just a possibility, it's a certainty."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 20) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Experience the thrill of complete dominance with putahack by your side."));
            } else if (ThreadLocalRandom.current().nextInt(1, 21) == 21) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Putahack, the secret weapon that ensures you'll never lose again."));
            }
            timer.reset();
        }
    }

}