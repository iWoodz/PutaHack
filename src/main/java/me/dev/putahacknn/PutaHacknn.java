package me.dev.putahacknn;

import me.dev.putahacknn.manager.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = "putahack.nn", name = "PutaHack.nn", version = "0.1.7")
public class PutaHacknn {
    public static final String MODID = "putahack.nn";
    public static final String MODNAME = "PutaHack.nn";
    public static final String MODVER = "0.1.7";
    public static final Logger LOGGER = LogManager.getLogger("PutaHack.nn");
    public static TimerManager timerManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    private static boolean unloaded;

    static {
        unloaded = false;
    }

    public static void init() {
        Display.setTitle("PutaHack.nn v0.1.7");
        PutaHacknn.load();
    }

    public static void load() {
        LOGGER.info("\n\nLoading PutaHack.nn by your favorite racist, special thanks to a particular oak log, all the kibbles haters and a man who hates robots");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        timerManager = new TimerManager();
        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        serverManager = new ServerManager();
        fileManager = new FileManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info("PutaHack.nn successfully loaded!\n");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading PutaHack.nn by your favorite racist, special thanks to a particular oak log, all the kibbles haters and a man who hates robots");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        PutaHacknn.onUnload();
        timerManager = null;
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        LOGGER.info("PutaHack.nn unloaded!\n");
    }

    public static void reload() {
        PutaHacknn.unload(false);
        PutaHacknn.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(PutaHacknn.configManager.config.replaceFirst("putahacknn/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }
}

