package me.dev.putahacknn.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.command.Command;

public class PrefixCommand
        extends Command {
    public PrefixCommand() {
        super("prefix", new String[]{"<char>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(ChatFormatting.GREEN + "Current prefix is " + PutaHacknn.commandManager.getPrefix());
            return;
        }
        PutaHacknn.commandManager.setPrefix(commands[0]);
        Command.sendMessage("Prefix changed to " + ChatFormatting.GRAY + commands[0]);
    }
}

