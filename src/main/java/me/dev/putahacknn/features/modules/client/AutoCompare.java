package me.dev.putahacknn.features.modules.client;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.Timer;
import net.minecraft.network.play.client.CPacketChatMessage;

public class AutoCompare extends Module {
//compare funds pooron
    public AutoCompare() {
        super("AutoCompare", "Automatically says compare _____ in chat", Category.CLIENT, true, false, false);
    }

    public final Setting<Integer> delay = this.register(new Setting<>("Seconds Delay", 7, 1, 20));
    public final Setting<Boolean> packetMessage = this.register(new Setting<>("Packet Message", false));
    public Timer timer = new Timer();
    public int messageCount = 1;

    @Override
    public void onUpdate() {
        if (messageCount == 1 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare bitcoin"));
            } else {
                mc.player.sendChatMessage("compare bitcoin");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 2 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare rise uid"));
            } else {
                mc.player.sendChatMessage("compare rise uid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 3 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare moon uid"));
            } else {
                mc.player.sendChatMessage("compare moon uid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 4 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare tenacity uid"));
            } else {
                mc.player.sendChatMessage("compare tenacity uid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 5 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare nnpaste.fuckyou uid"));
            } else {
                mc.player.sendChatMessage("compare nnpaste.fuckyou uid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 6 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare robux"));
            } else {
                mc.player.sendChatMessage("compare robux");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 7 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare roblox friends"));
            } else {
                mc.player.sendChatMessage("compare roblox friends");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 8 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare discord friends"));
            } else {
                mc.player.sendChatMessage("compare discord friends");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 9 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare phantom forces kdr"));
            } else {
                mc.player.sendChatMessage("compare phantom forces kdr");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 10 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare phantom forces credits"));
            } else {
                mc.player.sendChatMessage("compare phantom forces credits");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 11 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare octohack uid"));
            } else {
                mc.player.sendChatMessage("compare octohack uid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 12 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare randroidware uid"));
            } else {
                mc.player.sendChatMessage("compare randroidware uid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 13 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare zero day uid"));
            } else {
                mc.player.sendChatMessage("compare zeroday uid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 14 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare minecoins"));
            } else {
                mc.player.sendChatMessage("compare minecoins");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 15 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare dogecoin"));
            } else {
                mc.player.sendChatMessage("compare dogecoin");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 16 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare putahack.nn uid"));
            } else {
                mc.player.sendChatMessage("compare putahack.nn uid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 17 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare kills on cc"));
            } else {
                mc.player.sendChatMessage("compare kills on cc");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 18 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare vbucks"));
            } else {
                mc.player.sendChatMessage("compare vbucks");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 19 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare valorant kills"));
            } else {
                mc.player.sendChatMessage("compare valorant kills");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 20 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare fortnite wins"));
            } else {
                mc.player.sendChatMessage("compare fortnite wins");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 21 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare bloxburg money"));
            } else {
                mc.player.sendChatMessage("compare bloxburg money");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 22 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare jailbreak money"));
            } else {
                mc.player.sendChatMessage("compare jailbreak money");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 23 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare ratio"));
            } else {
                mc.player.sendChatMessage("compare ratio");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 24 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare funds"));
            } else {
                mc.player.sendChatMessage("compare funds");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 25 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare future uid"));
            } else {
                mc.player.sendChatMessage("compare future uid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 26 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare jd"));
            } else {
                mc.player.sendChatMessage("compare jd");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 27 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare wins against nelson (the king)"));
            } else {
                mc.player.sendChatMessage("compare wins against nelson (the king)");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 28 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare iq test scores"));
            } else {
                mc.player.sendChatMessage("compare iq test scores");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 29 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare cubecraft kills"));
            } else {
                mc.player.sendChatMessage("compare cubecraft kills");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 30 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare zonecraft wins"));
            } else {
                mc.player.sendChatMessage("compare zonecraft wins");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 31 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare hypixel wins"));
            } else {
                mc.player.sendChatMessage("compare hypixel wins");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 32 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare skywars coins"));
            } else {
                mc.player.sendChatMessage("compare skywars coins");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 33 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare 3rd worlders packed"));
                messageCount = 0;
            } else {
                mc.player.sendChatMessage("compare 3rd worlders packed");
                messageCount = 0;
            }
            messageCount++;
            timer.reset();
            return;
        }
    }

}
