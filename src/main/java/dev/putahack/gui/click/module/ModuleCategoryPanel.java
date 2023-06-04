package dev.putahack.gui.click.module;

import dev.putahack.gui.animation.Animation;
import dev.putahack.gui.animation.Easing;
import dev.putahack.gui.component.Component;
import dev.putahack.gui.component.DraggableComponent;

import java.awt.Color;

import static dev.putahack.util.render.RenderUtils.*;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class ModuleCategoryPanel extends DraggableComponent {
    private static final Color PANEL_COLOR = new Color(30, 30, 30);

    private final Animation openAnimation = new Animation(
            Easing.CUBIC_IN_OUT, 250.0, true);

    private final String displayCategoryName;

    public ModuleCategoryPanel(String displayCategoryName) {
        this.displayCategoryName = displayCategoryName;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        rect(getX(), getY(), getWidth(), getHeight(), PANEL_COLOR.getRGB());
        mc.fontRenderer.drawStringWithShadow(displayCategoryName,
                (float) (getX() + 2.0),
                (float) (getY() + (super.getHeight() / 2.0) - (mc.fontRenderer.FONT_HEIGHT / 2.0)), -1);

        scissor(getX(), getY(), getWidth(), getHeight());
        {

            if (openAnimation.getFactor() > 0.0) {
                double y = getY() + super.getHeight() + 1.0;
                for (Component component : getChildren()) {
                    component.setX(getX() + 1.0);
                    component.setY(y);
                    component.setWidth(getWidth() - 2.0);
                    component.setHeight(13.5);

                    component.drawComponent(mouseX, mouseY, partialTicks);
                    y += component.getHeight();
                }
            }
        }
        endScissor();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInBounds(mouseX,
                mouseY,
                getX(),
                getY(),
                getWidth(),
                super.getHeight())) {

            if (mouseButton == 0) { // left click, dragging

            } else if (mouseButton == 1) { // right click
                openAnimation.setState(!openAnimation.getState());
            }
        }

        if (!openAnimation.getState()) return;
        for (Component component : getChildren()) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (!openAnimation.getState()) return;
        for (Component component : getChildren()) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (!openAnimation.getState()) return;
        for (Component component : getChildren()) {
            component.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public double getHeight() {
        double height = 2.0;
        for (Component component : getChildren()) {
            height += component.getHeight();
        }

        return super.getHeight() + (height * openAnimation.getFactor());
    }
}
