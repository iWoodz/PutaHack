package dev.starstruck.friend;

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
 * @since 06/10/23
 */
public class FriendConfig extends Config {
    private final FriendManager friends;

    public FriendConfig(FriendManager friends) {
        super(new File(FileUtils.root, "friends.json"));
        this.friends = friends;
    }

    @Override
    public void save() {
        JsonArray array = new JsonArray();
        for (Friend friend : friends.getFriends()) {
            JsonObject object = new JsonObject();
            object.addProperty("name", friend.getName());
            // gson moment
            object.addProperty("alias", friend.getAlias() == null
                    ? "" : friend.getAlias());

            array.add(object);
        }

        try {
            FileUtils.writeFile(getFile(), FileUtils.gson.toJson(array));
        } catch (IOException e) {
            Starstruck.getLogger().error("Failed to save to {}", getFile());
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
            Starstruck.getLogger().error("Failed to read from {}", getFile());
            e.printStackTrace();
            return;
        }
        if (content.isEmpty()) return;

        friends.getFriends().clear();

        JsonArray array = FileUtils.jsonParser
                .parse(content).getAsJsonArray();

        for (JsonElement element : array) {
            if (!element.isJsonObject()) continue;

            JsonObject object = element.getAsJsonObject();
            Friend friend = new Friend();
            friend.fromJson(object);

            if (friend.getName() != null) friends.add(friend);
        }

        Starstruck.getLogger().info("Loaded {} friends", friends.getFriends().size());
    }
}
