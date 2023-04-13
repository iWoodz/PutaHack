package me.dev.putahacknn.features.command.commands;

import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.PutaHacknn;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        PutaHacknn.unload(true);
    }
}

