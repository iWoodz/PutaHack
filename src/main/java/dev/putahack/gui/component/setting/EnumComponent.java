package dev.putahack.gui.component.setting;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.putahack.gui.component.Component;
import dev.putahack.setting.Setting;
import dev.putahack.util.io.AudioUtils;
import org.lwjgl.input.Keyboard;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class EnumComponent extends Component {
    private final Setting<Enum<?>> setting;

    public EnumComponent(Setting<Enum<?>> setting) {
        this.setting = setting;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        mc.fontRenderer.drawStringWithShadow(setting.getName(),
                (float) (getX() + 2.0),
                (float) (getY() + (super.getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)), -1);

        String formatted = Setting.formatEnumName(setting.getValue());
        mc.fontRenderer.drawStringWithShadow(formatted,
                (float) (getX() + getWidth() - mc.fontRenderer.getStringWidth(formatted) - 2.0),
                (float) (getY() + (super.getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)), 0xBBBBBB);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInBounds(mouseX, mouseY)) {
            AudioUtils.playClick();
            if (mouseButton == 0) {
                setting.next();
            } else if (mouseButton == 1) {
                setting.previous();
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
