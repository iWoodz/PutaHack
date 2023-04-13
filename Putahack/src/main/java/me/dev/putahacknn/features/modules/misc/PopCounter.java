package me.dev.putahacknn.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentScore;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;

public class PopCounter
        extends Module {
    public static HashMap<String, Integer> TotemPopContainer = new HashMap();
    private static PopCounter INSTANCE = new PopCounter();

    public final Setting<Boolean> black = this.register(new Setting("obese", false));

    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static PopCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopCounter();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        TotemPopContainer.clear();
        if (black.getValue()) {
            sendMessage("ur obese, ur pc is bricked, every vc you join from now on will be recorded, expect a pipe bomb in your mailbox within 3 - 5 business days", 38249732);
        }
    }

    public void onDeath(EntityPlayer player) {
        if (TotemPopContainer.containsKey(player.getName())) {
            int l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                sendMessage(black.getValue() ? ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.WHITE + " just died after popping " + ChatFormatting.BLUE + l_Count + ChatFormatting.WHITE + "totem." : ChatFormatting.RESET + "man this dude " + ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.WHITE + " actually just died after popping " + ChatFormatting.BLUE + l_Count + ChatFormatting.WHITE + " totem wtf, no way this nigga totem failing in 2023 LOL! did he pay for that?", player.getName().hashCode());
            } else {
                sendMessage(black.getValue() ? ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.WHITE + " just died after popping " + ChatFormatting.BLUE + l_Count + ChatFormatting.WHITE + "totems." : ChatFormatting.RESET + "LMFAOO no way this fuckin dude " + ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.WHITE + " died after popping " + ChatFormatting.BLUE + l_Count + ChatFormatting.WHITE + " totems bruh, how do you use that many totems and still die, this is actually sad.", player.getName().hashCode());
            }
        }
    }

    public void onTotemPop(EntityPlayer player) {
        if (PopCounter.fullNullCheck()) {
            return;
        }
        if (PopCounter.mc.player.equals(player)) {
            return;
        }
        int l_Count = 1;
        if (TotemPopContainer.containsKey(player.getName())) {
            l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.put(player.getName(), ++l_Count);
        } else {
            TotemPopContainer.put(player.getName(), l_Count);
        }
        if (l_Count == 1) {
            sendMessage(black.getValue() ? ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.WHITE + " just popped " + ChatFormatting.BLUE + l_Count + ChatFormatting.WHITE + "totem." : ChatFormatting.RESET + "naw bruh dis nigga " + ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.WHITE + " really just popped " + ChatFormatting.BLUE + l_Count + ChatFormatting.WHITE + " totem? LOL no fuckin way what a loser.", player.getName().hashCode());
        } else {
            sendMessage(black.getValue() ? ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.WHITE + " just popped " + ChatFormatting.BLUE + l_Count + ChatFormatting.WHITE + "totems." : ChatFormatting.RESET + "LOL BRO WTF DID " + ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.WHITE + " JUST POP " + ChatFormatting.BLUE + l_Count + ChatFormatting.WHITE + " totems? LMAOOOO bro how bad can you be", player.getName().hashCode());
        }
    }

    public void sendMessage(String message, int id) {
        mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(message), id);
    }

    public static int getPopCount(EntityPlayer player) {
        if (TotemPopContainer.containsKey(player.getName())) {
            return TotemPopContainer.get(player.getName());
        }
        return 0;
    }
}