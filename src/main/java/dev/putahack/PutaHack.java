package dev.putahack;

import dev.putahack.bind.BindManager;
import dev.putahack.listener.bus.EventBus;
import dev.putahack.module.ModuleManager;
import dev.putahack.util.time.DateUtil;
import dev.putahack.util.time.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

/**
 * @author aesthetical
 * @since 06/04/23
 *
 * Entry point for putahack.nn
 */
public class PutaHack {

    /**
     * PutaHack singleton (instance)
     */
    private static PutaHack singleton;

    /**
     * The name of the client
     * this line of code is protected by the federal government and if you rename this, C02 will be found inside your air conditioning unit without you knowing
     * your C02 sensors will be turned off as a result as well
     */
    private static final String name = "putahack.nn";

    /**
     * The version of the client
     */
    private static final String version = "2.0.0";

    /**
     * Global client logger
     */
    private static final Logger logger = LogManager.getLogger(name);

    /**
     * The client event bus
     */
    private static final EventBus bus = new EventBus();

    private final BindManager binds;
    private final ModuleManager modules;

    private PutaHack() {
        singleton = this;

        Display.setTitle("Loading " + name);
        // Uncomment on release
        // ClientHook.loadRat()
        logger.info("Loading PutaHack.nn by your favorite racist, special thanks to a particular oak log, all the kibbles haters and a man who hates robots, and an aesthetic retard");

        Timer timer = new Timer();

        binds = new BindManager();
        modules = new ModuleManager();

        bus.subscribe(binds);

        logger.info("Loaded PutaHack.nn successfully in {}ms",
                timer.getDurationMS());
        logger.info("(you are now apart of the botnet) https://www.youtube.com/watch?v=iCjLB2-1ud4");
        Display.setTitle(name);

    }

    /**
     * Gets the bind manager instance
     * @return the bind manager instance
     */
    public BindManager getBinds() {
        return binds;
    }

    /**
     * Gets the module manager instance
     * @return the module manager instance
     */
    public ModuleManager getModules() {
        return modules;
    }

    /**
     * Gets the client event bus
     * @return the client event bus instance
     */
    public static EventBus getBus() {
        return bus;
    }

    /**
     * Gets the client global logger
     * @return the client's global logger
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Gets the client name
     * @return the client name
     */
    public static String getName() {
        return name;
    }

    /**
     * Gets the client version
     * @return the client version
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Builds PutaHack
     */
    public static void build() {
        if (DateUtil.isChristmas()) {
            for (int i = 0; i < 1000; ++i) {
                logger.info("sir its christmas we aint toby harnack you aint getting no future update go outside and spend time with ur family fuckass get off anarchy holy shit get help");
            }
            return;
        }

        if (singleton == null) new PutaHack();
    }

    public static PutaHack get() {
        return singleton;
    }
}
