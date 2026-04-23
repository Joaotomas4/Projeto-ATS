package org.spotifumtp37.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spotifumtp37.model.album.Album;
import org.spotifumtp37.model.album.ExplicitSong;
import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.model.subscription.FreePlan;
import org.spotifumtp37.model.subscription.PremiumBase;
import org.spotifumtp37.model.subscription.PremiumTop;
import org.spotifumtp37.model.subscription.SubscriptionPlan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private User premiumUser;
    private List<History> emptyHistory;
    private Song song1;
    private Song song2;
    private Song explicitSong;
    private Album album1;
    private Map<String, Album> albumMap;

    @BeforeEach
    void setUp() {
        emptyHistory = new ArrayList<>();
        user = new User("TestUser", "test@example.com", "123 Main St", new FreePlan(), "password123", 0, emptyHistory);
        premiumUser = new User("PremiumUser", "premium@example.com", "456 Oak St", new PremiumBase(), "premium123", 50, emptyHistory);
        
        // Create test songs
        song1 = new Song("Test Song 1", "Artist1", "Publisher1", "Lyrics1", "Notes1", "Rock", 180);
        song2 = new Song("Test Song 2", "Artist2", "Publisher2", "Lyrics2", "Notes2", "Pop", 210);
        explicitSong = new ExplicitSong("Explicit Song", "Artist3", "Publisher3", "Explicit Lyrics", "Notes3", "Rock", 240);
        
        // Create test album and album map
        List<Song> songs = new ArrayList<>();
        songs.add(song1);
        songs.add(song2);
        songs.add(explicitSong);
        album1 = new Album("Test Album", "Various Artists", 2022, "Mixed", songs);
        
        albumMap = new HashMap<>();
        albumMap.put(album1.getTitle(), album1);
    }

    @Test
    void getName() {
        assertEquals("TestUser", user.getName());
        assertEquals("PremiumUser", premiumUser.getName());
    }

    @Test
    void getEmail() {
        assertEquals("test@example.com", user.getEmail());
        assertEquals("premium@example.com", premiumUser.getEmail());
    }

    @Test
    void getAddress() {
        assertEquals("123 Main St", user.getAddress());
        assertEquals("456 Oak St", premiumUser.getAddress());
    }

    @Test
    void getSubscriptionPlan() {
        assertTrue(user.getSubscriptionPlan() instanceof FreePlan);
        assertTrue(premiumUser.getSubscriptionPlan() instanceof PremiumBase);
    }

    @Test
    void getPassword() {
        assertEquals("password123", user.getPassword());
        assertEquals("premium123", premiumUser.getPassword());
    }

    @Test
    void getPontos() {
        assertEquals(0, user.getPontos());
        assertEquals(50, premiumUser.getPontos());
    }

    @Test
    void getHistory() {
        // Initially empty
        assertTrue(user.getHistory().isEmpty());
        
        // Add something to history and check
        user.updateHistory(song1);
        List<History> history = user.getHistory();
        
        assertEquals(1, history.size());
        assertEquals("Test Song 1", history.get(0).getSong().getName());
        
        // Verify history is returned as a copy (modifying returned history doesn't affect user's history)
        history.clear();
        assertEquals(1, user.getHistory().size());
    }

    @Test
    void setSubscriptionPlan() {
        // Test changing from Free to Premium
        SubscriptionPlan premiumPlan = new PremiumBase();
        user.setSubscriptionPlan(premiumPlan);
        assertSame(premiumPlan, user.getSubscriptionPlan());
        
        // Test changing from one premium to another
        SubscriptionPlan topPlan = new PremiumTop();
        premiumUser.setSubscriptionPlan(topPlan);
        assertSame(topPlan, premiumUser.getSubscriptionPlan());
        
        // Test with null (should throw exception)
        assertThrows(IllegalArgumentException.class, () -> user.setSubscriptionPlan(null));
    }

    @Test
    void setPontos() {
        user.setPontos(100);
        assertEquals(100, user.getPontos());
        
        user.setPontos(0);
        assertEquals(0, user.getPontos());
        
        // Test with negative value
        user.setPontos(-10);
        assertEquals(-10, user.getPontos());
    }

    @Test
    void setHistory() {
        // Create a history and add to a list
        History historyEntry = new History();
        historyEntry.setSong(song1);
        historyEntry.setTime(LocalDateTime.now());
        
        List<History> newHistory = new ArrayList<>();
        newHistory.add(historyEntry);
        
        // Set the history
        user.setHistory(newHistory);
        
        // Verify history was set correctly
        List<History> retrievedHistory = user.getHistory();
        assertEquals(1, retrievedHistory.size());
        assertEquals(song1.getName(), retrievedHistory.get(0).getSong().getName());
        
        // Verify that modifying the original list doesn't affect the user's history
        newHistory.clear();
        assertEquals(1, user.getHistory().size());
    }

    @Test
    void setName() {
        user.setName("NewName");
        assertEquals("NewName", user.getName());
        
        user.setName("");
        assertEquals("", user.getName());
        
        user.setName(null);
        assertNull(user.getName());
    }

    @Test
    void setEmail() {
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
        
        user.setEmail("");
        assertEquals("", user.getEmail());
        
        user.setEmail(null);
        assertNull(user.getEmail());
    }

    @Test
    void setAddress() {
        user.setAddress("789 New Street");
        assertEquals("789 New Street", user.getAddress());
        
        user.setAddress("");
        assertEquals("", user.getAddress());
        
        user.setAddress(null);
        assertNull(user.getAddress());
    }

    @Test
    void setPassword() {
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
        
        user.setPassword("");
        assertEquals("", user.getPassword());
        
        user.setPassword(null);
        assertNull(user.getPassword());
    }

    @Test
    void addPoints() {
        // Test with free plan
        double initialPoints = user.getPontos();
        user.addPoints();
        assertTrue(user.getPontos() > initialPoints);
        
        // Test with premium plan
        double initialPremiumPoints = premiumUser.getPontos();
        premiumUser.addPoints();
        assertTrue(premiumUser.getPontos() > initialPremiumPoints);
        
        // Premium should add more points than free plan
        double freePointsAdded = user.getPontos() - initialPoints;
        double premiumPointsAdded = premiumUser.getPontos() - initialPremiumPoints;
        assertTrue(premiumPointsAdded > freePointsAdded);
    }

    @Test
    void testClone() {
        // Test clone with empty history
        User clonedUser = user.clone();

        assertEquals(user.getName(), clonedUser.getName());
        assertEquals(user.getEmail(), clonedUser.getEmail());
        assertEquals(user.getAddress(), clonedUser.getAddress());
        assertEquals(user.getPassword(), clonedUser.getPassword());
        assertEquals(user.getPontos(), clonedUser.getPontos());
        assertEquals(user.getSubscriptionPlan().getClass(), clonedUser.getSubscriptionPlan().getClass());
        assertEquals(user.getHistory().size(), clonedUser.getHistory().size());

        // Test clone with non-empty history
        user.updateHistory(song1);
        User clonedUserWithHistory = user.clone();

        assertEquals(1, clonedUserWithHistory.getHistory().size());
        assertEquals(song1.getName(), clonedUserWithHistory.getHistory().get(0).getSong().getName());

        // Verify deep cloning - modifying original doesn't affect clone
        user.setName("ChangedName");
        assertNotEquals(user.getName(), clonedUser.getName());
    }

    @Test
    void updatePremiumTop() {
        double initialPoints = user.getPontos();
        PremiumTop premiumTopPlan = new PremiumTop();
        
        user.updatePremiumTop(premiumTopPlan);
        
        // Verify subscription plan changed
        assertSame(premiumTopPlan, user.getSubscriptionPlan());
        
        // Verify points increased
        assertEquals(initialPoints + 100, user.getPontos());
    }

    @Test
    void updateHistory() {
        // Check initial history is empty
        assertTrue(user.getHistory().isEmpty());
        
        // Update history with a song
        user.updateHistory(song1);
        
        // Verify history has been updated
        List<History> history = user.getHistory();
        assertEquals(1, history.size());
        assertEquals(song1.getName(), history.get(0).getSong().getName());
        assertEquals(song1.getArtist(), history.get(0).getSong().getArtist());
        
        // Verify timestamp is set correctly (should be recent)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime historyTime = history.get(0).getTime();
        assertTrue(historyTime.getYear() == now.getYear() && 
                   historyTime.getMonth() == now.getMonth() && 
                   historyTime.getDayOfMonth() == now.getDayOfMonth());
    }

    @Test
    void testToString() {
        String userString = user.toString();
        
        // Verify toString contains important user information
        assertTrue(userString.contains("TestUser"));
        assertTrue(userString.contains("test@example.com"));
        assertTrue(userString.contains("123 Main St"));
        assertTrue(userString.contains("password123"));
        assertTrue(userString.contains("pontos=0"));
    }

    @Test
    void testEquals() {
        // Same user
        User sameUser = new User("TestUser", "different@example.com", "Different Address", 
                             new FreePlan(), "differentPassword", 100, emptyHistory);
        assertEquals(user, sameUser);
        
        // Different name
        User differentUser = new User("DifferentUser", "test@example.com", "123 Main St", 
                                  new FreePlan(), "password123", 0, emptyHistory);
        assertNotEquals(user, differentUser);
        
        // Test with null
        assertNotEquals(user, null);
        
        // Test with different object type
        assertNotEquals(user, "string");
        
        // Test user with self
        assertEquals(user, user);
    }

    @Test
    void getTopGenre() {
        // Initially, no top genre
        assertNull(user.getTopGenre());
        
        // Add songs to history
        Song rockSong1 = new Song("Rock Song 1", "Rock Artist", "Publisher", "Lyrics", "Notes", "Rock", 180);
        Song rockSong2 = new Song("Rock Song 2", "Rock Artist", "Publisher", "Lyrics", "Notes", "Rock", 180);
        Song popSong = new Song("Pop Song", "Pop Artist", "Publisher", "Lyrics", "Notes", "Pop", 180);
        
        user.updateHistory(rockSong1);
        user.updateHistory(rockSong2);
        user.updateHistory(popSong);
        
        // Rock should be the top genre (2 vs 1)
        assertEquals("Rock", user.getTopGenre());
        
        // Add more pop songs to change the top genre
        user.updateHistory(popSong);
        user.updateHistory(popSong);
        
        // Now Pop should be the top genre (3 vs 2)
        assertEquals("Pop", user.getTopGenre());
    }

    @Test
    void createTopGenrePlaylist() {
        // Add songs to history to establish a top genre
        user.updateHistory(song1);  // Rock
        user.updateHistory(explicitSong);  // Rock
        
        // Test creating a playlist of top genre
        List<Song> playlist = user.createTopGenrePlaylist(albumMap);
        
        // Should have 2 rock songs
        assertEquals(2, playlist.size());
        boolean hasSong1 = false;
        boolean hasExplicitSong = false;
        
        for (Song song : playlist) {
            if (song.getName().equals(song1.getName())) hasSong1 = true;
            if (song.getName().equals(explicitSong.getName())) hasExplicitSong = true;
        }
        
        assertTrue(hasSong1);
        assertTrue(hasExplicitSong);
        
        // Test with empty album map
        playlist = user.createTopGenrePlaylist(new HashMap<>());
        assertTrue(playlist.isEmpty());
        
        // Test with null album map
        playlist = user.createTopGenrePlaylist(null);
        assertTrue(playlist.isEmpty());
    }

    @Test
    void createTopGenrePlaylistWithinTime() {
        // Add songs to history to establish a top genre
        user.updateHistory(song1);  // Rock
        user.updateHistory(explicitSong);  // Rock
        
        // Test with time limit that allows all rock songs (180 + 240 = 420)
        List<Song> playlist = user.createTopGenrePlaylistWithinTime(albumMap, 500);
        assertEquals(2, playlist.size());
        
        // Test with time limit that allows only one song
        playlist = user.createTopGenrePlaylistWithinTime(albumMap, 200);
        assertEquals(1, playlist.size());
        assertEquals("Rock", playlist.get(0).getGenre());
        
        // Test with time limit that doesn't allow any songs
        playlist = user.createTopGenrePlaylistWithinTime(albumMap, 100);
        assertTrue(playlist.isEmpty());
        
        // Test with empty album map
        playlist = user.createTopGenrePlaylistWithinTime(new HashMap<>(), 500);
        assertTrue(playlist.isEmpty());
        
        // Test with null album map
        playlist = user.createTopGenrePlaylistWithinTime(null, 500);
        assertTrue(playlist.isEmpty());
    }

    @Test
    void createTopGenreExplicitPlaylist() {
        // Add songs to history to establish a top genre
        user.updateHistory(song1);  // Rock (not explicit)
        user.updateHistory(explicitSong);  // Rock (explicit)
        
        // Test creating explicit playlist
        List<Song> playlist = user.createTopGenreExplicitPlaylist(albumMap);
        
        // Should have only the explicit rock song
        assertEquals(1, playlist.size());
        assertEquals(explicitSong.getName(), playlist.get(0).getName());
        assertTrue(playlist.get(0).isExplicit());
        
        // Test with empty album map
        playlist = user.createTopGenreExplicitPlaylist(new HashMap<>());
        assertTrue(playlist.isEmpty());
        
        // Test with null album map
        playlist = user.createTopGenreExplicitPlaylist(null);
        assertTrue(playlist.isEmpty());
    }

    @Test
    void constructors() {
        // Test parameterized constructor
        User paramUser = new User("ParamUser", "param@example.com", "Param Address", 
                              new FreePlan(), "paramPass", 25, emptyHistory);
        assertEquals("ParamUser", paramUser.getName());
        assertEquals("param@example.com", paramUser.getEmail());
        assertEquals("Param Address", paramUser.getAddress());
        assertEquals("paramPass", paramUser.getPassword());
        assertEquals(25, paramUser.getPontos());
        assertTrue(paramUser.getHistory().isEmpty());
        
        // Test copy constructor
        User copyUser = new User(paramUser);
        assertEquals(paramUser.getName(), copyUser.getName());
        assertEquals(paramUser.getEmail(), copyUser.getEmail());
        assertEquals(paramUser.getAddress(), copyUser.getAddress());
        assertEquals(paramUser.getPassword(), copyUser.getPassword());
        assertEquals(paramUser.getPontos(), copyUser.getPontos());
        assertEquals(paramUser.getHistory().size(), copyUser.getHistory().size());
        
        // Test default constructor
        User defaultUser = new User();
        assertEquals("", defaultUser.getName());
        assertEquals("", defaultUser.getEmail());
        assertEquals("", defaultUser.getAddress());
        assertEquals("", defaultUser.getPassword());
        assertEquals(0, defaultUser.getPontos());
        assertTrue(defaultUser.getHistory().isEmpty());
        assertTrue(defaultUser.getSubscriptionPlan() instanceof FreePlan);
    }
}