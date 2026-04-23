package org.spotifumtp37.model;

import org.spotifumtp37.model.album.*;
import org.spotifumtp37.exceptions.AlreadyExistsException;
import org.spotifumtp37.exceptions.DoesntExistException;
import org.spotifumtp37.model.playlist.Playlist;
import org.spotifumtp37.model.user.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class SpotifUMData implements Serializable {
    private Map<String, Album> albums;
    private Map<String, User> users;
    private Map<String, Playlist> playlists;

    public SpotifUMData() {
        this.albums = new HashMap<>();
        this.users = new HashMap<>();
        this.playlists = new HashMap<>();
    }

    public SpotifUMData(SpotifUMData outro) {
        this.albums = new HashMap<>();
        for (Map.Entry<String, Album> entry : outro.albums.entrySet()) {
            this.albums.put(entry.getKey(), entry.getValue().clone());
        }
        this.users = new HashMap<>();
        for (Map.Entry<String, User> entry : outro.users.entrySet()) {
            this.users.put(entry.getKey(), entry.getValue().clone());
        }
        this.playlists = new HashMap<>();
        for (Map.Entry<String, Playlist> entry : outro.playlists.entrySet()) {
            this.playlists.put(entry.getKey(), entry.getValue().clone());
        }
    }

    @Override
    public SpotifUMData clone() {
        return new SpotifUMData(this);
    }

    @Override
    public String toString() {
        return "Albums: " + albums.toString()
                + "\nUsers: " + users.toString()
                + "\nPlaylists: " + playlists.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotifUMData that = (SpotifUMData) o;
        return Objects.equals(albums, that.albums)
                && Objects.equals(users, that.users)
                && Objects.equals(playlists, that.playlists);
    }

    public Map<String, Album> getMapAlbumsCopy() {
        Map<String, Album> mapCarros = new HashMap<>();
        for (Map.Entry<String, Album> entry : albums.entrySet()) {
            mapCarros.put(entry.getKey(), entry.getValue().clone());
        }
        return mapCarros;
    }

    public Map<String, Album> getMapAlbums() {
        return new HashMap<>(albums);
    }

    public Map<String, User> getMapUsers() {
        Map<String, User> mapUsers = new HashMap<>();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            mapUsers.put(entry.getKey(), entry.getValue().clone());
        }
        return mapUsers;
    }

    public User getCurrentUserPointer(String username) {
        return this.users.get(username);
    }

    public Map<String, Playlist> getMapPlaylists() {
        Map<String, Playlist> mapPlaylists = new HashMap<>();
        for (Map.Entry<String, Playlist> entry : playlists.entrySet()) {
            mapPlaylists.put(entry.getKey(), entry.getValue().clone());
        }
        return mapPlaylists;
    }

    public void setMapAlbums(Map<String, Album> mapAlbums) {
        Map<String, Album> newMapAlbums = new HashMap<>();
        for (Map.Entry<String, Album> entry : mapAlbums.entrySet()) {
            newMapAlbums.put(entry.getKey(), entry.getValue().clone());
        }
        this.albums = newMapAlbums;
    }

    public void setMapUsers(Map<String, User> mapUsers) {
        Map<String, User> newMapUsers = new HashMap<>();
        for (Map.Entry<String, User> entry : mapUsers.entrySet()) {
            newMapUsers.put(entry.getKey(), entry.getValue().clone());
        }
        this.users = newMapUsers;
    }

    public void setMapPlaylists(Map<String, Playlist> mapPlaylists) {
        Map<String, Playlist> newMapPlaylists = new HashMap<>();
        for (Map.Entry<String, Playlist> entry : mapPlaylists.entrySet()) {
            newMapPlaylists.put(entry.getKey(), entry.getValue().clone());
        }
        this.playlists = newMapPlaylists;
    }

    public boolean existsAlbum(String title) {
        return albums.containsKey(title);
    }

    public boolean existsSong(String title, String albumTitle) {
        if (existsAlbum(albumTitle)) {
            Album album = albums.get(albumTitle);
            for (Song song : album.getSongsCopy()) {
                if (song.getName().equals(title)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean existsPlaylist(String playlistName) {
        return playlists.containsKey(playlistName);
    }

    public boolean existsUser(String username) {
        return users.containsKey(username);
    }

    public Album getAlbum(String title) throws DoesntExistException {
        if (!existsAlbum(title)) {
            throw new DoesntExistException(title);
        } else {
            return albums.get(title);
        }
    }

    public Song getSong(String title, String albumTitle) throws DoesntExistException {
        if (!existsSong(title, albumTitle)) {
            throw new DoesntExistException(title);
        } else {
            Album album = albums.get(albumTitle);
            for (Song song : album.getSongsCopy()) {
                if (song.getName().equals(title)) {
                    return song;
                }
            }
        }
        return null;
    }

    public Playlist getPlaylist(String playlistName) throws DoesntExistException {
        if (!existsPlaylist(playlistName)) {
            throw new DoesntExistException(playlistName);
        } else {
            return playlists.get(playlistName);
        }
    }

    public Playlist getAnyPlaylist(String playlistName,User creator) throws DoesntExistException {
        if (existsPlaylist(playlistName) && ( this.getPlaylistMapByCreator(creator).containsKey(playlistName) || playlists.get(playlistName).isPublic())) {
            return playlists.get(playlistName);
        } else {
            throw new DoesntExistException(playlistName);
        }
    }

    public User getUser(String username) throws DoesntExistException {
        if (!existsUser(username)) {
            throw new DoesntExistException(username);
        } else {
            return users.get(username).clone();
        }
    }

    public void addAlbum(Album album) throws AlreadyExistsException {
        if (!albums.containsKey(album.getTitle())) {
            albums.put(album.getTitle(), album.clone());
        } else {
            throw new AlreadyExistsException(album.getTitle());
        }
    }

    public void addPlaylist(Playlist playlist) throws AlreadyExistsException {
        if (!playlists.containsKey(playlist.getPlaylistName())) {
            playlists.put(playlist.getPlaylistName(), playlist.clone());
        } else {
            throw new AlreadyExistsException(playlist.getPlaylistName());
        }
    }

    public void addUser(User user) throws AlreadyExistsException {
        if (!users.containsKey(user.getName())) {
            users.put(user.getName(), user.clone());
        } else {
            throw new AlreadyExistsException(user.getName());
        }
    }

    public void removeAlbum(String title) throws DoesntExistException {
        if (albums.containsKey(title)) {
            albums.remove(title);
        } else {
            throw new DoesntExistException(title);
        }
    }

    public void removePlaylist(String playlistName) throws DoesntExistException {
        if (playlists.containsKey(playlistName)) {
            playlists.remove(playlistName);
        } else {
            throw new DoesntExistException(playlistName);
        }
    }

    public void removeUser(String username) throws DoesntExistException {
        if (users.containsKey(username)) {
            users.remove(username);
        } else {
            throw new DoesntExistException(username);
        }
    }

    public Map<String, Playlist> getPlaylistMapByCreator(User creator) {
        return playlists.entrySet().stream()
                .filter(e -> e.getValue().getCreator().equals(creator))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
}