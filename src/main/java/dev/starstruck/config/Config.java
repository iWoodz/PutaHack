package dev.starstruck.config;

import java.io.File;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public abstract class Config {
    private final File file;

    public Config(File file) {
        this.file = file;
    }

    /**
     * Saves this configuration to a file
     */
    public abstract void save();

    /**
     * Loads this configuration from a file
     */
    public abstract void load();

    public File getFile() {
        return file;
    }
}
