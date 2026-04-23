package org.spotifumtp37.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.spotifumtp37.model.SpotifUMData;
import org.spotifumtp37.model.album.Album;
import org.spotifumtp37.model.playlist.Playlist;
import org.spotifumtp37.model.subscription.SubscriptionPlan;
import org.spotifumtp37.model.user.User;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Map;

public class JsonDataParser {
    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = "users.json";
    private static final String ALBUMS_FILE = "albums.json";
    private static final String PLAYLISTS_FILE = "playlists.json";

    private final Gson gson;

    public JsonDataParser() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(SubscriptionPlan.class, new SubscriptionPlanAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(org.spotifumtp37.model.album.Song.class, new SongTypeAdapter())//for explicit and multimedia songs
                .setPrettyPrinting()
                .create();
        new File(DATA_DIR).mkdirs();
    }

    /**
     * Deserializes all the state from JSON
     * Supports both single file and multiple file formats.
     */
    public SpotifUMData fromJsonData(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.isDirectory()) {
            return loadFromMultipleFiles(filePath);
        } else {
            // Original single file behavior
            try (FileReader reader = new FileReader(filePath)) {
                return gson.fromJson(reader, SpotifUMData.class);
            }
        }
    }

    /**
     * Serializes all the state to JSON.
     * Supports both single file and multiple file formats.
     */
    public void toJsonData(SpotifUMData data, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.isDirectory()) {
            saveToMultipleFiles(data, filePath);
        } else {
            // Original single file behavior
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(data, writer);
            }
        }
    }

    private SpotifUMData loadFromMultipleFiles(String directoryPath) throws IOException {
        SpotifUMData data = new SpotifUMData();

        // Load users
        File usersFile = new File(directoryPath, USERS_FILE);
        if (usersFile.exists()) {
            Type userMapType = new TypeToken<Map<String, User>>() {
            }.getType();
            try (FileReader reader = new FileReader(usersFile)) {
                Map<String, User> users = gson.fromJson(reader, userMapType);
                data.setMapUsers(users);
            }
        }

        // Load albums
        File albumsFile = new File(directoryPath, ALBUMS_FILE);
        if (albumsFile.exists()) {
            Type albumMapType = new TypeToken<Map<String, Album>>() {
            }.getType();
            try (FileReader reader = new FileReader(albumsFile)) {
                Map<String, Album> albums = gson.fromJson(reader, albumMapType);
                data.setMapAlbums(albums);
            }
        }

        // Load playlists
        File playlistsFile = new File(directoryPath, PLAYLISTS_FILE);
        if (playlistsFile.exists()) {
            Type playlistMapType = new TypeToken<Map<String, Playlist>>() {
            }.getType();
            try (FileReader reader = new FileReader(playlistsFile)) {
                Map<String, Playlist> playlists = gson.fromJson(reader, playlistMapType);
                data.setMapPlaylists(playlists);
            }
        }

        return data;
    }

    private void saveToMultipleFiles(SpotifUMData data, String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save users
        try (FileWriter writer = new FileWriter(new File(directory, USERS_FILE))) {
            gson.toJson(data.getMapUsers(), writer);
        }

        // Save albums
        try (FileWriter writer = new FileWriter(new File(directory, ALBUMS_FILE))) {
            gson.toJson(data.getMapAlbumsCopy(), writer);
        }

        // Save playlists
        try (FileWriter writer = new FileWriter(new File(directory, PLAYLISTS_FILE))) {
            gson.toJson(data.getMapPlaylists(), writer);
        }
    }
}