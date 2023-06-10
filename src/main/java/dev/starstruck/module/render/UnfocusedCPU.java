package dev.starstruck.module.render;

import dev.starstruck.gui.click.ClickUIScreen;
import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.render.EventLimitFramerate;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.opengl.Display;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public class UnfocusedCPU extends Module {
    private final Setting<Integer> limit = new Setting<>(30, 0, 60, "Limit");

    public UnfocusedCPU() {
        super("UnfocusedCPU",
                "Reduces CPU and GPU load when not focused on the game window",
                ModuleCategory.RENDER);
    }

    @Listener
    public void onLimitFramerate(EventLimitFramerate event) {
        if (isUnfocused()) event.setFps(limit.getValue());
    }

//    @Listener
//    public void onRender3D(EventRender3D event) {
//        if (event.getStage() == EventStage.PRE && isUnfocused()) event.cancel();
//    }

    private boolean isUnfocused() {
        return (!mc.inGameHasFocus || !Display.isActive())
                && !(mc.currentScreen instanceof GuiChat)
                && !(mc.currentScreen instanceof ClickUIScreen);
    }
}
