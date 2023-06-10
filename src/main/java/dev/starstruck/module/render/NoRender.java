package dev.starstruck.module.render;

import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.render.EventFOV;
import dev.starstruck.listener.event.render.EventRenderHurt;
import dev.starstruck.listener.event.render.EventRenderPortal;
import dev.starstruck.listener.event.render.EventRenderToast;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public class NoRender extends Module {
    private final Setting<Boolean> hurt = new Setting<>(true, "Hurt");
    private final Setting<Boolean> portal = new Setting<>(true, "Portal");
    private final Setting<Boolean> dynamicFoV = new Setting<>(true, "DynamicFoV");
    private final Setting<Boolean> toasts = new Setting<>(true, "Toasts");

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

    @Listener
    public void onRenderToast(EventRenderToast event) {
        if (toasts.getValue()) event.cancel();
    }
}
