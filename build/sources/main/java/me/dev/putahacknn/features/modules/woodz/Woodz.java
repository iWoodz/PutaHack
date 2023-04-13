package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.event.events.WoodzJoinEvent;
import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.features.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Woodz extends Module {

    public Woodz() {
        super("Woodz", "woodz", Category.WOODZ, true, false, false);
    }

    @SubscribeEvent
    public void onWoodzJoin(WoodzJoinEvent event) {
        Command.sendMessage("Hey Woodz! Welcome to PutaHack.nn, dont forget to punch a tree to collect wood!");
    }

}