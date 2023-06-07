package dev.putahack.gui.click.module;

import dev.putahack.bind.Bind;
import dev.putahack.gui.animation.Animation;
import dev.putahack.gui.animation.Easing;
import dev.putahack.gui.component.Component;
import dev.putahack.gui.component.setting.*;
import dev.putahack.module.Module;
import dev.putahack.module.render.HUD;
import dev.putahack.setting.Setting;
import dev.putahack.util.io.AudioUtils;

import java.awt.Color;

import static dev.putahack.util.render.RenderUtils.rect;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class ModuleComponent extends Component {
    private static final Color UNTOGGLED_BG = new Color(35, 35, 35);
    private static final Color SETTING_BG = new Color(19, 19, 19);

    private final Animation openAnimation = new Animation(
            Easing.CUBIC_IN_OUT, 250.0, false);
    private final Animation toggleAnimation = new Animation(
            Easing.CUBIC_IN_OUT, 150.0, false);
    private final Animation hoverAnimation = new Animation(
            Easing.CUBIC_IN_OUT, 400.0, false);

    private final Module module;

    public ModuleComponent(Module module) {
        this.module = module;

        for (Setting<?> setting : module.getSettings()) {
            if (setting.getValue() instanceof Bind) {
                getChildren().add(new BindComponent("Bind", (Bind) setting.getValue()));
            } else if (setting.getValue() instanceof Enum<?>) {
                getChildren().add(new EnumComponent((Setting<Enum<?>>) setting));
            } else if (setting.getValue() instanceof Color) {
                getChildren().add(new ColorComponent((Setting<Color>) setting));
            } else if (setting.getValue() instanceof Number) {
                getChildren().add(new NumberComponent((Setting<Number>) setting));
            } else if (setting.getValue() instanceof Boolean) {
                getChildren().add(new BooleanComponent((Setting<Boolean>) setting));
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {

        if (hoverAnimation.getState() != (isInBounds(mouseX, mouseY) || openAnimation.getState()))
            hoverAnimation.setState(isInBounds(mouseX, mouseY));

        if (toggleAnimation.getState() != module.isToggled())
            toggleAnimation.setState(module.isToggled());

        if (openAnimation.getFactor() > 0.0) {
            rect(getX(),
                    getY() + super.getHeight(),
                    getWidth(),
                    getHeight() - super.getHeight(),
                    SETTING_BG.getRGB());

            double y = getY() + super.getHeight() + 1.0;
            for (Component component : getChildren()) {
                if (!component.isVisible()) continue;

                component.setX(getX() + 1.0);
                component.setWidth(getWidth() - 2.0);
                component.setY(y);
                component.setHeight(13.5);

                component.drawComponent(mouseX, mouseY, partialTicks);

                y += component.getHeight();
            }
        }

        rect(getX(), getY(),
                getWidth() * toggleAnimation.getFactor(),
                super.getHeight(),
                (module.isToggled()
                        ? HUD.primaryColor.getValue()
                        : UNTOGGLED_BG).getRGB());
        mc.fontRenderer.drawStringWithShadow(module.getName(),
                (float) (getX() + 2.5 + (2.5 * hoverAnimation.getFactor())),
                (float) (getY() + (super.getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)), -1);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInBounds(mouseX,
                mouseY,
                getX(),
                getY(),
                getWidth(),
                super.getHeight())) {

            AudioUtils.playClick();
            if (mouseButton == 0) { // left click, dragging
                module.setState(!module.isToggled());
            } else if (mouseButton == 1) { // right click
                openAnimation.setState(!openAnimation.getState());
            }
        }

        if (!openAnimation.getState()) return;
        for (Component component : getChildren()) {
            if (!component.isVisible()) continue;
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (!openAnimation.getState()) return;
        for (Component component : getChildren()) {
            if (!component.isVisible()) continue;
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (!openAnimation.getState()) return;
        for (Component component : getChildren()) {
            if (!component.isVisible()) continue;
            component.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public double getHeight() {
        double height = 2.0;
        for (Component component : getChildren()) {
            if (!component.isVisible()) continue;
            height += component.getHeight();
        }

        return super.getHeight() + (height * openAnimation.getFactor());
    }
}
