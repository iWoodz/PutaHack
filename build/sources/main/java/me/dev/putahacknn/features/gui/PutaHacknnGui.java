package me.dev.putahacknn.features.gui;

import me.dev.putahacknn.features.Feature;
import me.dev.putahacknn.features.gui.components.Component;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.gui.components.items.Item;
import me.dev.putahacknn.features.gui.components.items.buttons.Button;
import me.dev.putahacknn.features.gui.components.items.buttons.ModuleButton;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.modules.client.ClickGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
public class PutaHacknnGui extends GuiScreen
{
    private static PutaHacknnGui PutaHacknnGui;
    private static PutaHacknnGui INSTANCE;
    private final ArrayList<Component> components;

    public PutaHacknnGui() {
        this.components = new ArrayList<Component>();
        this.setInstance();
        this.load();
    }

    public static PutaHacknnGui getInstance() {
        if (PutaHacknnGui.INSTANCE == null) {
            PutaHacknnGui.INSTANCE = new PutaHacknnGui();
        }
        return PutaHacknnGui.INSTANCE;
    }

    public static PutaHacknnGui getClickGui() {
        return getInstance();
    }

    private void setInstance() {
        PutaHacknnGui.INSTANCE = this;
    }

    private void load() {
        int x = -84;
        for (final Module.Category category : PutaHacknn.moduleManager.getCategories()) {
            final ArrayList<Component> components2 = this.components;
            final String name = category.getName();
            x += 90;
            components2.add(new Component(name, x, 4, true) {
                public void setupItems() {
                    counter1 = new int[] { 1 };
                    PutaHacknn.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton((Button)new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    public void updateModule(final Module module) {
        for (final Component component : this.components) {
            for (final Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) {
                    continue;
                }
                final ModuleButton button = (ModuleButton)item;
                final Module mod = button.getModule();
                if (module == null) {
                    continue;
                }
                if (!module.equals(mod)) {
                    continue;
                }
                button.initSettings();
            }
        }
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        final int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        }
        else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(final String name) {
        for (final Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return component;
        }
        return null;
    }

    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }

    public void onGuiClosed() {
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    public void initGui() {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer && ClickGui.getInstance().mode.getValue() == ClickGui.Mode.BLUR) {
            if (this.mc.entityRenderer.getShaderGroup() != null) {
                this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }

    static {
        PutaHacknnGui.INSTANCE = new PutaHacknnGui();
    }
}
