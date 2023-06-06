package dev.putahack.module.render;

import dev.putahack.gui.click.ClickUIScreen;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;

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
                ModuleCategory.CLIENT);

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
