package dev.starstruck.friend;

import com.google.gson.JsonObject;
import dev.starstruck.setting.IJsonSerializable;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class Friend implements IJsonSerializable {
    private String name, alias;

    public Friend() {
        // no-args constructor
        // lol
    }

    public Friend(String name) {
        this.name = name;
    }

    public Friend(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public void fromJson(JsonObject object) {
        name = object.get("name").getAsString();

        String value = object.get("alias").getAsString();
        if (!value.isEmpty()) alias = value;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        // gson moment
        object.addProperty("alias", alias == null ? "" : alias);
        return object;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Friend)) return false;
        return ((Friend) obj).getName().equals(name);
    }
}
