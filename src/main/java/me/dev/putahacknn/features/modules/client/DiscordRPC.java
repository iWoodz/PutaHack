package me.dev.putahacknn.features.modules.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.modules.Module;
import net.minecraft.client.gui.GuiMainMenu;

import java.util.Random;


public class DiscordRPC extends Module {

    private final club.minnced.discord.rpc.DiscordRPC rpc = club.minnced.discord.rpc.DiscordRPC.INSTANCE;

    private final DiscordRichPresence presence = new DiscordRichPresence();

    private Thread thread;

    private final String[] state = {
            "winning",
            "pooping on ur family",
            "putahook owns u",
            "on a 56 killstreak",
            "in ur wall",
            "flying over thousands of blocks with the power of putahack",
            "bitchware.fatcunt",
            "gaming",
            "with the fellas",
            "putahack",
            "Canada Ontario, Toronto, Tycos Dr, 2811",
            "owned by austin dillard",
            "186.295.40.2"
    };

    public DiscordRPC() {
        super("DiscordRPC", "yurrrrr", Category.CLIENT , true, false, false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        stop();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (isOn()) {
            start();
        }
    }

    private void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        rpc.Discord_Initialize("1069828695373643787", handlers, true, "");

        presence.startTimestamp = System.currentTimeMillis() / 1000L;

        presence.details = "PutaHack " + PutaHacknn.MODVER;

        presence.state = state[new Random().nextInt(state.length)];

        presence.largeImageKey = "big";
        presence.largeImageText = "PutaHack " + PutaHacknn.MODVER;

        presence.largeImageKey = ((mc.currentScreen instanceof GuiMainMenu ? "idling" :
                (mc.currentServerData != null ? "multiplayer" : "singleplayer")));

        presence.largeImageText = ((mc.currentScreen instanceof GuiMainMenu ? "Idling." :
                (mc.currentServerData != null ? "Playing multiplayer." : "Playing singleplayer.")));

        rpc.Discord_UpdatePresence(presence);

        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();

                presence.details = "PutaHack " + PutaHacknn.MODVER;

                presence.state = state[new Random().nextInt(state.length)];

                presence.largeImageKey = ((mc.currentScreen instanceof GuiMainMenu ? "iding" :
                        (mc.currentServerData != null ? "multiplayer" : "singleplayer")));

                presence.largeImageText = ((mc.currentScreen instanceof GuiMainMenu ? "Iding." :
                        (mc.currentServerData != null ? "Playing multiplayer." : "Playing singleplayer.")));

                rpc.Discord_UpdatePresence(presence);

                try {
                    Thread.sleep(2000L);

                } catch (InterruptedException ignored) {

                }
            }
        }, "DiscordRPC-Callback-Handler");

        thread.start();
    }

    private void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }

        rpc.Discord_Shutdown();
    }
}