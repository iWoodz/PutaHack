package dev.putahack.module.render;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.render.EventFOV;
import dev.putahack.listener.event.render.EventRenderHurt;
import dev.putahack.listener.event.render.EventRenderPortal;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public class NoRender extends Module {
    private final Setting<Boolean> hurt = new Setting<>(true, "Hurt");
    private final Setting<Boolean> portal = new Setting<>(true, "Portal");
    private final Setting<Boolean> dynamicFoV = new Setting<>(true, "DynamicFoV");

    public NoRender() {
        super("NoRender",
                "Stops retarded things from rendering",
                ModuleCategory.RENDER);
    }

    @Listener
    public void onRenderHurt(EventRenderHurt event) {
        if (hurt.getValue()) event.cancel();
    }

    @Listener
    public void onRenderPortal(EventRenderPortal event) {
        if (portal.getValue()) event.cancel();
    }

    @Listener
    public void onFOV(EventFOV event) {
        if (dynamicFoV.getValue()) {
            event.setValue(event.getUseFoVSetting()
                    ? mc.gameSettings.fovSetting
                    : 70.0f);
            event.cancel();
        }
    }
}
