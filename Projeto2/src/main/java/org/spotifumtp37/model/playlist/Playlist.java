package org.spotifumtp37.model.playlist;

import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.exceptions.SubscriptionDoesNotAllowException;
import org.spotifumtp37.model.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a playlist of songs in a music application.
 * <p>
 * This class implements both {@link Serializable} for persistence and {@link Playable} for
 * playback functionality. A playlist contains information about its creator,
 * a collection of songs, visibility status (public or private), and methods
 * to manage and play the songs. It also tracks the number of followers
 * and the currently playing song.
 * </p>
 */
public class Playlist implements Serializable, Playable {
    private User creator;
    private String playlistName;
    private String playlistDescription;
    private int numberOfFollowers;
    private String status; // public or private
    private List<Song> songs;
    private Song currentSong;

    /**
     * Constructs a playlist with the specified attributes.
     * The initial current song is set to a random song from the provided list.
     *
     * @param creator The user who created the playlist.
     * @param playlistName The name of the playlist.
     * @param playlistDescription A description of the playlist.
     * @param numberOfFollowers The initial number of users following this playlist.
     * @param status The visibility status of the playlist ("public" or "private").
     * @param songs The list of songs to include in the playlist. If the list is empty,
     *              the behavior of setting a current song might be problematic.
     *              Consider adding a check for an empty song list.
     */
    public Playlist(User creator, String playlistName, String playlistDescription, int numberOfFollowers, String status, List<Song> songs) {
        this.creator = creator;
        this.playlistName = playlistName;
        this.playlistDescription = playlistDescription;
        this.numberOfFollowers = numberOfFollowers;
        this.status = status;
        this.songs = new ArrayList<>();
        if (songs != null) {
            for (Song song : songs) {
                this.songs.add(song.clone());
            }
        }
        if (!this.songs.isEmpty()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(this.songs.size());
            this.currentSong = this.songs.get(randomIndex);
        } else {
            this.currentSong = null;
        }
    }

    /**
     * Copy constructor that creates a new Playlist as a deep copy of another Playlist.
     * The songs list is deep-copied. The current song is also initially set randomly
     * from the copied list of songs.
     *
     * @param other The Playlist to copy.
     */
    public Playlist(Playlist other) {
        this.creator = other.creator; // Assuming User is immutable or shared reference is intended
        this.playlistName = other.playlistName;
        this.playlistDescription = other.playlistDescription;
        this.numberOfFollowers = other.numberOfFollowers;
        this.status = other.status;
        this.songs = new ArrayList<>();
        if (other.songs != null) {
            for (Song song : other.songs) {
                this.songs.add(song.clone()); // Assuming Song has a clone method
            }
        }
        if (!this.songs.isEmpty()) {
            Random rand = new Random();
            // Ensure currentSong is from the *new* list of songs
            int randomIndex = rand.nextInt(this.songs.size());
            this.currentSong = this.songs.get(randomIndex);
        } else {
            this.currentSong = null;
        }
    }

    /**
     * Creates and returns a deep copy of this playlist.
     * This method utilizes the copy constructor.
     *
     * @return A new Playlist instance that is a copy of this playlist.
     */
    @Override
    public Playlist clone() {
        return new Playlist(this);
    }

    /**
     * Compares this playlist to another object for equality.
     * <p>
     * Two playlists are considered equal if they have the same creator, name,
     * description, number of followers, status, a structurally equal list of songs,
     * and the same current song.
     * </p>
     *
     * @param obj The object to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Playlist other = (Playlist) obj;
        return Objects.equals(creator, other.creator) && Objects.equals(playlistName, other.playlistName)
                && Objects.equals(playlistDescription, other.playlistDescription) && numberOfFollowers == other.numberOfFollowers
                && Objects.equals(status, other.status) && Objects.equals(songs, other.songs) // Assumes Song.equals() is well-defined
                && Objects.equals(currentSong, other.currentSong); // Assumes Song.equals() is well-defined
    }

    /**
     * Returns a string representation of the playlist.
     * This includes the creator, name, description, follower count, status,
     * list of songs, and the current song.
     *
     * @return A string containing all the playlist's attributes.
     */
    @Override
    public String toString() {
        return "Playlist{" + "creator: {" + (creator != null ? creator.toString() : "null") + "} \'" + ", playlistName='" + playlistName + '\''
                + ", playlistDescription='" + playlistDescription + '\'' + ", numberOfFollowers=" + numberOfFollowers
                + ", status='" + status + '\'' + ", songs=" + (songs != null ? songs.toString() : "[]") + '}'
                + ", currentsong: {" + (currentSong != null ? currentSong.toString() : "null") + "}";
    }

    /**
     * Gets the user who created this playlist.
     *
     * @return The creator of the playlist.
     */
    public User getCreator() {
        return creator;
    }

    /**
     * Gets the name of the playlist.
     *
     * @return The playlist name.
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Gets the description of the playlist.
     *
     * @return The playlist description.
     */
    public String getPlaylistDescription() {
        return playlistDescription;
    }

    /**
     * Gets the number of users following this playlist.
     *
     * @return The number of followers.
     */
    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    /**
     * Gets the visibility status of the playlist (e.g., "public" or "private").
     *
     * @return The status string.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets a defensive copy of the songs in this playlist.
     * Modifying the returned list will not affect the playlist's internal list of songs.
     * Each song in the copied list is a clone of the original song.
     *
     * @return A new list containing clones of the songs in this playlist.
     */
    public List<Song> getSongs() {
        List<Song> copy = new ArrayList<>();
        if (this.songs != null) {
            for (Song song : this.songs) {
                copy.add(song.clone()); // Assuming Song has a clone method
            }
        }
        return copy;
    }

    /**
     * Gets the currently selected song in the playlist.
     * This returns a direct reference, so modifications to the song object
     * will affect the song within the playlist. Consider returning a clone
     * if immutability is desired.
     *
     * @return The current song, or {@code null} if no song is currently selected or the playlist is empty.
     */
    public Song getCurrentSong() {
        return currentSong; // Or currentSong.clone() if a copy is preferred
    }

    /**
     * Sets the creator of the playlist.
     *
     * @param creator The new {@link User} who created the playlist.
     */
    public void setCreatorUsername(User creator) {
        this.creator = creator;
    }

    /**
     * Sets the name of the playlist.
     *
     * @param playlistName The new playlist name.
     */
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    /**
     * Sets the description of the playlist.
     *
     * @param playlistDescription The new playlist description.
     */
    public void setPlaylistDescription(String playlistDescription) {
        this.playlistDescription = playlistDescription;
    }

    /**
     * Sets the number of followers for this playlist.
     *
     * @param numberOfFollowers The new number of followers.
     */
    public void setNumberOfFollowers(int numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    /**
     * Sets the visibility status of the playlist.
     *
     * @param status The new status (e.g., "public" or "private").
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Replaces all songs in this playlist with clones of the songs from the provided list.
     * The existing songs in the playlist are cleared first.
     *
     * @param songs The list of {@link Song} objects to set. Each song will be cloned.
     */
    public void setSongs(List<Song> songs) {
        this.songs.clear();
        if (songs != null) {
            for (Song song : songs) {
                this.songs.add(song.clone()); // Assuming Song has a clone method
            }
        }
        // Optionally, update currentSong if the list of songs changes significantly
        if (!this.songs.isEmpty() && !this.songs.contains(this.currentSong)) {
            this.currentSong = this.songs.get(0); // Or a random one
        } else if (this.songs.isEmpty()) {
            this.currentSong = null;
        }
    }

    /**
     * Sets the currently selected song in the playlist.
     * The provided song should ideally be one of the songs already in the playlist.
     *
     * @param currentSong The new current {@link Song}.
     */
    public void setCurrentSong(Song currentSong) {
        // Consider adding a check to ensure currentSong is part of this.songs
        this.currentSong = currentSong;
    }

    /**
     * Checks if the playlist is private. Comparison is case-insensitive.
     *
     * @return {@code true} if the playlist status is "private", {@code false} otherwise.
     */
    public boolean isPrivate() {
        return "private".equalsIgnoreCase(status);
    }

    /**
     * Checks if the playlist is public. Comparison is case-insensitive.
     *
     * @return {@code true} if the playlist status is "public", {@code false} otherwise.
     */
    public boolean isPublic() {
        return "public".equalsIgnoreCase(status);
    }

    /**
     * Plays the current song in the playlist.
     * This action increments the play count of the current song,
     * adds points to the user, and updates the user's listening history.
     * Does nothing if {@code currentSong} is null.
     *
     * @param user The {@link User} who is playing the song.
     */
    @Override
    public void play(User user) {
        if (this.currentSong != null && user != null) {
            this.currentSong.incrementTimesPlayed();
            user.addPoints();
            user.updateHistory(this.currentSong);
        }
    }

    /**
     * Moves to the next song in the playlist.
     * <p>
     * If the user's subscription plan allows browsing the playlist ({@code canBrowsePlaylist()}),
     * it cycles to the next song in order.
     * Otherwise, if the playlist has more than one song, it selects a new random song
     * that is different from the current one. If the playlist has only one song,
     * the current song remains unchanged.
     * </p>
     * Does nothing if the playlist is empty or currentSong is null.
     *
     * @param user The {@link User} performing the action. Their subscription plan determines behavior.
     */
    @Override
    public void next(User user) {
        if (songs == null || songs.isEmpty() || user == null || user.getSubscriptionPlan() == null) {
            return;
        }

        if (user.getSubscriptionPlan().canBrowsePlaylist()) {
            int index = songs.indexOf(currentSong);
            if (index != -1) { // currentSong is in the list
                int nextIndex = (index + 1) % songs.size();
                currentSong = songs.get(nextIndex);
            } else if (!songs.isEmpty()) { // currentSong was not in list, pick first
                 currentSong = songs.get(0);
            }
        } else {
            if (songs.size() <= 1) { // Only one song or empty, no change or handled by initial check
                return;
            }
            Random rand = new Random();
            int currentIndex = songs.indexOf(currentSong);
            int randomIndex;
            do {
                randomIndex = rand.nextInt(songs.size());
            } while (randomIndex == currentIndex && songs.size() > 1); // ensure different if possible
            currentSong = songs.get(randomIndex);
        }
    }

    /**
     * Selects a new random song from the playlist, ensuring it's different from the current song
     * if the playlist contains more than one song.
     * If the playlist has only one song, the current song remains unchanged.
     * Does nothing if the playlist is empty.
     */
    public void nextShuffle() {
        if (songs == null || songs.isEmpty()) {
            return;
        }
        if (songs.size() == 1) {
            currentSong = songs.get(0); // Ensure current song is set if it was null
            return;
        }
        Random rand = new Random();
        int currentIndex = songs.indexOf(currentSong);
        int randomIndex;
        do {
            randomIndex = rand.nextInt(songs.size());
        } while (randomIndex == currentIndex && songs.size() > 1); // ensure different if possible
        currentSong = songs.get(randomIndex);
    }

    /**
     * Moves to the previous song in the playlist.
     * <p>
     * This action is only allowed if the user's subscription plan permits playlist browsing
     * ({@code canBrowsePlaylist()}). If permitted, it cycles to the previous song in order.
     * </p>
     * Does nothing if the playlist is empty or currentSong is null.
     *
     * @param user The {@link User} performing the action.
     * @throws SubscriptionDoesNotAllowException If the user's subscription plan does not
     *         allow navigating to the previous song.
     */
    @Override
    public void previous(User user) throws SubscriptionDoesNotAllowException {
        if (songs == null || songs.isEmpty() || user == null || user.getSubscriptionPlan() == null) {
            return;
        }

        if (user.getSubscriptionPlan().canBrowsePlaylist()) {
            int index = songs.indexOf(currentSong);
            if (index != -1) { // currentSong is in the list
                int prevIndex = (index - 1 + songs.size()) % songs.size();
                currentSong = songs.get(prevIndex);
            } else if (!songs.isEmpty()) { // currentSong was not in list, pick last
                 currentSong = songs.get(songs.size() - 1);
            }
        } else {
            throw new SubscriptionDoesNotAllowException("Your subscription does not allow going back in the playlist.");
        }
    }

    /**
     * Adds a song to the playlist.
     * <p>
     * This action is only allowed if the playlist creator's subscription plan permits
     * playlist creation/modification ({@code canCreatePlaylist()}).
     * A cloned version of the song is added.
     * </p>
     *
     * @param song The {@link Song} to add. The song will be cloned before adding.
     * @throws SubscriptionDoesNotAllowException If the creator's subscription plan does not
     *         allow adding songs to the playlist.
     * @throws UnsupportedOperationException If the song (or an equal song) is already in the playlist.
     * @throws NullPointerException if the song to add is null.
     */
    public void addSong(Song song) throws SubscriptionDoesNotAllowException {
        Objects.requireNonNull(song, "Cannot add a null song to the playlist.");
        if (creator == null || creator.getSubscriptionPlan() == null) {
             throw new IllegalStateException("Playlist creator or subscription plan is not properly initialized.");
        }

        if (creator.getSubscriptionPlan().canCreatePlaylist()) {
            if (!songs.contains(song)) { // Relies on Song.equals()
                songs.add(song.clone()); // Assuming Song has a clone method
                if (currentSong == null && songs.size() == 1) { // If playlist was empty, set current song
                    currentSong = songs.get(0);
                }
            } else {
                throw new UnsupportedOperationException("This song is already in the playlist.");
            }
        } else {
            throw new SubscriptionDoesNotAllowException("Your subscription does not allow adding songs to the playlist.");
        }
    }

    /**
     * Deletes a song from the playlist.
     * <p>
     * This action is only allowed if the playlist creator's subscription plan permits
     * playlist creation/modification ({@code canCreatePlaylist()}).
     * If the deleted song was the current song, {@code currentSong} might become null
     * or be set to another song if the playlist is not empty.
     * </p>
     *
     * @param song The {@link Song} to delete. Equality is checked using {@code Song.equals()}.
     * @throws SubscriptionDoesNotAllowException If the creator's subscription plan does not
     *         allow deleting songs from the playlist.
     * @throws UnsupportedOperationException If the specified song is not found in the playlist.
     * @throws NullPointerException if the song to delete is null.
     */
    public void deleteSong(Song song) throws SubscriptionDoesNotAllowException {
        Objects.requireNonNull(song, "Cannot delete a null song from the playlist.");
        if (creator == null || creator.getSubscriptionPlan() == null) {
             throw new IllegalStateException("Playlist creator or subscription plan is not properly initialized.");
        }

        if (creator.getSubscriptionPlan().canCreatePlaylist()) {
            boolean removed = songs.remove(song); // Relies on Song.equals()
            if (removed) {
                if (Objects.equals(currentSong, song)) {
                    currentSong = songs.isEmpty() ? null : songs.get(0); // Or a random one
                }
            } else {
                throw new UnsupportedOperationException("This song is not in the playlist.");
            }
        } else {
            throw new SubscriptionDoesNotAllowException("Your subscription does not allow deleting songs from the playlist.");
        }
    }
}