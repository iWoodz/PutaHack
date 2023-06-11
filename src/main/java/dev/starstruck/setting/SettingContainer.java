package dev.starstruck.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author aesthetical
 * @since 04/27/23
 */
public class SettingContainer implements JsonSerializable {

    private final Map<String, Setting<?>> settingNameMap = new LinkedHashMap<>();

    @Override
    public void fromJson(JsonObject object) {
        if (!object.has("settings")) return;

        JsonArray array = object.get("settings").getAsJsonArray();
        for (JsonElement element : array) {
            if (!element.isJsonObject()) continue;

            JsonObject obj = element.getAsJsonObject();
            Setting<?> setting = getSetting(obj.get("id").getAsString());
            if (setting != null) setting.fromJson(obj);
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject configObject = new JsonObject();

        JsonArray settingArray = new JsonArray();
        settingNameMap.forEach((name, setting) -> settingArray.add(setting.toJson()));

        configObject.add("settings", settingArray);

        return configObject;
    }

    /**
     * Registers a setting to this container
     * @param setting the setting
     */
    public void register(Setting<?> setting) {
        settingNameMap.put(setting.getName(), setting);
    }

    /**
     * Gets a setting based off the name
     * @param name the setting name
     * @return the setting that is registered to that name, or null
     * @param <T> the setting value type
     */
    public <T> Setting<T> getSetting(String name) {
        return (Setting<T>) settingNameMap.getOrDefault(name, null);
    }

    /**
     * Gets all the setting values
     * @return the setting values
     */
    public Collection<Setting<?>> getSettings() {
        return settingNameMap.values();
    }
}
