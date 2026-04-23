package org.spotifumtp37.model.album;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a musical song in the application.
 * <p>
 * This class contains the basic attributes of a song including name, artist,
 * publisher, lyrics, musical notes, genre, duration, and play count statistics.
 * It serves as the base class for specialized song types like {@link ExplicitSong}
 * and {@link MultimediaSong}.
 * </p>
 */
public class Song implements Serializable {
    private String name;
    private String artist;
    private String publisher;
    private String lyrics;
    private String musicalNotes;
    private String genre;
    private int durationInSeconds;
    private int timesPlayed;

    /**
     * Default constructor that initializes an empty song.
     * Creates a song with empty strings for all text fields,
     * zero duration, and zero play count.
     */
    public Song() {
        this.name = "";
        this.artist = "";
        this.publisher = "";
        this.lyrics = "";
        this.musicalNotes = "";
        this.genre = "";
        this.durationInSeconds = 0;
        this.timesPlayed = 0;
    }

    /**
     * Parameterized constructor to create a song with specific attributes.
     *
     * @param name The name of the song
     * @param artist The artist who performs the song
     * @param publisher The publisher of the song
     * @param lyrics The lyrics of the song
     * @param musicalNotes The musical notes or composition information
     * @param genre The genre of the song
     * @param durationInSeconds The duration of the song in seconds
     */
    public Song(String name, String artist, String publisher, String lyrics,
                String musicalNotes, String genre, int durationInSeconds) {
        this.name = name;
        this.artist = artist;
        this.publisher = publisher;
        this.lyrics = lyrics;
        this.musicalNotes = musicalNotes;
        this.genre = genre;
        this.durationInSeconds = durationInSeconds;
        this.timesPlayed = 0;
    }

    /**
     * Copy constructor that creates a new Song as a copy of another Song.
     *
     * @param other The Song to copy
     */
    public Song(Song other) {
        this.name = other.getName();
        this.artist = other.getArtist();
        this.publisher = other.getPublisher();
        this.lyrics = other.getLyrics();
        this.musicalNotes = other.getMusicalNotes();
        this.genre = other.getGenre();
        this.durationInSeconds = other.getDurationInSeconds();
        this.timesPlayed = other.getTimesPlayed();
    }

    /**
     * Gets the name of the song.
     *
     * @return The song name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the song.
     *
     * @param name The new song name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the artist who performs the song.
     *
     * @return The artist name
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the artist who performs the song.
     *
     * @param artist The new artist name
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Gets the publisher of the song.
     *
     * @return The publisher name
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher of the song.
     *
     * @param publisher The new publisher name
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the lyrics of the song.
     *
     * @return The song lyrics
     */
    public String getLyrics() {
        return lyrics;
    }

    /**
     * Sets the lyrics of the song.
     *
     * @param lyrics The new song lyrics
     */
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * Gets the musical notes or composition information.
     *
     * @return The musical notes
     */
    public String getMusicalNotes() {
        return musicalNotes;
    }

    /**
     * Sets the musical notes or composition information.
     *
     * @param musicalNotes The new musical notes
     */
    public void setMusicalNotes(String musicalNotes) {
        this.musicalNotes = musicalNotes;
    }

    /**
     * Gets the genre of the song.
     *
     * @return The song genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the song.
     *
     * @param genre The new song genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the duration of the song in seconds.
     *
     * @return The duration in seconds
     */
    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    /**
     * Sets the duration of the song in seconds.
     *
     * @param durationInSeconds The new duration in seconds
     */
    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    /**
     * Gets the number of times the song has been played.
     *
     * @return The play count
     */
    public int getTimesPlayed() {
        return timesPlayed;
    }

    /**
     * Sets the number of times the song has been played.
     *
     * @param timesPlayed The new play count
     */
    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

    /**
     * Increments the play count of the song by one.
     */
    public void incrementTimesPlayed() {
        this.timesPlayed++;
    }

    /**
     * Compares this song to another object for equality.
     * <p>
     * Two songs are considered equal if they have the same name, artist,
     * publisher, duration, lyrics, musical notes, genre, and play count.
     * </p>
     *
     * @param obj The object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Song other = (Song) obj;
        return Objects.equals(name, other.name) && Objects.equals(artist, other.artist)
                && Objects.equals(publisher, other.publisher) && durationInSeconds == other.durationInSeconds
                && Objects.equals(lyrics, other.lyrics) && Objects.equals(musicalNotes, other.musicalNotes)
                && Objects.equals(genre, other.genre) && Objects.equals(timesPlayed, other.timesPlayed);
    }

    /**
     * Generates a hash code for this song.
     * <p>
     * The hash code is based on the song's name, artist, and duration.
     * </p>
     *
     * @return A hash code value for this song
     */
    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getArtist().hashCode();
        result = 31 * result + getDurationInSeconds();
        return result;
    }

    /**
     * Creates and returns a deep copy of this song.
     *
     * @return A new Song instance that is a copy of this song
     */
    @Override
    public Song clone() {
        return new Song(this);
    }

    /**
     * Returns a string representation of the song.
     *
     * @return A string containing all the song's attributes
     */
    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", publisher='" + publisher + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", musicalNotes='" + musicalNotes + '\'' +
                ", genre='" + genre + '\'' +
                ", durationInSeconds=" + durationInSeconds +
                ", timesPlayed=" + timesPlayed +
                '}';
    }

    /**
     * Checks if the song contains explicit content.
     * <p>
     * This method returns {@code false} for regular songs.
     * It is overridden in the {@link ExplicitSong} subclass.
     * </p>
     *
     * @return {@code false} for regular songs
     */
    public boolean isExplicit() {
        return false;
    }
    
    /**
     * Checks if the song has multimedia content.
     * <p>
     * This method returns {@code false} for regular songs.
     * It is overridden in the {@link MultimediaSong} subclass.
     * </p>
     *
     * @return {@code false} for regular songs
     */
    public boolean isMultimedia() {
        return false;
    }
}