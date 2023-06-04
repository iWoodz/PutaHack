package dev.putahack.gui.component.setting;

import dev.putahack.gui.animation.Animation;
import dev.putahack.gui.animation.Easing;
import dev.putahack.gui.component.Component;
import dev.putahack.setting.Setting;
import dev.putahack.util.io.AudioUtils;
import dev.putahack.util.render.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class ColorComponent extends Component {
    private static final ResourceLocation RGB_GRADIENT = new ResourceLocation("putahack/textures/click_ui/rgb_gradient.png");
    private static final Color SETTING_BG = new Color(19, 19, 19);
    private static final double PICKER_HEIGHT = 70.0;

    private final Setting<Color> setting;
    private boolean opened;

    private final float[] hsb = new float[3];
    private double pickerX, pickerY, hueX;

    private final Animation openAnimation = new Animation(
            Easing.CUBIC_IN_OUT, 250.0, false);

    public ColorComponent(Setting<Color> setting) {
        this.setting = setting;

        Color c = setting.getValue();
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (pickerX == 0.0 && pickerY == 0.0) {
            pickerX = (getX() + 1.0) + (hsb[2] * getWidth());
            pickerY = (getY() + super.getHeight() + 0.5) + ((1.0f - hsb[1]) * PICKER_HEIGHT);
        }

        if (opened && isInBounds(mouseX, mouseY, getX() + 1.0, getY() + super.getHeight() + 0.5, getWidth() - 2.0, PICKER_HEIGHT)) {

            if (Mouse.isButtonDown(0) && !Mouse.getEventButtonState()) {

                pickerX = mouseX;
                pickerY = mouseY;

                hsb[1] = (float) (1.0 - ((mouseY - (getY() + super.getHeight() + 0.5)) / PICKER_HEIGHT));;
                hsb[2] = (float) ((mouseX - getX()) / getWidth());

                setting.setValue(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
            }
        }

        if (openAnimation.getState() != opened) {
            openAnimation.setState(opened);
        }

        mc.fontRenderer.drawStringWithShadow(
                setting.getName(),
                (float) (getX() + 2.0),
                (float) (getY() + (super.getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)),
                -1);

        double size = super.getHeight() - 4.0;
        Color settingColor = setting.getValue();
        RenderUtils.rect((float) ((getX() + getWidth()) - size - 2.0), (float) (getY() + 2.0), (float) size, (float) size, settingColor.getRGB());

        if (openAnimation.getFactor() > 0.0) {

            double w = (getWidth() - 2.0);

            double y = getY() + super.getHeight() + 0.5;
            int s = Color.getHSBColor(hsb[0], 1.0f, 1.0f).getRGB();

            RenderUtils.gradientRect(getX() + 1.0, y, w, PICKER_HEIGHT, Color.black.getRGB(), Color.black.getRGB(), s, Color.white.getRGB());

            double scaledWidth = mc.fontRenderer.getStringWidth("+") / 2.0;
            mc.fontRenderer.drawString("+", (int) (pickerX - scaledWidth), (int) (pickerY - scaledWidth), -1);

            RenderUtils.renderTexture(RGB_GRADIENT, getX() + 1.0, y + (PICKER_HEIGHT + 5.0), (int) w, 5);

            if (hueX == 0.0) {
                hueX = getX() + 1.0 + (hsb[0] * getWidth());
            }

            if (hueX + 5.0 >= getX() + w) {
                hueX = getX() + w;
            }

            RenderUtils.triangle(hueX, y + (PICKER_HEIGHT + 4.0), 6.0, 6.0, SETTING_BG.getRGB());

            if (isInBounds(mouseX, mouseY, getX() + 1.0, y + (PICKER_HEIGHT + 5.0), w, 5)) {
                double val = (mouseX - getX()) / getWidth();

                if (Mouse.isButtonDown(0) && !Mouse.getEventButtonState()) {
                    hueX = mouseX;
                    setting.setValue(Color.getHSBColor(hsb[0] = (float) val, 1.0f, hsb[2]));
                }
            }

        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInBounds(mouseX, mouseY, getX(), getY(), getWidth(), super.getHeight())) {
            AudioUtils.playClick();
            opened = !opened;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public double getHeight() {
        return super.getHeight() + ((PICKER_HEIGHT + 5.0 + 6.0 + 1.0) * openAnimation.getFactor());
    }
}
