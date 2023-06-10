package dev.starstruck.command;

import dev.starstruck.Starstruck;
import dev.starstruck.config.Config;
import dev.starstruck.util.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class CommandPrefixConfig extends Config {
    private final CommandManager commands;

    public CommandPrefixConfig(CommandManager commands) {
        super(new File(FileUtils.root, "command_prefix.txt"));
        this.commands = commands;
    }

    @Override
    public void save() {
        try {
            FileUtils.writeFile(getFile(), commands.getCommandPrefix());
        } catch (IOException e) {
            Starstruck.getLogger().error("Failed to save to {}", getFile());
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        if (!getFile().exists()) {
            try {
                boolean result = getFile().createNewFile();
                Starstruck.getLogger().info("Created {} file {}",
                        getFile(), result ? "successfully" : "unsuccessfully");
            } catch (IOException e) {
                Starstruck.getLogger().error("Failed to create {}", getFile());
                e.printStackTrace();
            }

            return;
        }

        String content;
        try {
            content = FileUtils.readFile(getFile());
        } catch (IOException e) {
            Starstruck.getLogger().error("Failed to read from {}", getFile());
            e.printStackTrace();
            return;
        }
        if (content.isEmpty()) return;

        commands.setCommandPrefix(content
                .trim()
                .replaceAll("\\s+", ""));
    }
}
