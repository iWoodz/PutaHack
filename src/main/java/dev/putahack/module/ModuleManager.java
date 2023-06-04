package dev.putahack.module;

import dev.putahack.module.combat.Criticals;
import dev.putahack.module.render.ClickUI;
import dev.putahack.module.render.Fullbright;
import dev.putahack.module.render.HUD;
import dev.putahack.util.timing.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class ModuleManager {

    /**
     * The module manager logger
     */
    private static final Logger logger = LogManager.getLogger(
            "Module Manager");

    /**
     * A map of the client classes with their instances
     */
    private final Map<Class<? extends Module>, Module> moduleClassMap = new LinkedHashMap<>();

    public ModuleManager() {

        register(
                // Combat
                new Criticals(),

                // Render modules
                new ClickUI(),
                new Fullbright(),
                new HUD()
        );

        logger.info("Loaded {} modules", moduleClassMap.size());
    }

    private void register(Module... modules) {
        for (Module module : modules) {

            if (DateUtil.isAprilFools() && Math.random() > 0.5) continue;

            moduleClassMap.put(module.getClass(), module);
            module.registerAllSettings();
        }
    }

    public Collection<Module> get() {
        return moduleClassMap.values();
    }

    public <T extends Module> T get(Class<T> clazz) {
        return (T) moduleClassMap.getOrDefault(clazz, null);
    }
}
