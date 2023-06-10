package dev.starstruck.module.render;

import dev.starstruck.gui.click.ClickUIScreen;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;

import static org.lwjgl.input.Keyboard.KEY_O;

/**
 * @author aesthetical, iWoodz
 * @since 06/04/23
 */
public class ClickUI extends Module {

    private ClickUIScreen clickUiScreen;

    public ClickUI() {
        super("ClickUI",
                "Displays a configuration screen for the clients' modules",
                ModuleCategory.RENDER);

        getBind().setKey(KEY_O);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (mc.player == null || mc.world == null) {
            setState(false);
            return;
        }

        if (clickUiScreen == null)
            clickUiScreen = new ClickUIScreen();

        mc.displayGuiScreen(clickUiScreen);
    }
}
