package org.spotifumtp37.model.playlist;

import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.exceptions.SubscriptionDoesNotAllowException;
import org.spotifumtp37.model.user.User;

import java.io.Serializable;

/**
 * Defines the contract for objects that can be played, such as playlists or albums.
 * It includes methods for playback control (play, next, previous, shuffle) and
 * for retrieving the current song.
 * This interface extends {@link Serializable} to allow playable objects
 * to be persisted.
 */
public interface Playable extends Serializable {
    /**
     * Advances to the next song in the playable item.
     * The behavior might depend on the user's subscription plan (e.g., ordered vs. shuffle).
     *
     * @param user The user performing the action. Their subscription plan might affect behavior.
     */
    void next(User user);

    /**
     * Advances to the next song in a shuffled order.
     * This plays a random song from the playable item, different from the current one if possible.
     */
    void nextShuffle();

    /**
     * Moves to the previous song in the playable item.
     * This action might be restricted based on the user's subscription plan.
     *
     * @param user The user performing the action.
     * @throws SubscriptionDoesNotAllowException If the user's subscription plan does not
     *                                           permit going to the previous song.
     */
    void previous(User user) throws SubscriptionDoesNotAllowException;

    /**
     * Starts playback of the current song in the playable item.
     * This typically involves actions like incrementing play counts and updating user history.
     *
     * @param user The user who is playing the item.
     */
    void play(User user);

    /**
     * Retrieves the currently active or selected song within the playable item.
     *
     * @return The current {@link Song}, or {@code null} if no song is currently active.
     */
    Song getCurrentSong();

}