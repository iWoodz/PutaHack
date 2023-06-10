package dev.starstruck.friend.sync;

import java.io.File;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public interface ClientFriendSyncer {

    /**
     * Called to sync Starstruck's friends with another clients friends, and
     * add another client's friends list to this client's friends
     * @return a plaintext result of the friend sync
     */
    String sync();

    /**
     * A file location
     * @return the file location object of the other client's friend list
     */
    File getFileLocation();
}
