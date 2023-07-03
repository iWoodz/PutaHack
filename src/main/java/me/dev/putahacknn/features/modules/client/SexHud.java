package me.dev.putahacknn.features.modules.client;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.features.gui.animation.Animation;
import me.dev.putahacknn.features.gui.animation.Easing;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.ColorUtil;
import me.dev.putahacknn.util.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SexHud extends Module {

    private static final float PADDING = 2.0f;

    private final Setting<Boolean> arrayList = new Setting<>("ArrayList", true);

    private final Animation animation = new Animation(
            Easing.CUBIC_IN_OUT, 250, false);

    public SexHud() {
        super("SexHud", "https://media.discordapp.net/attachments/1124878552916033678/1124878625943068763/watermark.gif", Category.CLIENT, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {

        Color color = ClickGui.getInstance().getColor2();

        if (arrayList.getValue()) {

            arraylistRender:
            {
                List<Module> modules = PutaHacknn.moduleManager.modules
                        .stream()
                        .filter((m) -> m.isEnabled() || m.animation.getFactor() > 0.0)
                        .sorted(Comparator.comparingInt((m) -> -PutaHacknn.textManager.getStringWidth(m.getName())))
                        .collect(Collectors.toList());

                if (modules.isEmpty()) break arraylistRender;

                int fontHeight = PutaHacknn.textManager.getFontHeight();

                float y = PADDING;
                for (int i = 0; i < modules.size(); ++i) {
                    Module module = modules.get(i);
                    String displayName = module.getName();

                    int c = ColorUtil.thehahafunny(color, 20, i + 1).getRGB();
                    int width = PutaHacknn.textManager.getStringWidth(displayName);

                    double factor = module.animation.getFactor();
                    float x = (float) (event.getScreenWidth() - (width + PADDING) * factor);

                    RenderUtil.drawGlow(x, y - (PADDING * 2), x + (width + (PADDING * 4)), y + (fontHeight + (PADDING * 4)), ColorUtil.newAlpha(c, 80));
                    // aids
//                    RenderUtil.drawRectangleCorrectly(
//                            (int) (x - PADDING),
//                            (int) (y - PADDING),
//                            (int) (width + (PADDING * 2)),
//                            (int) (fontHeight + (PADDING * 2)),
//                            ColorUtil.newAlpha(c, 60));

                    PutaHacknn.textManager.drawStringWithShadow(displayName, x, y + 2.0f, c);
                    y += (fontHeight + PADDING) * factor;
                }
            }
        }
    }
}
