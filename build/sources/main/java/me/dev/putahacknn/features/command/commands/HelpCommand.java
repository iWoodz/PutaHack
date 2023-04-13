package me.dev.putahacknn.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.PutaHacknn;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : PutaHacknn.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + PutaHacknn.commandManager.getPrefix() + command.getName());
        }
    }
}

