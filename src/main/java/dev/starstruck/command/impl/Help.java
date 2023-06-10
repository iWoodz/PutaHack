package dev.starstruck.command.impl;

import dev.starstruck.Starstruck;
import dev.starstruck.command.Command;

import java.util.List;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class Help extends Command {
    public Help() {
        super(new String[]{"help", "h", "?"},
                "Displays information on commands",
                "<command name>");
    }

    @Override
    public String dispatch(String[] args) {
        if (args.length == 0) {

            List<Command> commands = Starstruck.get()
                    .getCommands().get();

            for (Command command : commands) {
                print(String.join("/", command.getAliases())
                        + " - " + command.getDescription());
            }

            return commands.size() + " commands available";
        }

        Command command = Starstruck.get().getCommands().get(args[0]);
        if (command == null) return "Invalid command name";

        return String.join("/", command.getAliases())
                + " " + command.getSyntax();
    }
}
