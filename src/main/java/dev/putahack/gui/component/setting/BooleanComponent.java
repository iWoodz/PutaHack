package dev.putahack.gui.component.setting;

import dev.putahack.gui.component.Component;
import dev.putahack.module.render.HUD;
import dev.putahack.setting.Setting;
import dev.putahack.util.io.AudioUtils;
import dev.putahack.util.render.RenderUtils;

import java.awt.*;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class BooleanComponent extends Component {
    private static final Color UNTOGGLED_BG = new Color(35, 35, 35);

    private final Setting<Boolean> setting;

    public BooleanComponent(Setting<Boolean> setting) {
        this.setting = setting;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        mc.fontRenderer.drawStringWithShadow(setting.getName(),
                (float) (getX() + 2.0),
                (float) (getY() + (super.getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)), -1);

        double size = getHeight() - 4.0;
        RenderUtils.rect(getX() + getWidth() - size - 2.0,
                getY() + 2.0,
                size, size,
                (setting.getValue()
                        ? HUD.primaryColor.getValue()
                        : UNTOGGLED_BG).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInBounds(mouseX, mouseY)) {
            AudioUtils.playClick();
            setting.setValue(!setting.getValue());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }
}
