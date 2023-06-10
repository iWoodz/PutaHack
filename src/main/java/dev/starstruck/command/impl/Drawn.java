package dev.starstruck.command.impl;

import dev.starstruck.Starstruck;
import dev.starstruck.command.Command;
import dev.starstruck.command.exceptions.InvalidSyntaxException;
import dev.starstruck.module.Module;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class Drawn extends Command {
    public Drawn() {
        super(new String[]{"drawn", "d", "hide"},
                "Sets a module to be drawn to the arraylist",
                "[module]");
    }

    @Override
    public String dispatch(String[] args) throws InvalidSyntaxException {

        if (args.length == 0) throw new InvalidSyntaxException();

        Module module = null;
        for (Module m : Starstruck.get().getModules().get()) {
            if (m.getName().equalsIgnoreCase(args[0])) {
                module = m;
                break;
            }
        }

        if (module == null) return "Could not resolve module";

        module.setDrawn(!module.isDrawn());

        return module.getName()
                + " is set to be "
                + (module.isDrawn() ? "" : "not ")
                + "drawn on the arraylist.";
    }
}
