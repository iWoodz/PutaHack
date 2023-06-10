package dev.starstruck.friend;

import dev.starstruck.Starstruck;
import dev.starstruck.friend.sync.FutureClientFriendSyncer;
import dev.starstruck.friend.sync.IClientFriendSyncer;
import io.netty.util.internal.ConcurrentSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class FriendManager {

    /**
     * A map for each client's friend syncer
     */
    private final Map<String, IClientFriendSyncer> syncers = new HashMap<>();

    /**
     * A list of friends as a set
     */
    private final Set<Friend> friends = new ConcurrentSet<>();

    public FriendManager() {
        Starstruck.get().getConfigs().add(
                new FriendConfig(this));

        syncers.put("future", new FutureClientFriendSyncer());
    }

    /**
     * Syncs a client's friend list with Starstruck's
     * @param name the name of the client
     * @return a plaintext result
     */
    public String sync(String name) {
        IClientFriendSyncer syncer = syncers.getOrDefault(name, null);
        if (syncer == null) return "Invalid syncer";
        return syncer.sync();
    }

    /**
     * Adds a friend
     * @param friend the {@link Friend} object
     * @return if the add was successful
     */
    public boolean add(Friend friend) {
        if (friends.contains(friend)) return false;
        return friends.add(friend);
    }

    /**
     * Removes a friend
     * @param friend the {@link Friend} object
     * @return if the remove was successful
     */
    public boolean remove(Friend friend) {
        return friends.remove(friend);
    }

    /**
     * Gets a {@link Friend} by name
     * @param name the username of the friend
     * @return the {@link Friend} object or null
     */
    public Friend getFriendByName(String name) {
        // TODO: use a Map for name and alias
        for (Friend friend : friends) {
            if (friend.getName().equals(name)) return friend;
        }
        return null;
    }

    /**
     * Gets a {@link Friend} by alias
     * @param alias the alias of the friend
     * @return the {@link Friend} object or null
     */
    public Friend getFriendByAlias(String alias) {
        // TODO: use a Map for name and alias
        for (Friend friend : friends) {
            if (friend.getAlias() != null
                    && friend.getAlias().equals(alias)) return friend;
        }
        return null;
    }

    /**
     * Gets the client's friends
     * @return a set {@link Friend} objects
     */
    public Set<Friend> getFriends() {
        return friends;
    }
}
