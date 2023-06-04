package me.dev.putahacknn.features.modules.client;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.Timer;
import net.minecraft.network.play.client.CPacketChatMessage;

public class AutoBait extends Module {
    public AutoBait() {
        super("AutoBait", "baits rando nn dogs", Category.CLIENT, true, false, false);
    }

    public final Setting<Integer> delay = this.register(new Setting<>("Seconds Delay", 7, 1, 20));
    public final Setting<Boolean> packetMessage = this.register(new Setting<>("Packet Message", false));
    public Timer timer = new Timer();
    public int messageCount = 1;

    @Override
    public void onUpdate() {
        if (messageCount == 1 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("XDDDDDDDDD you suck lelll"));
            } else {
                mc.player.sendChatMessage("XDDDDDDDDD you suck lelll");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 2 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Lol 1x1 or dodge lel"));
            } else {
                mc.player.sendChatMessage("Lol 1x1 or dodge lel");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 3 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("i am the bestest ever lolz"));
            } else {
                mc.player.sendChatMessage("i am the bestest ever lolz");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 4 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("Go compare funds lelelel"));
            } else {
                mc.player.sendChatMessage("Go compare funds lelelel");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 5 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("hush mode hahahaha"));
            } else {
                mc.player.sendChatMessage("hush mode hahahaha");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 6 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("ur ab to get swatted roflsauce"));
            } else {
                mc.player.sendChatMessage("ur ab to get swatted roflsauce");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 7 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("compare roblox friends"));
            } else {
                mc.player.sendChatMessage("hang urself incel");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 8 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("freak lelelel"));
            } else {
                mc.player.sendChatMessage("freak lelelel");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 9 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("lolololol compare"));
            } else {
                mc.player.sendChatMessage("lolololol compare");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 10 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("ez mad shitbox lelz"));
            } else {
                mc.player.sendChatMessage("ez mad shitbox lelz");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 11 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("1v1 me or swat lelsauce"));
            } else {
                mc.player.sendChatMessage("1v1 me or swat lelsauce");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 12 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("sit dog"));
            } else {
                mc.player.sendChatMessage("sit dog");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 13 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("lel randroid"));
            } else {
                mc.player.sendChatMessage("lel randroid");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 14 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("lol sit ur so bad haha"));
            } else {
                mc.player.sendChatMessage("lol sit ur so bad haha");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 15 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("im so known haha"));
            } else {
                mc.player.sendChatMessage("im so known haha");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 16 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("im the king hahahah"));
            } else {
                mc.player.sendChatMessage("im the king hahahah");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 17 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("lolol how are you so bad"));
            } else {
                mc.player.sendChatMessage("lolol how are you so bad");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 18 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("you suck kek"));
            } else {
                mc.player.sendChatMessage("you suck kek");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 19 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("sit lelsauce"));
            } else {
                mc.player.sendChatMessage("sit lelsauce");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 20 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("stupid 2023 randroid lololol"));
            } else {
                mc.player.sendChatMessage("stupid 2023 randroid lololol");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 21 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("hahahahah this guy is so bad"));
            } else {
                mc.player.sendChatMessage("hahahahah this guy is so bad");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 22 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("you suck so much omd"));
            } else {
                mc.player.sendChatMessage("you suck so much omd");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 23 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("i win lol haha xd"));
            } else {
                mc.player.sendChatMessage("i win lol haha xd");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 24 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("define bait lolololol"));
            } else {
                mc.player.sendChatMessage("define bait lolololol");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 25 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("hahahaha i win i win i win"));
            } else {
                mc.player.sendChatMessage("hahahaha i win i win i win");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 26 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("ur so bad rofl"));
            } else {
                mc.player.sendChatMessage("ur so bad rofl");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 27 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("want pvp lessons"));
            } else {
                mc.player.sendChatMessage("want pvp lessons");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 28 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("hahahahahahahahaha"));
            } else {
                mc.player.sendChatMessage("hahahahahahahahaha");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 29 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("cope XD"));
            } else {
                mc.player.sendChatMessage("cope XD");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 30 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("haha sit shitter"));
            } else {
                mc.player.sendChatMessage("haha sit shitter");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 31 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("opp down lol!"));
            } else {
                mc.player.sendChatMessage("opp down haha");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 32 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("cry lel"));
            } else {
                mc.player.sendChatMessage("cry lel");
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 33 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("n1 pooron"));
                messageCount = 0;
            } else {
                mc.player.sendChatMessage("n1 pooron");
                messageCount = 0;
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 34 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("hahahahah i am the king of this server"));
                messageCount = 0;
            } else {
                mc.player.sendChatMessage("hahahahah i am the king of this server");
                messageCount = 0;
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 35 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("go compare hahhahaha lel"));
                messageCount = 0;
            } else {
                mc.player.sendChatMessage("go compare hahhahaha lel");
                messageCount = 0;
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 36 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("go X: 0. Y: 199. Z: 0 or dodge nn"));
                messageCount = 0;
            } else {
                mc.player.sendChatMessage("go X: 0. Y: 199. Z: 0 or dodge nn");
                messageCount = 0;
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 37 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("go X: 0. Y: 199. Z: 0 or dodge nn"));
                messageCount = 0;
            } else {
                mc.player.sendChatMessage("go X: 0. Y: 199. Z: 0 or dodge nn");
                messageCount = 0;
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 38 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("you're so poor lol n1"));
                messageCount = 0;
            } else {
                mc.player.sendChatMessage("you're so poor lol n1");
                messageCount = 0;
            }
            messageCount++;
            timer.reset();
            return;
        }
        if (messageCount == 39 && timer.passedS(delay.getValue())) {
            if (packetMessage.getValue()) {
                mc.player.connection.sendPacket(new CPacketChatMessage("who are you lel ur unknown"));
                messageCount = 0;
            } else {
                mc.player.sendChatMessage("who are you lel ur unknown");
                messageCount = 0;
            }
            messageCount++;
            timer.reset();
            return;

        }
    }
}