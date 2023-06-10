package dev.starstruck.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class ConfigManager {

    /**
     * A list of configs
     */
    private final List<Config> configList = new ArrayList<>();

    public ConfigManager() {
        Runtime.getRuntime().addShutdownHook(
                new ConfigShutdownThread(this));
    }

    /**
     * Adds a configuration
     * @param config the config object
     */
    public void add(Config config) {
        configList.add(config);
    }

    /**
     * Gets all the config objects
     * @return a list of config objects
     */
    public List<Config> get() {
        return configList;
    }
}
