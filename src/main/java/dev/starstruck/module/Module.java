package dev.starstruck.module;

import com.google.gson.JsonObject;
import dev.starstruck.Starstruck;
import dev.starstruck.bind.Bind;
import dev.starstruck.bind.BindDevice;
import dev.starstruck.gui.animation.Animation;
import dev.starstruck.gui.animation.Easing;
import dev.starstruck.setting.JsonSerializable;
import dev.starstruck.setting.Setting;
import dev.starstruck.setting.SettingContainer;
import dev.starstruck.util.trait.Nameable;
import dev.starstruck.util.trait.Printable;
import dev.starstruck.util.trait.Toggleable;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;

import static org.lwjgl.input.Keyboard.KEY_NONE;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class Module extends SettingContainer
        implements Nameable,
        Toggleable,
        Printable,
        JsonSerializable {

    /**
     * The minecraft game instance
     */
    protected static final Minecraft mc = Minecraft.getMinecraft();

    private final String name, description;
    private final ModuleCategory category;

    private final Setting<Bind> bind;
    private boolean drawn = false;

    private final Animation animation = new Animation(
            Easing.CUBIC_IN_OUT, 250, false);

    public Module(String name, String description, ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;

        bind = new Setting<>(new Bind(name, (bind) -> {
            if (bind.isToggled()) {
                onEnable();
            } else {
                onDisable();
            }
        }, BindDevice.KEYBOARD, KEY_NONE), name);
    }

    /**
     * Registers settings dynamically via reflection
     */
    public void registerAllSettings() {
        for (Field field : getClass().getDeclaredFields()) {
            if (Setting.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);

                try {
                    register((Setting<?>) field.get(this));
                } catch (IllegalAccessException e) {
                    Starstruck.getLogger().error("Failed to get setting value in module {}", name);
                    e.printStackTrace();
                }
            }
        }

        // register bind setting locally & add to bind manager
        register(bind);
        Starstruck.get().getBinds().addBind(bind.getValue());
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public Bind getBind() {
        return bind.getValue();
    }

    @Override
    public void onEnable() {
        Starstruck.getBus().subscribe(this);
        animation.setState(true);
    }

    @Override
    public void onDisable() {
        Starstruck.getBus().unsubscribe(this);
        animation.setState(false);
    }

    @Override
    public boolean isToggled() {
        return bind.getValue().isToggled();
    }

    @Override
    public void setState(boolean state) {
        bind.getValue().setState(state);
    }

    public boolean isDrawn() {
        return drawn;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    @Override
    public void fromJson(JsonObject object) {
        super.fromJson(object);
        setDrawn(object.get("drawn").getAsBoolean());
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = super.toJson();
        object.addProperty("drawn", drawn);
        return object;
    }

    public Animation getAnimation() {
        return animation;
    }
}
