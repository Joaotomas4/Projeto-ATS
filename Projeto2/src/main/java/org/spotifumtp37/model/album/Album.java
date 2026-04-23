package org.spotifumtp37.model.album;

import org.spotifumtp37.exceptions.SubscriptionDoesNotAllowException;
import org.spotifumtp37.model.playlist.Playable;
import org.spotifumtp37.model.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a music album containing a collection of songs and associated data.
 * The album includes details such as title, artist, release year, genre,
 * and provides functionality to manage the list of songs.
 * This class implements the Playable interface to support music playback functionality
 * and Serializable to allow album persistence.
 */
public class Album implements Playable, Serializable {
    private String title;
    private String artist;
    private int releaseYear;
    private String genre;
    private List<Song> songs;
    private Song currentSong;

    /**
     * Default constructor that initializes an empty album.
     * Creates an album with empty strings for title, artist, and genre,
     * sets the release year to 0, and initializes an empty song list.
     */
    public Album() {
        this.title = "";
        this.artist = "";
        this.releaseYear = 0;
        this.genre = "";
        this.songs = new ArrayList<>();
        this.currentSong = new Song();
    }

    /**
     * Parameterized constructor to create an album with specific attributes.
     *
     * @param title The title of the album
     * @param artist The name of the artist or band
     * @param releaseYear The year when the album was released
     * @param genre The musical genre of the album
     * @param songs The list of songs included in the album
     */
    public Album(String title, String artist, int releaseYear, String genre, List<Song> songs) {
        this.title = title;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.songs = copySongs(songs);
        this.currentSong = new Song();
    }

    /**
     * Copy constructor that creates a deep copy of another album.
     *
     * @param other The album to copy
     */
    public Album(Album other) {
        this.title = other.getTitle();
        this.artist = other.getArtist();
        this.releaseYear = other.getReleaseYear();
        this.genre = other.getGenre();
        this.songs = copySongs(other.getSongs());
        this.currentSong = new Song(other.getCurrentSong());
    }

    /**
     * Creates a deep copy of a list of songs.
     *
     * @param songs The list of songs to copy
     * @return A new list containing clones of the original songs
     */
    public List<Song> copySongs(List<Song> songs) {
        List<Song> copy = new ArrayList<>();
        for (Song song : songs) {
            copy.add(song.clone());
        }
        return copy;
    }

    /**
     * Adds a new song to the album with the specified attributes.
     * The method creates an appropriate song type (regular, explicit, or multimedia)
     * based on the provided parameters.
     *
     * @param name The name of the song
     * @param publisher The publisher of the song
     * @param lyrics The lyrics of the song
     * @param musicalNotes The musical notes of the song
     * @param genre The genre of the song
     * @param durationInSeconds The duration of the song in seconds
     * @param isExplicit Whether the song contains explicit content
     * @param isMultimedia Whether the song is a multimedia song with video
     * @param videoLink The link to the video for multimedia songs
     * @throws IllegalArgumentException If the song name is empty, duration is not positive,
     *         or a song with the same name already exists in the album
     */
    public void addSong(String name, String publisher, String lyrics, String musicalNotes, String genre,
                        int durationInSeconds, boolean isExplicit, boolean isMultimedia, String videoLink) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Song name cannot be null or empty.");
        }
        if (durationInSeconds <= 0) {
            throw new IllegalArgumentException("Song duration must be positive.");
        }
        if (songs.stream().anyMatch(song -> song.getName().equalsIgnoreCase(name))) {
            throw new IllegalArgumentException("A song with the same name already exists in the album.");
        }

        Song song;
        if (isExplicit) {
            song = new ExplicitSong(name, this.artist, publisher, lyrics, musicalNotes, genre, durationInSeconds);
        } else if (isMultimedia) {
            song = new MultimediaSong(name, this.artist, publisher, lyrics, musicalNotes, genre, durationInSeconds, videoLink);
        } else
            song = new Song(name, this.artist, publisher, lyrics, musicalNotes, genre, durationInSeconds);
        songs.add(song);
    }

    /**
     * Removes a song from the album by name.
     *
     * @param name The name of the song to delete
     * @return true if a song was found and removed, false otherwise
     */
    public boolean deleteSong(String name) {
        return songs.removeIf(song -> song.getName().equals(name));
    }

    /**
     * Returns a deep copy of the album's songs.
     *
     * @return A new list containing clones of the songs on the album
     */
    public List<Song> getSongsCopy() {
        List<Song> copy = new ArrayList<>();
        for (Song song : this.songs) {
            copy.add(song.clone());
        }
        return copy;
    }

    /**
     * Returns a shallow copy of the album's songs.
     * The returned list contains references to the original songs, not copies.
     *
     * @return A new list containing references to the songs on the album
     */
    public List<Song> getSongs() {
        return new ArrayList<>(this.songs);
    }

    /**
     * Gets the title of the album.
     *
     * @return The album title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the current song to a random song from the album.
     */
    public void setCurrentSong() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(songs.size());
        this.currentSong = songs.get(randomIndex);
    }

    /**
     * Sets the title of the album.
     *
     * @param title The new album title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the artist name.
     *
     * @return The name of the artist or band
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the artist name.
     *
     * @param artist The new artist or band name
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Sets the release year of the album.
     *
     * @param releaseYear The year when the album was released
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Sets the genre of the album.
     *
     * @param genre The new musical genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the release year of the album.
     *
     * @return The year when the album was released
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Gets the genre of the album.
     *
     * @return The musical genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Calculates the total duration of all songs on the album.
     *
     * @return The total duration in seconds
     */
    public int getTotalDuration() {
        return songs.stream().mapToInt(Song::getDurationInSeconds).sum();
    }

    /**
     * Gets the currently selected song.
     *
     * @return The current song
     */
    public Song getCurrentSong() {
        return currentSong;
    }

    /**
     * Sets the currently selected song.
     *
     * @param currentSong The song to set as current
     */
    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    /**
     * Moves to the next song in the album, based on the user's subscription plan.
     * For users with appropriate subscription, moves to the next sequential song.
     * For users without proper subscription, selects a random song.
     *
     * @param user The user who is playing the album
     */
    @Override
    public void next(User user) {
        if (user.getSubscriptionPlan().canBrowsePlaylist()) {
            int index = songs.indexOf(currentSong);
            int nextIndex = (index + 1) % songs.size();
            currentSong = songs.get(nextIndex);
        } else {
            if (songs.size() == 1) return;
            Random rand = new Random();
            int currentIndex = songs.indexOf(currentSong);
            int randomIndex;
            do {
                randomIndex = rand.nextInt(songs.size());
            } while (randomIndex == currentIndex);
            currentSong = songs.get(randomIndex);
        }
    }

    /**
     * Selects a random song different from the current one.
     * If there's only one song on the album, no change occurs.
     */
    public void nextShuffle() {
        if (songs.size() == 1) return;
        Random rand = new Random();
        int currentIndex = songs.indexOf(currentSong);
        int randomIndex;
        do {
            randomIndex = rand.nextInt(songs.size());
        } while (randomIndex == currentIndex);
        currentSong = songs.get(randomIndex);
    }

    /**
     * Moves to the previous song on the album if the user's subscription allows it.
     *
     * @param user The user who is playing the album
     * @throws SubscriptionDoesNotAllowException If the user's subscription does not allow
     *         navigating to previous songs
     */
    @Override
    public void previous(User user) throws SubscriptionDoesNotAllowException {
        if (user.getSubscriptionPlan().canBrowsePlaylist()) {
            int index = songs.indexOf(currentSong);
            int prevIndex = (index - 1 + songs.size()) % songs.size();
            currentSong = songs.get(prevIndex);
        } else {
            throw new SubscriptionDoesNotAllowException("Your subscription does not allow going back in the playlist.");
        }
    }

    /**
     * Plays the current song, increments its play count, and updates the user's
     * points and listening history.
     *
     * @param user The user who is playing the song
     */
    @Override
    public void play(User user) {
        this.currentSong.incrementTimesPlayed();
        user.addPoints();
        user.updateHistory(this.currentSong);
    }

    /**
     * Creates and returns a deep copy of this album.
     *
     * @return A new Album instance that is a copy of this album
     */
    @Override
    public Album clone() {
        return new Album(this);
    }

    /**
     * Returns a string representation of the album.
     *
     * @return A string containing the album's title, artist, release year, genre,
     *         songs, and current song
     */
    @Override
    public String toString() {
        return "Album{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", releaseYear=" + releaseYear +
                ", genre='" + genre + '\'' +
                ", songs=" + songs +
                ", currentSong=" + currentSong +
                '}';
    }
}