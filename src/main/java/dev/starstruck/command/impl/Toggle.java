package dev.starstruck.command.impl;

import dev.starstruck.Starstruck;
import dev.starstruck.command.Command;
import dev.starstruck.command.exceptions.InvalidSyntaxException;
import dev.starstruck.module.Module;
import net.minecraft.util.text.TextFormatting;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class Toggle extends Command {
    public Toggle() {
        super(new String[]{"toggle", "t"},
                "Toggles a module on or off",
                "[module]");
    }

    @Override
    public String dispatch(String[] args) throws Exception {
        if (args.length == 0) throw new InvalidSyntaxException();

        Module module = null;
        for (Module m : Starstruck.get().getModules().get()) {
            if (m.getName().equalsIgnoreCase(args[0])) {
                module = m;
                break;
            }
        }

        if (module == null) return "Could not resolve module";

        module.setState(!module.isToggled());

        return module.getName() + " is now "
                + (module.isToggled()
                    ? (TextFormatting.GREEN + "enabled")
                    : (TextFormatting.RED + "disabled"));
    }
}
