package dev.starstruck.friend.sync;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.starstruck.Starstruck;
import dev.starstruck.friend.Friend;
import dev.starstruck.friend.FriendManager;
import dev.starstruck.util.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class FutureClientFriendSyncer implements IClientFriendSyncer {
    @Override
    public String sync() {
        File file = getFileLocation();
        if (file == null) return "Future folder was not found";

        if (!file.exists()) return file + " was not found";

        String content;
        try {
            content = FileUtils.readFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to read " + file;
        }
        if (content.isEmpty()) return file + " has no file content";

        FriendManager friends = Starstruck.get().getFriends();

        JsonArray array = FileUtils.jsonParser
                .parse(content).getAsJsonArray();


        int added = 0, size = 0;
        for (JsonElement element : array) {
            if (!element.isJsonObject()) continue;
            JsonObject object = element.getAsJsonObject();

            ++size;

            String name = object.get("friend-label").getAsString();
            String alias = object.get("friend-alias").getAsString();

            if (friends.add(new Friend(name, alias))) ++added;
        }

        return "Added " + added + "/" + size + " friends to Starstruck";
    }

    @Override
    public File getFileLocation() {
        String home = System.getProperty("user.home");
        File futureFolder = new File(home, "Future");
        if (!futureFolder.exists()) return null;
        return new File(futureFolder, "friends.json");
    }
}
