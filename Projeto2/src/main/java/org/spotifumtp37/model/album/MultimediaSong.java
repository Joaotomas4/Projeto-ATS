package org.spotifumtp37.model.album;

import java.io.Serializable;

/**
 * Represents a song with associated multimedia content in a music application.
 * <p>
 * This class extends the base {@link Song} class and adds functionality to
 * store and manage video links.
 * </p>
 */
public class MultimediaSong extends Song implements Serializable {
    private final boolean multimedia;
    private String videoLink;

    /**
     * Copy constructor that creates a new MultimediaSong as a copy of another MultimediaSong.
     *
     * @param other The MultimediaSong to copy
     */
    public MultimediaSong(MultimediaSong other) {
        super(other);
        this.multimedia = true;
        this.videoLink = other.videoLink;
    }

    /**
     * Gets the video link associated with this multimedia song.
     *
     * @return The URL or reference to the video content
     */
    public String getVideoLink() {
        return videoLink;
    }

    /**
     * Sets the video link associated with this multimedia song.
     *
     * @param videoLink The URL or reference to the video content
     */
    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    /**
     * Parameterized constructor to create a multimedia song with specific attributes.
     *
     * @param name The name of the song
     * @param artist The artist who performs the song
     * @param publisher The publisher of the song
     * @param lyrics The lyrics of the song
     * @param musicalNotes The musical notes or composition information
     * @param genre The genre of the song
     * @param durationInSeconds The duration of the song in seconds
     * @param videoLink The URL or reference to the video content
     */
    MultimediaSong(String name, String artist, String publisher, String lyrics, String musicalNotes, String genre, int durationInSeconds, String videoLink) {
        super(name, artist, publisher, lyrics, musicalNotes, genre, durationInSeconds);
        this.multimedia = true;
        this.videoLink = videoLink;
    }

    /**
     * Checks if the song has multimedia content.
     *
     * @return {@code true} as this is a multimedia song
     */
    public boolean isMultimedia() {
        return multimedia;
    }

    /**
     * Creates and returns a deep copy of this multimedia song.
     *
     * @return A new MultimediaSong instance that is a copy of this song
     */
    @Override
    public MultimediaSong clone() {
        return new MultimediaSong(this);
    }

    /**
     * Returns a string representation of the multimedia song.
     *
     * @return A string containing the base song's string representation plus
     *         the multimedia flag and video link
     */
    @Override
    public String toString() {
        return super.toString() +
                "multimedia=" + multimedia +
                ", videoLink='" + videoLink + '\'' +
                '}';
    }
}