package dev.starstruck;

import dev.starstruck.bind.BindManager;
import dev.starstruck.config.Config;
import dev.starstruck.config.ConfigManager;
import dev.starstruck.friend.FriendManager;
import dev.starstruck.listener.bus.EventBus;
import dev.starstruck.management.InventoryManager;
import dev.starstruck.management.PopManager;
import dev.starstruck.management.RotationManager;
import dev.starstruck.module.ModuleManager;
import dev.starstruck.util.BuildConfig;
import dev.starstruck.util.io.FileUtils;
import dev.starstruck.util.timing.DateUtil;
import dev.starstruck.util.timing.Timer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

/**
 * @author aesthetical
 * @since 06/04/23
 *
 * Entry point for starstruck.dev - previously putahack.nn
 */
@Mod(modid = "starstruck.dev", useMetadata = true)
public class Starstruck {

    /**
     * Starstruck singleton (instance)
     */
    @Instance
    private static Starstruck singleton;

    /**
     * Global client logger
     */
    private static final Logger logger = LogManager.getLogger(getName());

    /**
     * The client event bus
     */
    private static final EventBus bus = new EventBus();

    private final ConfigManager configs;
    private final BindManager binds;
    private final ModuleManager modules;
    private final FriendManager friends;
    private final RotationManager rotations;
    private final InventoryManager inventory;
    private final PopManager pops;

    public Starstruck() {
        if (singleton != null) {
            logger.info("go fuck yourself");
            FMLCommonHandler.instance().exitJava(69420, true);
        }

        singleton = this;

        // create putahack.nn save directory
        if (!FileUtils.root.exists()) {
            boolean result = FileUtils.root.mkdir();
            logger.info("Created {} directory {}",
                    FileUtils.root, result ? "successfully" : "unsuccessfully");
        }

        Display.setTitle("Loading " + getName());
        // Uncomment on release
        // ClientHook.loadRat()
        logger.info("Loading starstruck.dev by your favorite racist, special thanks to a particular oak log, all the kibbles haters and a man who hates robots, and an aesthetic retard");

        Timer timer = new Timer();

        configs = new ConfigManager();
        binds = new BindManager();
        modules = new ModuleManager();
        friends = new FriendManager();
        rotations = new RotationManager();
        inventory = new InventoryManager();
        pops = new PopManager();

        bus.subscribe(binds);
        bus.subscribe(rotations);
        bus.subscribe(inventory);
        bus.subscribe(pops);

        logger.info("Loaded starstruck.dev successfully in {}ms",
                timer.getDurationMS());
        logger.info("(you are now apart of the botnet) https://www.youtube.com/watch?v=iCjLB2-1ud4");

        logger.info("Loading configs...");
        configs.get().forEach(Config::load);
        logger.info("Loaded configs");

        Display.setTitle(getName());

    }

    /**
     * Gets the config manager instance
     * @return the config manager instance
     */
    public ConfigManager getConfigs() {
        return configs;
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
     * Gets the friend manager instance
     * @return the friend manager instance
     */
    public FriendManager getFriends() {
        return friends;
    }

    /**
     * Gets the rotation manager instance
     * @return the rotation manager instance
     */
    public RotationManager getRotations() {
        return rotations;
    }

    /**
     * Gets the inventory manager instance
     * @return the inventory manager instance
     */
    public InventoryManager getInventory() {
        return inventory;
    }

    /**
     * Gets the totem pop manager instance
     * @return the totem pop manager instance
     */
    public PopManager getPops() {
        return pops;
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
        return BuildConfig.NAME;
    }

    /**
     * Gets the client version
     * @return the client version
     */
    public static String getVersion() {
        return BuildConfig.VERSION;
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

        if (singleton == null) new Starstruck();
    }

    public static Starstruck get() {
        return singleton;
    }
}
