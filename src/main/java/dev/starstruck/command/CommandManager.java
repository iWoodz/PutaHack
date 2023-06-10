package dev.starstruck.command;

import dev.starstruck.Starstruck;
import dev.starstruck.command.exceptions.InvalidSyntaxException;
import dev.starstruck.command.impl.Drawn;
import dev.starstruck.command.impl.Help;
import dev.starstruck.command.impl.Toggle;
import dev.starstruck.command.impl.Watermark;
import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.network.EventPacket;
import dev.starstruck.util.trait.Printable;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class CommandManager implements Printable {

    /**
     * The default dispatch result if a command does not return one
     */
    private static final String DEFAULT_DISPATCH_RESULT = "Command successfully dispatched";

    /**
     * The message ID for all executed commands
     */
    private static final int MESSAGE_ID = 694201337;

    /**
     * A map of commands and their aliases
     */
    private final Map<String, Command> commandAliasMap = new LinkedHashMap<>();

    /**
     * A list of all of the command instances
     */
    private final List<Command> commandList = new LinkedList<>();

    /**
     * The command prefix
     */
    private String commandPrefix = "-";

    public CommandManager() {
        Starstruck.get().getConfigs().add(
                new CommandPrefixConfig(this));

        register(new Drawn(), new Help(), new Toggle(), new Watermark());
    }

    private void register(Command... commands) {
        for (Command command : commands) {
            commandList.add(command);
            for (String alias : command.getAliases()) {
                commandAliasMap.put(alias, command);
            }
        }
    }

    @Listener
    public void onPacketOut(EventPacket.Out event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = event.getPacket();
            if (!packet.getMessage().startsWith(commandPrefix)) return;

            event.cancel();

            String[] args = packet.getMessage()
                    .substring(commandPrefix.length())
                    .trim()
                    .split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            if (args.length == 0) {
                print(MESSAGE_ID, "No command provided. Lost? Use \"" + commandPrefix + "help\"");
                return;
            }

            Command command = get(args[0].toLowerCase());
            if (command == null) {
                print(MESSAGE_ID, "Invalid command provided. Lost? Use \"" + commandPrefix + "help\"");
                return;
            }

            try {
                args = Arrays.copyOfRange(args, 1, args.length);
                String dispatched = command.dispatch(args);
                if (dispatched == null
                        || dispatched.isEmpty()) dispatched = DEFAULT_DISPATCH_RESULT;

                print(command.hashCode(), dispatched);
            } catch (InvalidSyntaxException e) {
                print(command.hashCode(), command.syntax());
            } catch (Exception e) {
                Starstruck.getLogger().error("Failed to execute command {}", command);
                e.printStackTrace();
                print(MESSAGE_ID, TextFormatting.RED + "Failed to execute command");
            }
        }
    }

    public List<Command> get() {
        return commandList;
    }

    public <T extends Command> T get(String alias) {
        return (T) commandAliasMap.getOrDefault(alias.toLowerCase(), null);
    }

    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }
}
