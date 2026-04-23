package org.spotifumtp37.util;

import org.spotifumtp37.model.album.Album;
import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.model.playlist.Playlist;
import org.spotifumtp37.model.user.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Stats {

    /**
     * Gets the most played song from all albums.
     *
     * @param albums A map of album titles to Album objects.
     * @return The most played Song, or null if no albums or songs are available.
     */
    public static Song getMostPlayedSong(Map<String, Album> albums) {
        if (albums == null || albums.isEmpty()) {
            return null;
        }
        return albums.values().stream()
                .flatMap(album -> album.getSongsCopy().stream())
                .max(Comparator.comparingInt(Song::getTimesPlayed))
                .orElse(null);
    }

    /**
     * Gets the artist with the most song plays across all albums.
     *
     * @param albums A map of album titles to Album objects.
     * @return The name of the most listened to artist, or null if no data.
     */
    public static String getMostListenedArtist(Map<String, Album> albums) {
        if (albums == null || albums.isEmpty()) {
            return null;
        }
        Map<String, Integer> playCountByArtist = new HashMap<>();

        for (Album album : albums.values()) {
            for (Song song : album.getSongsCopy()) {
                String artist = song.getArtist();
                playCountByArtist.put(artist, playCountByArtist.getOrDefault(artist, 0) + song.getTimesPlayed());
            }
        }

        return playCountByArtist.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Gets the user who has listened to the most songs (based on history size).
     *
     * @param usersMap A map of usernames to User objects.
     * @return The User with the largest listening history, or null if the map is empty or null.
     */
    public static User getTopListener(Map<String, User> usersMap) {
        if (usersMap == null || usersMap.isEmpty()) {
            return null;
        }
        return usersMap.values().stream()
                .max(Comparator.comparingInt(user -> user.getHistory().size()))
                .orElse(null);
    }

    public static User getTopListenerFromDate(Map<String, User> usersMap, LocalDateTime fromDate) {
        if (usersMap == null || usersMap.isEmpty() || fromDate == null) {
            return null;
        }
        return usersMap.values().stream()
                .max(Comparator.comparingInt(user ->
                        (int) user.getHistory().stream()
                                .filter(h -> h.getTime().isAfter(fromDate) || h.getTime().isEqual(fromDate))
                                .count()
                ))
                .orElse(null);
    }

    /**
     * Gets the user with the most points.
     * Assumes User.getPontos() returns the current, subscription-adjusted points.
     *
     * @param usersMap A map of usernames to User objects.
     * @return The User with the most points, or null if the map is empty or null.
     */
    public static User getUserWithMostPoints(Map<String, User> usersMap) {
        if (usersMap == null || usersMap.isEmpty()) {
            return null;
        }
        return usersMap.values().stream()
                .max(Comparator.comparingDouble(User::getPontos))
                .orElse(null);
    }

    /**
     * Determines the most played genre based on all users' listening histories.
     *
     * @param usersMap A map of usernames to User objects, where each User has a listening history.
     * @return The name of the most reproduced genre, or null if no users or history.
     */
    public static String mostListenedGenre(Map<String, User> usersMap) {
        if (usersMap == null || usersMap.isEmpty()) {
            return null;
        }

        Map<String, Long> GenreCounter = usersMap.values().stream()
                .flatMap(user -> user.getHistory().stream()) // Stream all history entries from all users
                .map(historyEntry -> historyEntry.getSong().getGenre()) // Get the genre of each song in history
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())); // Count occurrences of each genre

        return GenreCounter.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Counts the number of public playlists.
     *
     * @param playlistsMap A map of playlist names to Playlist objects.
     * @return The total count of public playlists.
     */
    public static long countPublicPlaylists(Map<String, Playlist> playlistsMap) {
        if (playlistsMap == null || playlistsMap.isEmpty()) {
            return 0L;
        }
        return playlistsMap.values().stream()
                .filter(Playlist::isPublic)
                .count();
    }

    /**
     * Finds the user who has created the most playlists.
     * This method assumes Playlist objects have a getCreator() method that returns a User object.
     *
     * @param playlistsMap A map of playlist names to Playlist objects.
     * @return The User who has created the most playlists, or null if no playlists or creators.
     */
    public static User userWithMostPlaylists(Map<String, Playlist> playlistsMap) {
        if (playlistsMap == null || playlistsMap.isEmpty()) {
            return null;
        }

        Map<User, Long> playlistCountsByCreator = playlistsMap.values().stream()
                .filter(playlist -> playlist.getCreator() != null)
                .collect(Collectors.groupingBy(Playlist::getCreator, Collectors.counting()));

        return playlistCountsByCreator.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}