package dev.putahack.gui.component.setting;

import dev.putahack.gui.component.Component;
import dev.putahack.module.render.HUD;
import dev.putahack.setting.Setting;
import dev.putahack.util.io.AudioUtils;
import dev.putahack.util.math.MathUtils;
import dev.putahack.util.render.RenderUtils;
import org.lwjgl.input.Mouse;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class NumberComponent extends Component {
    private final Setting<Number> setting;

    private final double difference;
    private boolean dragging = false;

    public NumberComponent(Setting<Number> setting) {
        this.setting = setting;
        this.difference = setting.getMax().doubleValue() - setting.getMin().doubleValue();
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            if (!Mouse.isButtonDown(0) || !isInBounds(mouseX, mouseY, getX() - 1.0, getY(), getWidth() + 2, getHeight())) {
                dragging = false;
            }

            if (dragging) {
                setValue(mouseX);
            }
        }

        double part = (setting.getValue().doubleValue() - setting.getMin().doubleValue()) / difference;
        double barWidth = setting.getValue().doubleValue() < setting.getMin().doubleValue() ? 0.0 : (getWidth() - 2.0) * part;

        RenderUtils.rect(getX(), getY(), barWidth, getHeight(), HUD.primaryColor.getValue().getRGB());

        mc.fontRenderer.drawStringWithShadow(
                setting.getName(),
                (float) (getX() + 2.0),
                (float) (getY() + (super.getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)),
                -1);

        String formatted = setting.getValue().toString();
        mc.fontRenderer.drawStringWithShadow(
                formatted,
                (float) ((getX() + getWidth() - 4.0) - mc.fontRenderer.getStringWidth(formatted)),
                (float) (getY() + (getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)),
                0xBBBBBB);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInBounds(mouseX, mouseY) && mouseButton == 0) {
            AudioUtils.playClick();
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    private void setValue(double mouseX) {
        double value = setting.getMin().doubleValue() + difference * (mouseX - getX()) / getWidth();
        double percision = 1.0 / setting.getScale().doubleValue();
        value = Math.round(value * percision) / percision;
        value = MathUtils.round(value, 2);

        if (value > setting.getMax().doubleValue()) {
            value = setting.getMax().doubleValue();
        }

        if (value < setting.getMin().doubleValue()) {
            value = setting.getMin().doubleValue();
        }

        if (setting.getValue() instanceof Integer) {
            setting.setValue((int) value);
        } else if (setting.getValue() instanceof Double) {
            setting.setValue(value);
        } else if (setting.getValue() instanceof Float) {
            setting.setValue((float) value);
        }
    }
}
