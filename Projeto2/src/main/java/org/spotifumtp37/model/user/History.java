package org.spotifumtp37.model.user;

import org.spotifumtp37.model.album.Song;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a record of a song played by a user at a specific time.
 * This class is serializable, allowing its state to be saved and restored.
 */
public class History implements Serializable {
    /**
     * The song that was played.
     */
    private Song song;
    /**
     * The date and time when the song was played.
     */
    private LocalDateTime time;

    /**
     * Constructs a new History record.
     *
     * @param song The song that was played.
     * @param time The date and time the song was played.
     */
    public History(Song song, LocalDateTime time) {
        this.song = song;
        this.time = time;
    }

    /**
     * Default constructor for History.
     * Initializes song and time to null.
     */
    public History() {
        this.time = null;
        this.song = null;
    }

    /**
     * Copy constructor for History.
     * Creates a new History object by cloning another History object.
     * The song is cloned to ensure a deep copy.
     *
     * @param other The History object to clone.
     */
    public History(History other) {
        this.song = other.song != null ? other.song.clone() : null;
        this.time = other.getTime();
    }

    /**
     * Gets the time the song was played.
     *
     * @return The {@link LocalDateTime} representing when the song was played.
     */
    public LocalDateTime getTime() {
        return this.time;
    }

    /**
     * Sets the time the song was played.
     *
     * @param time The {@link LocalDateTime} to set as the playback time.
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * Gets the song that was played.
     * Returns a clone of the song to maintain encapsulation.
     *
     * @return A clone of the {@link Song} object, or null if no song is set.
     */
    public Song getSong() {
        return this.song != null ? this.song.clone() : null;
    }

    /**
     * Sets the song that was played.
     * Stores a reference to the provided song, not a deep copy.
     *
     * @param song The {@link Song} to set.
     */
    public void setSong(Song song) {
        // Store the reference, not a deep copy
        this.song = song;
    }

    /**
     * Returns a string representation of the History object.
     *
     * @return A string containing the song and time of playback.
     */
    @Override
    public String toString() {
        return "History{" +
                "song=" + song +
                ", time=" + time +
                '}';
    }

    /**
     * Compares this History object to another object for equality.
     * Two History objects are considered equal if their song and time are equal.
     *
     * @param o The object to compare with this History object.
     * @return {@code true} if the given object is a History object and has the same song and time; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(song, history.song) && Objects.equals(time, history.time);
    }

    /**
     * Returns a hash code value for the History object.
     * The hash code is based on the song and time.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(song, time);
    }

    /**
     * Creates and returns a deep copy of this History object.
     *
     * @return A clone of this History instance.
     */
    @Override
    public History clone() {
        return new History(this);
    }
}