package dev.putahack.gui.component.setting;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.putahack.bind.Bind;
import dev.putahack.bind.BindDevice;
import dev.putahack.gui.component.Component;
import dev.putahack.util.io.AudioUtils;
import org.lwjgl.input.Keyboard;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class BindComponent extends Component {
    private final String name;
    private final Bind bind;

    private boolean listening;

    public BindComponent(String name, Bind bind) {
        this.name = name;
        this.bind = bind;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        mc.fontRenderer.drawStringWithShadow(listening ? "Listening..." : name,
                (float) (getX() + 2.0),
                (float) (getY() + (super.getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)), -1);

        if (listening) return;

        String formatted = "";
        if (bind.getKey() == -1) {
            formatted = "NONE";
        } else {
            switch (bind.getDevice()) {
                case KEYBOARD:
                    formatted = Keyboard.getKeyName(bind.getKey());
                    break;

                case MOUSE:
                    formatted = "MB " + (bind.getKey() + 1);
                    break;

                case UNKNOWN:
                    formatted = "NONE";
                    break;
            }
        }

        mc.fontRenderer.drawStringWithShadow(formatted,
                (float) (getX() + getWidth() - mc.fontRenderer.getStringWidth(formatted) - 2.0),
                (float) (getY() + (super.getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)), 0xBBBBBB);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInBounds(mouseX, mouseY)) {
            AudioUtils.playClick();
            if (mouseButton == 0) {
                listening = !listening;
            } else if (mouseButton == 1) {
                bind.setKey(-1);
                listening = false;
            }

            return;
        }

        if (listening) {
            listening = false;
            bind.setKey(mouseButton);
            bind.setDevice(BindDevice.MOUSE);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (listening) {
            listening = false;
            bind.setKey(keyCode);
            bind.setDevice(BindDevice.KEYBOARD);
        }
    }
}
