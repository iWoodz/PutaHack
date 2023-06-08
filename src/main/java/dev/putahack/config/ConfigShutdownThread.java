package dev.putahack.config;

import dev.putahack.PutaHack;
import dev.putahack.util.timing.Timer;

import java.util.List;

/**
 * @author aesthetical
 * @since 06/07/23
 *
 * what a great name for this class
 */
public class ConfigShutdownThread extends Thread {

    private final ConfigManager configs;

    public ConfigShutdownThread(ConfigManager configs) {
        this.configs = configs;
    }

    @Override
    public void run() {
        List<Config> cfgs = configs.get();
        System.out.println("Saving " + cfgs.size() + " configs");
        for (Config config : cfgs) {
            Timer timer = new Timer();

            try {
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }

            long passedMS = timer.getDurationMS();
            System.out.println("Saved " + config.getFile() + " in " + passedMS + "ms");
        }

        System.out.println(PutaHack.get().getModules()
                .save("default"));
    }
}
