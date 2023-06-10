package dev.starstruck.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.starstruck.Starstruck;
import dev.starstruck.module.combat.*;
import dev.starstruck.module.exploit.AntiPopLag;
import dev.starstruck.module.movement.NoPush;
import dev.starstruck.module.movement.NoSlow;
import dev.starstruck.module.movement.Sprint;
import dev.starstruck.module.player.FastPlace;
import dev.starstruck.module.player.Notifier;
import dev.starstruck.module.player.Scaffold;
import dev.starstruck.module.render.*;
import dev.starstruck.util.io.FileUtils;
import dev.starstruck.util.timing.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author aesthetical, iWoodz, Primooctopus33
 * @since 06/04/23
 */
public class ModuleManager {

    /**
     * The module manager logger
     */
    private static final Logger logger = LogManager.getLogger(
            "Module Manager");

    private static final File configs = new File(
            FileUtils.root, "configs");

    /**
     * A map of the client classes with their instances
     */
    private final Map<Class<? extends Module>, Module> moduleClassMap = new LinkedHashMap<>();

    public ModuleManager() {

        register(
                // Combat
                new AutoArmor(),
                new AutoEXP(),
                new Criticals(),
                new KillAura(),
                new Velocity(),

                // Exploit
                new AntiPopLag(),

                // Movement
                new NoPush(),
                new NoSlow(),
                new Sprint(),

                // Player
                new FastPlace(),
                new Notifier(),
                new Scaffold(),

                // Render
                new Ambience(),
                new ClickUI(),
                new Fullbright(),
                new HUD(),
                new NoRender(),
                new UnfocusedCPU(),
                new ViewModel()
        );

        logger.info("Loaded {} modules", moduleClassMap.size());

        if (!configs.exists()) {
            boolean result = configs.mkdir();
            logger.info("Created {} directory {}",
                    configs, result ? "successfully" : "unsuccessfully");

            if (!result) return;
        }

        Starstruck.getLogger().info(load("default"));
    }

    private void register(Module... modules) {
        for (Module module : modules) {

            if (DateUtil.isAprilFools() && Math.random() > 0.5) continue;

            moduleClassMap.put(module.getClass(), module);
            module.registerAllSettings();
        }
    }

    /**
     * Saves a configuration to a file
     * @param configName the config name without .cfg
     * @return the result of the configuration saving
     */
    public String save(String configName) {

        File file = new File(configs, configName + ".cfg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Starstruck.getLogger().error("Failed to save config {}", file);
                e.printStackTrace();

                return "Failed to create config";
            }
        }

        JsonObject object = new JsonObject();
        for (Module module : get()) {
            object.add(module.getName(), module.toJson());
        }

        try {
            FileUtils.writeFile(file, FileUtils.gson.toJson(object));
        } catch (IOException e) {
            Starstruck.getLogger().error("Failed to save config {}", file);
            e.printStackTrace();

            return "Failed to save config";
        }

        return "Saved config successfully";
    }

    /**
     * Loads a configuration from a file
     * @param configName the config name without .cfg
     * @return the result of the configuration loading
     */
    public String load(String configName) {

        File file = new File(configs, configName + ".cfg");
        if (!file.exists() || !file.isFile()) {
            return "Config does not exist";
        }

        String content;
        try {
            content = FileUtils.readFile(file);
        } catch (IOException e) {
            Starstruck.getLogger().error("Failed to read config {}", file);
            e.printStackTrace();
            return "Failed to read config";
        }
        if (content.isEmpty()) return "Config does not exist";

        JsonObject object = FileUtils.jsonParser
                .parse(content).getAsJsonObject();

        for (Module module : get()) {
            if (object.has(module.getName())) {
                JsonElement element = object.get(module.getName());
                if (!element.isJsonObject()) continue;

                module.fromJson(element.getAsJsonObject());
            }
        }

        return "Loaded config successfully";
    }

    public Collection<Module> get() {
        return moduleClassMap.values();
    }

    public <T extends Module> T get(Class<T> clazz) {
        return (T) moduleClassMap.getOrDefault(clazz, null);
    }
}