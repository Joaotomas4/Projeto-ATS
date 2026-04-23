package org.spotifumtp37.model.album;

import java.io.Serializable;

/**
 * Represents a song with explicit content in a music application.
 * <p>
 * This class extends the base {@link Song} class and adds functionality to mark
 * a song as containing explicit content.
 * </p>
 */
public class ExplicitSong extends Song implements Serializable {
    private final boolean explicit;

    /**
     * Copy constructor that creates a new ExplicitSong as a copy of another ExplicitSong.
     *
     * @param other The ExplicitSong to copy
     */
    ExplicitSong(ExplicitSong other) {
        super(other);
        this.explicit = other.isExplicit();
    }

    /**
     * Parameterized constructor to create an explicit song with specific attributes.
     *
     * @param name The name of the song
     * @param artist The artist who performs the song
     * @param publisher The publisher of the song
     * @param lyrics The lyrics of the song
     * @param musicalNotes The musical notes or composition information
     * @param genre The genre of the song
     * @param durationInSeconds The duration of the song in seconds
     */
    public ExplicitSong(String name, String artist, String publisher, String lyrics, String musicalNotes, String genre, int durationInSeconds) {
        super(name, artist, publisher, lyrics, musicalNotes, genre, durationInSeconds);
        this.explicit = true;
    }

    /**
     * Checks if the song contains explicit content.
     *
     * @return {@code true} since this is an explicit song
     */
    public boolean isExplicit() {
        return explicit;
    }

    /**
     * Checks if the song has multimedia content.
     *
     * @return {@code false} as explicit songs do not inherently have multimedia content
     */
    public boolean isMultimedia() {
        return false;
    }

    /**
     * Creates and returns a deep copy of this explicit song.
     *
     * @return A new ExplicitSong instance that is a copy of this song
     */
    @Override
    public ExplicitSong clone() {
        return new ExplicitSong(this);
    }

    /**
     * Returns a string representation of the explicit song.
     *
     * @return A string containing the base song's string representation plus the explicit flag
     */
    @Override
    public String toString() {
        return super.toString() +
                "explicit=" + explicit +
                '}';
    }
}