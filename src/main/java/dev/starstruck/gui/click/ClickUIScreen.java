package dev.starstruck.gui.click;

import dev.starstruck.Starstruck;
import dev.starstruck.gui.click.module.ModuleCategoryPanel;
import dev.starstruck.gui.click.module.ModuleComponent;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.module.render.ClickUI;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class ClickUIScreen extends GuiScreen {

    /**
     * Click UI module instance
     */
    private static final ClickUI CLICK_UI = Starstruck.get()
            .getModules().get(ClickUI.class);

    private final List<ModuleCategoryPanel> categoryPanels = new ArrayList<>();

    public ClickUIScreen() {
        double posX = 10.0;
        for (ModuleCategory category : ModuleCategory.values()) {
            List<Module> modules = Starstruck.get().getModules().get()
                    .stream()
                    .filter((module) -> module.getCategory() == category)
                    .collect(Collectors.toList());
            if (modules.isEmpty()) continue;

            ModuleCategoryPanel categoryPanel = new ModuleCategoryPanel(
                    category.getDisplayName());
            categoryPanel.setX(posX);
            categoryPanel.setY(26.0);
            categoryPanel.setWidth(120.0);
            categoryPanel.setHeight(16.5);

            for (Module module : modules) categoryPanel.getChildren().add(
                    new ModuleComponent(module));

            categoryPanels.add(categoryPanel);
            posX += categoryPanel.getWidth() + 4.0;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (ModuleCategoryPanel categoryPanel : categoryPanels) {
            categoryPanel.drawComponent(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (ModuleCategoryPanel categoryPanel : categoryPanels) {
            categoryPanel.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (ModuleCategoryPanel categoryPanel : categoryPanels) {
            categoryPanel.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (ModuleCategoryPanel categoryPanel : categoryPanels) {
            categoryPanel.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        CLICK_UI.setState(false);
        Starstruck.getLogger().info(Starstruck.get().getModules()
                .save("default"));
    }
}
