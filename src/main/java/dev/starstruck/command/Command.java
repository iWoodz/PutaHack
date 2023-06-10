package dev.starstruck.command;

import dev.starstruck.util.trait.Nameable;
import dev.starstruck.util.trait.Printable;
import net.minecraft.client.Minecraft;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public abstract class Command implements Nameable, Printable {

    /**
     * The minecraft game instance
     */
    protected static final Minecraft mc = Minecraft.getMinecraft();

    private final String[] aliases;
    private final String description, syntax;

    public Command(String[] aliases, String description) {
        this(aliases, description, null);
    }

    public Command(String[] aliases, String description, String syntax) {
        this.aliases = aliases;
        this.description = description;
        this.syntax = syntax;
    }

    /**
     * Dispatches this command
     * @param args the arguments passed in
     * @return the result or null
     */
    public abstract String dispatch(String[] args) throws Exception;

    /**
     * Gets suggestions for this argument
     * @param id the index of the argument
     * @param argument the current value of the argument
     * @return a list of suggestions
     */
    public String[] suggest(int id, String argument) {
        return new String[0];
    }

    @Override
    public String getName() {
        return aliases[0];
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getSyntax() {
        return syntax;
    }

    public String syntax() {
        return "Invalid syntax! Valid syntax: "
                + String.join("/", aliases)
                + " " + getSyntax();
    }
}
