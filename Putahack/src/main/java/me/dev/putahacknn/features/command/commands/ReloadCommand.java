package me.dev.putahacknn.features.command.commands;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.command.Command;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        PutaHacknn.reload();
    }
}

