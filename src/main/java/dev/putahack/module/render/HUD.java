package dev.putahack.module.render;

import dev.putahack.PutaHack;
import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.render.EventRender2D;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;
import dev.putahack.util.render.ColorUtils;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class HUD extends Module {
    public static final Setting<Color> primaryColor = new Setting<>(new Color(120, 105, 225), "PrimaryColor");
    public static final Setting<Color> secondaryColor = new Setting<>(new Color(95, 145, 255), "SecondaryColor");
    private static final Setting<Colors> colors = new Setting<>(Colors.STATIC, "Colors");
    private static final Setting<Double> speed = new Setting<>(5.0, 0.01, 1.0, 10.0, "Speed");

    private final Setting<Boolean> arraylist = new Setting<>(true, "ArrayList");
    private final Setting<Boolean> potions = new Setting<>(true, "Potions");

    public HUD() {
        super("HUD", "what do u think this is retard", ModuleCategory.RENDER);

        // set to on by default
        setState(true);
    }

    @Listener
    public void onRender2D(EventRender2D event) {
        mc.fontRenderer.drawStringWithShadow(
                PutaHack.getName() + " " + PutaHack.getVersion(),
                3.0f, 3.0f, color(50));

        arrayListRender: {
            if (!arraylist.getValue()) break arrayListRender;

            List<Module> enabledModules = PutaHack.get().getModules().get()
                    .stream()
                    .filter((module) -> module.isToggled()
                            || module.getAnimation().getFactor() > 0.0)
                    .collect(Collectors.toList());
            if (enabledModules.isEmpty()) break arrayListRender;

            enabledModules.sort(Comparator.comparingInt(
                    (module) -> -mc.fontRenderer.getStringWidth(module.getName())));

            double y = 4.0;

            for (int i = 0; i < enabledModules.size(); ++i) {
                Module module = enabledModules.get(i);

                double factor = module.getAnimation().getFactor();
                int color = color(i * 50);

                mc.fontRenderer.drawStringWithShadow(module.getName(),
                        (float) (event.getResolution().getScaledWidth_double()
                                - ((mc.fontRenderer.getStringWidth(module.getName()) + 4.0f) * factor)),
                        (float) y, color);

                y += (mc.fontRenderer.FONT_HEIGHT + 2) * factor;
            }
        }

        potionRender: {

        }
    }

    public static int color(int index) {
        switch (colors.getValue()) {
            default:
            case STATIC:
                return primaryColor.getValue().getRGB();

            case RAINBOW:
                return ColorUtils.rainbow(index,
                        speed.getValue().floatValue(), 1.0f, 1.0f);

            case GRADIENT:
                return ColorUtils.gradientRainbow(
                        primaryColor.getValue(), 0.5f, index);

            case SWITCH:
                return ColorUtils.getColorSwitch(primaryColor.getValue(),
                        secondaryColor.getValue(),
                        850.0f,
                        index,
                        11L,
                        speed.getValue());
        }
    }

    public enum Colors {
        STATIC, RAINBOW, GRADIENT, SWITCH
    }
}
