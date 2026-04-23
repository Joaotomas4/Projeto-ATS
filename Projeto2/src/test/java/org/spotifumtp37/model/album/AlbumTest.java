package org.spotifumtp37.model.album;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spotifumtp37.exceptions.SubscriptionDoesNotAllowException;
import org.spotifumtp37.model.subscription.FreePlan;
import org.spotifumtp37.model.subscription.PremiumBase;
import org.spotifumtp37.model.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlbumTest {

    private Album album;
    private List<Song> songs;
    private Song song1;
    private Song song2;
    private User freeUser;
    private User premiumUser;

    @BeforeEach
    void setUp() {
        // Create test songs
        song1 = new Song("Song1", "Artist1", "Publisher1", "Lyrics1", "Notes1", "Rock", 180);
        song2 = new Song("Song2", "Artist1", "Publisher1", "Lyrics2", "Notes2", "Pop", 240);
        songs = new ArrayList<>(Arrays.asList(song1, song2));
        
        // Create test album
        album = new Album("Test Album", "Artist1", 2022, "Mixed", songs);
        
        // Create test users with different subscription plans
        freeUser = new User("FreeUser", "free@email.com", "Address1", new FreePlan(), "pass1", 0, new ArrayList<>());
        premiumUser = new User("PremiumUser", "premium@email.com", "Address2", new PremiumBase(), "pass2", 0, new ArrayList<>());
    }

    @Test
    void copySongs() {
        // Test copying a list of songs
        List<Song> copiedSongs = album.copySongs(songs);
        
        // Verify copied songs are equal but not the same instances
        assertEquals(songs.size(), copiedSongs.size());
        for (int i = 0; i < songs.size(); i++) {
            assertEquals(songs.get(i), copiedSongs.get(i));
            assertNotSame(songs.get(i), copiedSongs.get(i));
        }
        
        // Test copying an empty list
        List<Song> emptySongs = new ArrayList<>();
        List<Song> copiedEmptySongs = album.copySongs(emptySongs);
        assertEquals(0, copiedEmptySongs.size());
        
        // Test copying null (should handle gracefully or throw appropriate exception)
        assertThrows(NullPointerException.class, () -> album.copySongs(null));
    }

    @Test
    void addSong() {
        // Test adding a regular song
        album.addSong("NewSong", "Publisher2", "NewLyrics", "NewNotes", "Jazz", 
                      200, false, false, null);
        
        // Verify song was added
        List<Song> allSongs = album.getSongsCopy();
        assertEquals(3, allSongs.size());
        
        Song addedSong = allSongs.get(2);
        assertEquals("NewSong", addedSong.getName());
        assertEquals("Artist1", addedSong.getArtist()); // Should use album's artist
        assertEquals("Jazz", addedSong.getGenre());
        assertEquals(200, addedSong.getDurationInSeconds());
        assertFalse(addedSong.isExplicit());
        assertFalse(addedSong.isMultimedia());
        
        // Test adding an explicit song
        album.addSong("ExplicitSong", "Publisher3", "ExplicitLyrics", "Notes3", "Rock", 
                      210, true, false, null);
        
        allSongs = album.getSongsCopy();
        assertEquals(4, allSongs.size());
        
        Song explicitSong = allSongs.get(3);
        assertEquals("ExplicitSong", explicitSong.getName());
        assertTrue(explicitSong.isExplicit());
        
        // Test adding a multimedia song
        album.addSong("MultimediaSong", "Publisher4", "VideoLyrics", "Notes4", "Pop", 
                      220, false, true, "http://example.com/video");
        
        allSongs = album.getSongsCopy();
        assertEquals(5, allSongs.size());
        
        Song multimediaSong = allSongs.get(4);
        assertEquals("MultimediaSong", multimediaSong.getName());
        assertTrue(multimediaSong.isMultimedia());
        
        // Test adding a song with invalid parameters
        assertThrows(IllegalArgumentException.class, () -> 
            album.addSong("", "Publisher", "Lyrics", "Notes", "Genre", 200, false, false, null)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            album.addSong("InvalidDuration", "Publisher", "Lyrics", "Notes", "Genre", 0, false, false, null)
        );
        
        // Test adding a song with duplicate name
        assertThrows(IllegalArgumentException.class, () -> 
            album.addSong("Song1", "Publisher", "Lyrics", "Notes", "Genre", 200, false, false, null)
        );
    }

    @Test
    void deleteSong() {
        // Test deleting an existing song
        boolean result = album.deleteSong("Song1");
        assertTrue(result);
        
        // Verify song was deleted
        List<Song> remainingSongs = album.getSongsCopy();
        assertEquals(1, remainingSongs.size());
        assertEquals("Song2", remainingSongs.get(0).getName());
        
        // Test deleting a song that doesn't exist
        result = album.deleteSong("NonExistentSong");
        assertFalse(result);
        
        // Verify no changes to songs list
        remainingSongs = album.getSongsCopy();
        assertEquals(1, remainingSongs.size());
    }

    @Test
    void getSongsCopy() {
        // Test getting a copy of songs
        List<Song> songsCopy = album.getSongsCopy();
        
        // Verify correct number of songs
        assertEquals(2, songsCopy.size());
        
        // Verify songs are copies, not the same instances
        assertEquals(song1.getName(), songsCopy.get(0).getName());
        assertEquals(song2.getName(), songsCopy.get(1).getName());
        assertNotSame(song1, songsCopy.get(0));
        assertNotSame(song2, songsCopy.get(1));
        
        // Verify modifying the copy doesn't affect original album
        songsCopy.remove(0);
        assertEquals(1, songsCopy.size());
        assertEquals(2, album.getSongsCopy().size()); // Original album still has 2 songs
    }

    @Test
    void getSongs() {
        // Test getting a new list of songs
        List<Song> songsList = album.getSongs();
        
        // Verify correct number of songs
        assertEquals(2, songsList.size());
        
        // Unlike getSongsCopy, getSongs returns references to the original songs
        assertEquals(song1.getName(), songsList.get(0).getName());
        assertEquals(song2.getName(), songsList.get(1).getName());
        
        // Verify modifying the returned list doesn't affect original album
        songsList.remove(0);
        assertEquals(1, songsList.size());
        assertEquals(2, album.getSongs().size()); // Original album still has 2 songs
    }

    @Test
    void getTitle() {
        assertEquals("Test Album", album.getTitle());
    }

    @Test
    void setTitle() {
        album.setTitle("New Album Title");
        assertEquals("New Album Title", album.getTitle());
    }

    @Test
    void getArtist() {
        assertEquals("Artist1", album.getArtist());
    }

    @Test
    void setArtist() {
        album.setArtist("New Artist");
        assertEquals("New Artist", album.getArtist());
    }

    @Test
    void setReleaseYear() {
        album.setReleaseYear(2023);
        assertEquals(2023, album.getReleaseYear());
    }

    @Test
    void setGenre() {
        album.setGenre("New Genre");
        assertEquals("New Genre", album.getGenre());
    }

    @Test
    void getReleaseYear() {
        assertEquals(2022, album.getReleaseYear());
    }

    @Test
    void getGenre() {
        assertEquals("Mixed", album.getGenre());
    }

    @Test
    void getTotalDuration() {
        // Song1 = 180 seconds, Song2 = 240 seconds, total = 420 seconds
        assertEquals(420, album.getTotalDuration());
        
        // Test with empty album
        Album emptyAlbum = new Album("Empty", "NoOne", 2022, "None", new ArrayList<>());
        assertEquals(0, emptyAlbum.getTotalDuration());
    }

    @Test
    void getCurrentSong() {
        // Current song should be initialized in the constructor
        assertNotNull(album.getCurrentSong());
    }

    @Test
    void setCurrentSong() {
        // Test setting current song to a specific song
        Song newSong = new Song("New Current", "Artist3", "Publisher3", "Lyrics3", "Notes3", "Jazz", 300);
        album.setCurrentSong(newSong);
        
        assertEquals(newSong.getName(), album.getCurrentSong().getName());
        assertEquals(newSong.getArtist(), album.getCurrentSong().getArtist());
    }

    @Test
    void next() {
        // Get current song
        Song initialSong = album.getCurrentSong();
        
        // For premium user, next should go to the next song in sequence
        album.next(premiumUser);
        Song nextSong = album.getCurrentSong();
        assertNotEquals(initialSong.getName(), nextSong.getName());
        
        // For free user, next should go to a random song (harder to test deterministically)
        // Let's set a known current song and see that it changes
        album.setCurrentSong(song1);
        album.next(freeUser);
        // Since there are only 2 songs, next for free user should go to song2
        assertEquals(song2.getName(), album.getCurrentSong().getName());
    }

    @Test
    void previous() {
        // Set a known current song
        album.setCurrentSong(song2);
        
        // For premium user, previous should go to the previous song
        try {
            album.previous(premiumUser);
            assertEquals(song1.getName(), album.getCurrentSong().getName());
        } catch (SubscriptionDoesNotAllowException e) {
            fail("Premium user should be able to go to previous song");
        }
        
        // For free user, previous should throw an exception
        album.setCurrentSong(song1);
        assertThrows(SubscriptionDoesNotAllowException.class, () -> album.previous(freeUser));
    }

    @Test
    void testClone() {
        // Clone the album
        Album clonedAlbum = album.clone();
        
        // Verify clone is equal but not the same instance
        assertEquals(album.getTitle(), clonedAlbum.getTitle());
        assertEquals(album.getArtist(), clonedAlbum.getArtist());
        assertEquals(album.getReleaseYear(), clonedAlbum.getReleaseYear());
        assertEquals(album.getGenre(), clonedAlbum.getGenre());
        
        assertNotSame(album, clonedAlbum);
        
        // Verify songs are cloned
        List<Song> originalSongs = album.getSongsCopy();
        List<Song> clonedSongs = clonedAlbum.getSongsCopy();
        
        assertEquals(originalSongs.size(), clonedSongs.size());
        for (int i = 0; i < originalSongs.size(); i++) {
            assertEquals(originalSongs.get(i).getName(), clonedSongs.get(i).getName());
            assertNotSame(originalSongs.get(i), clonedSongs.get(i));
        }
    }

    @Test
    void play() {
        // Get initial play count for current song
        Song currentSong = album.getCurrentSong();
        int initialPlayCount = currentSong.getTimesPlayed();
        
        // Initial points for user
        double initialUserPoints = premiumUser.getPontos();
        
        // Play the album
        album.play(premiumUser);
        
        // Verify play count incremented
        assertEquals(initialPlayCount + 1, currentSong.getTimesPlayed());
        
        // Verify user points increased
        assertTrue(premiumUser.getPontos() > initialUserPoints);
        
        // Verify song added to user history
        assertEquals(1, premiumUser.getHistory().size());
        assertEquals(currentSong.getName(), premiumUser.getHistory().get(0).getSong().getName());
    }

    @Test
    void testToString() {
        String albumString = album.toString();
        
        // Verify toString contains important album information
        assertTrue(albumString.contains("Test Album"));
        assertTrue(albumString.contains("Artist1"));
        assertTrue(albumString.contains("2022"));
        assertTrue(albumString.contains("Mixed"));
    }
}