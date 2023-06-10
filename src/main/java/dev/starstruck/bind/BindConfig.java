package dev.starstruck.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.starstruck.Starstruck;
import dev.starstruck.config.Config;
import dev.starstruck.util.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class BindConfig extends Config {
    private final BindManager binds;

    public BindConfig(BindManager binds) {
        super(new File(FileUtils.root, "binds.json"));
        this.binds = binds;
    }

    @Override
    public void save() {
        JsonArray array = new JsonArray();
        for (Bind bind : binds.getBindList()) {
            array.add(bind.toJson());
        }

        try {
            FileUtils.writeFile(getFile(), FileUtils.gson.toJson(array));
        } catch (IOException e) {
            Starstruck.getLogger().error("Failed to save {}", getFile());
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        if (!getFile().exists()) {
            try {
                boolean result = getFile().createNewFile();
                Starstruck.getLogger().info("Created {} file {}",
                        getFile(), result ? "successfully" : "unsuccessfully");
            } catch (IOException e) {
                Starstruck.getLogger().error("Failed to create {}", getFile());
                e.printStackTrace();
            }

            return;
        }

        String content;
        try {
            content = FileUtils.readFile(getFile());
        } catch (IOException e) {
            Starstruck.getLogger().error("Failed to read {}", getFile());
            e.printStackTrace();
            return;
        }

        if (content.isEmpty()) return;

        JsonArray array = FileUtils.jsonParser
                .parse(content).getAsJsonArray();
        if (array == null) return;

        for (JsonElement element : array) {
            if (!element.isJsonObject()) continue;

            JsonObject object = element.getAsJsonObject();
            if (!object.has("name")) continue;

            Bind bind = binds.getBind(object.get("name").getAsString());
            if (bind != null) bind.fromJson(object);
        }
    }
}
