package dev.starstruck.bind;

import com.google.gson.JsonObject;
import dev.starstruck.setting.JsonSerializable;
import dev.starstruck.util.trait.Nameable;
import dev.starstruck.util.trait.Toggleable;

import java.util.function.Consumer;

/**
 * @author aesthetical
 * @since 04/27/23
 */
public class Bind implements Toggleable, Nameable, JsonSerializable {

    private final String name;

    private BindDevice device;
    private int key = -1;

    private boolean state;
    private Consumer<Bind> action;

    public Bind(String name, Consumer<Bind> action, BindDevice device, int key) {
        this.name = name;
        this.action = action;
        this.device = device;
        this.key = key;
    }

    @Override
    public void onEnable() {
        // empty method
    }

    @Override
    public void onDisable() {
        // empty method
    }

    public BindDevice getDevice() {
        return device;
    }

    public void setDevice(BindDevice device) {
        this.device = device;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setAction(Consumer<Bind> action) {
        this.action = action;
    }

    @Override
    public boolean isToggled() {
        return state;
    }

    @Override
    public void setState(boolean state) {
        this.state = state;
        if (action != null) {
            action.accept(this);
        }
    }

    public void setValue(boolean state) {
        this.state = state;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void fromJson(JsonObject object) {
        key = object.get("keyCode").getAsInt();
        device = BindDevice.valueOf(object.get("device").getAsString());
        setState(object.get("state").getAsBoolean());
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        object.addProperty("keyCode", key);
        object.addProperty("device", device.name());
        object.addProperty("state", state);
        return object;
    }
}
