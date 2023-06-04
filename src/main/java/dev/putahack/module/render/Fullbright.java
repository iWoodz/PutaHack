package dev.putahack.module.render;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.player.EventUpdate;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class Fullbright extends Module {
    private float oldGamma = 1.0f;

    public Fullbright() {
        super("Fullbright", "and god said \"let there be light!\" but ur mom was in the way", ModuleCategory.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        oldGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = oldGamma;
        oldGamma = 1.0f;
    }

    @Listener
    public void onUpdate(EventUpdate event) {
        mc.gameSettings.gammaSetting = 100.0f;
    }
}
