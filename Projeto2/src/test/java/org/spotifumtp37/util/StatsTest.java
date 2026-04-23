package org.spotifumtp37.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spotifumtp37.model.album.Album;
import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.model.playlist.Playlist;
import org.spotifumtp37.model.subscription.FreePlan;
import org.spotifumtp37.model.subscription.PremiumBase;
import org.spotifumtp37.model.subscription.PremiumTop;
import org.spotifumtp37.model.user.History;
import org.spotifumtp37.model.user.User;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

    private Map<String, Album> albums;
    private Map<String, User> users;
    private Map<String, Playlist> playlists;

    @BeforeEach
    void setUp() {
        // Initialize test data
        albums = new HashMap<>();
        users = new HashMap<>();
        playlists = new HashMap<>();
    }

    @Test
    void getMostPlayedSong() {
        // Create test songs with different play counts
        Song song1 = new Song("Song1", "Artist1", "Publisher1", "Lyrics1", "Notes1", "Rock", 180);
        song1.setTimesPlayed(10);
        
        Song song2 = new Song("Song2", "Artist2", "Publisher2", "Lyrics2", "Notes2", "Pop", 240);
        song2.setTimesPlayed(5);
        
        Song song3 = new Song("Song3", "Artist1", "Publisher1", "Lyrics3", "Notes3", "Rock", 200);
        song3.setTimesPlayed(20); // Most played
        
        // Create albums with songs
        Album album1 = new Album("Album1", "Artist1", 2020, "Rock", Arrays.asList(song1, song3));
        Album album2 = new Album("Album2", "Artist2", 2021, "Pop", Collections.singletonList(song2));
        
        albums.put("Album1", album1);
        albums.put("Album2", album2);
        
        // Test with populated albums map
        Song mostPlayed = Stats.getMostPlayedSong(albums);
        assertNotNull(mostPlayed);
        assertEquals("Song3", mostPlayed.getName());
        assertEquals(20, mostPlayed.getTimesPlayed());
        
        // Test with empty albums map
        assertNull(Stats.getMostPlayedSong(new HashMap<>()));
        
        // Test with null albums map
        assertNull(Stats.getMostPlayedSong(null));
    }

    @Test
    void getMostListenedArtist() {
        // Create test songs with different artists and play counts
        Song song1 = new Song("Song1", "Artist1", "Publisher1", "Lyrics1", "Notes1", "Rock", 180);
        song1.setTimesPlayed(10);
        
        Song song2 = new Song("Song2", "Artist2", "Publisher2", "Lyrics2", "Notes2", "Pop", 240);
        song2.setTimesPlayed(15);
        
        Song song3 = new Song("Song3", "Artist1", "Publisher1", "Lyrics3", "Notes3", "Rock", 200);
        song3.setTimesPlayed(10);
        
        // Create albums with songs
        Album album1 = new Album("Album1", "Artist1", 2020, "Rock", Arrays.asList(song1, song3));
        Album album2 = new Album("Album2", "Artist2", 2021, "Pop", Collections.singletonList(song2));
        
        albums.put("Album1", album1);
        albums.put("Album2", album2);
        
        // Test with populated albums map
        // Artist1 has total plays of 20 (10+10), Artist2 has 15
        String mostListened = Stats.getMostListenedArtist(albums);
        assertNotNull(mostListened);
        assertEquals("Artist1", mostListened);
        
        // Test with empty albums map
        assertNull(Stats.getMostListenedArtist(new HashMap<>()));
        
        // Test with null albums map
        assertNull(Stats.getMostListenedArtist(null));
    }

    @Test
    void getTopListener() {
        // Create songs for histories
        Song song1 = new Song("Song1", "Artist1", "Publisher1", "Lyrics1", "Notes1", "Rock", 180);
        Song song2 = new Song("Song2", "Artist2", "Publisher2", "Lyrics2", "Notes2", "Pop", 240);
        
        // Create users with different history sizes
        List<History> history1 = new ArrayList<>();
        history1.add(new History(song1, LocalDateTime.now()));
        
        List<History> history2 = new ArrayList<>();
        history2.add(new History(song1, LocalDateTime.now()));
        history2.add(new History(song2, LocalDateTime.now()));
        
        User user1 = new User("User1", "user1@email.com", "Address1", new FreePlan(), "pass1", 50, history1);
        User user2 = new User("User2", "user2@email.com", "Address2", new PremiumBase(), "pass2", 100, history2);
        
        users.put("User1", user1);
        users.put("User2", user2);
        
        // Test with populated users map
        User topListener = Stats.getTopListener(users);
        assertNotNull(topListener);
        assertEquals("User2", topListener.getName());
        assertEquals(2, topListener.getHistory().size());
        
        // Test with empty users map
        assertNull(Stats.getTopListener(new HashMap<>()));
        
        // Test with null users map
        assertNull(Stats.getTopListener(null));
    }

    @Test
    void getUserWithMostPoints() {
        // Create users with different points
        User user1 = new User("User1", "user1@email.com", "Address1", new FreePlan(), "pass1", 50, new ArrayList<>());
        User user2 = new User("User2", "user2@email.com", "Address2", new PremiumBase(), "pass2", 100, new ArrayList<>());
        User user3 = new User("User3", "user3@email.com", "Address3", new PremiumTop(), "pass3", 75, new ArrayList<>());
        
        users.put("User1", user1);
        users.put("User2", user2);
        users.put("User3", user3);
        
        // Test with populated users map
        User userWithMostPoints = Stats.getUserWithMostPoints(users);
        assertNotNull(userWithMostPoints);
        assertEquals("User2", userWithMostPoints.getName());
        assertEquals(100, userWithMostPoints.getPontos());
        
        // Test with empty users map
        assertNull(Stats.getUserWithMostPoints(new HashMap<>()));
        
        // Test with null users map
        assertNull(Stats.getUserWithMostPoints(null));
    }

    @Test
    void mostListenedGenre() {
        // Create songs with different genres
        Song rock1 = new Song("Rock1", "Artist1", "Publisher1", "Lyrics1", "Notes1", "Rock", 180);
        Song rock2 = new Song("Rock2", "Artist2", "Publisher2", "Lyrics2", "Notes2", "Rock", 240);
        Song pop1 = new Song("Pop1", "Artist3", "Publisher3", "Lyrics3", "Notes3", "Pop", 200);
        Song jazz1 = new Song("Jazz1", "Artist4", "Publisher4", "Lyrics4", "Notes4", "Jazz", 300);
        
        // Create histories with these songs
        History h1 = new History(rock1, LocalDateTime.now());
        History h2 = new History(rock2, LocalDateTime.now());
        History h3 = new History(pop1, LocalDateTime.now());
        History h4 = new History(jazz1, LocalDateTime.now());
        History h5 = new History(rock1, LocalDateTime.now()); // Add rock again to make it most listened
        
        // Create users with these histories
        List<History> history1 = new ArrayList<>(Arrays.asList(h1, h3));
        List<History> history2 = new ArrayList<>(Arrays.asList(h2, h4, h5));
        
        User user1 = new User("User1", "user1@email.com", "Address1", new FreePlan(), "pass1", 50, history1);
        User user2 = new User("User2", "user2@email.com", "Address2", new PremiumBase(), "pass2", 100, history2);
        
        users.put("User1", user1);
        users.put("User2", user2);
        
        // Test with populated users map
        String mostListenedGenre = Stats.mostListenedGenre(users);
        assertNotNull(mostListenedGenre);
        assertEquals("Rock", mostListenedGenre); // Rock appears 3 times, Pop and Jazz only once
        
        // Test with empty users map
        assertNull(Stats.mostListenedGenre(new HashMap<>()));
        
        // Test with null users map
        assertNull(Stats.mostListenedGenre(null));
    }

    @Test
    void countPublicPlaylists() {
        // Create users for playlists
        User user1 = new User("User1", "user1@email.com", "Address1", new FreePlan(), "pass1", 50, new ArrayList<>());
        User user2 = new User("User2", "user2@email.com", "Address2", new PremiumBase(), "pass2", 100, new ArrayList<>());
        
        // Create songs for playlists
        Song song = new Song("Song", "Artist", "Publisher", "Lyrics", "Notes", "Rock", 180);
        List<Song> songs = Collections.singletonList(song);
        
        // Create playlists with different visibility
        Playlist publicPlaylist1 = new Playlist(user1, "Public1", "Description1", 0, "public", songs);
        Playlist publicPlaylist2 = new Playlist(user2, "Public2", "Description2", 0, "public", songs);
        Playlist privatePlaylist = new Playlist(user1, "Private", "Description3", 0, "private", songs);
        
        playlists.put("Public1", publicPlaylist1);
        playlists.put("Public2", publicPlaylist2);
        playlists.put("Private", privatePlaylist);
        
        // Test with populated playlists map
        long publicCount = Stats.countPublicPlaylists(playlists);
        assertEquals(2, publicCount);
        
        // Test with empty playlists map
        assertEquals(0, Stats.countPublicPlaylists(new HashMap<>()));
        
        // Test with null playlists map
        assertEquals(0, Stats.countPublicPlaylists(null));
    }

    @Test
    void userWithMostPlaylists() {
        // Create users for playlists
        User user1 = new User("User1", "user1@email.com", "Address1", new FreePlan(), "pass1", 50, new ArrayList<>());
        User user2 = new User("User2", "user2@email.com", "Address2", new PremiumBase(), "pass2", 100, new ArrayList<>());
        
        // Create songs for playlists
        Song song = new Song("Song", "Artist", "Publisher", "Lyrics", "Notes", "Rock", 180);
        List<Song> songs = Collections.singletonList(song);
        
        // Create playlists with different creators
        Playlist playlist1 = new Playlist(user1, "Playlist1", "Description1", 0, "public", songs);
        Playlist playlist2 = new Playlist(user1, "Playlist2", "Description2", 0, "private", songs);
        Playlist playlist3 = new Playlist(user2, "Playlist3", "Description3", 0, "public", songs);
        
        playlists.put("Playlist1", playlist1);
        playlists.put("Playlist2", playlist2);
        playlists.put("Playlist3", playlist3);
        
        // Test with populated playlists map
        User userWithMostPlaylists = Stats.userWithMostPlaylists(playlists);
        assertNotNull(userWithMostPlaylists);
        assertEquals("User1", userWithMostPlaylists.getName()); // User1 has 2 playlists, User2 has 1
        
        // Test with empty playlists map
        assertNull(Stats.userWithMostPlaylists(new HashMap<>()));
        
        // Test with null playlists map
        assertNull(Stats.userWithMostPlaylists(null));
    }
}