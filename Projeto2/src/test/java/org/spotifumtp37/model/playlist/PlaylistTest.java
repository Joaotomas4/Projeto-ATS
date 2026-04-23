package org.spotifumtp37.model.playlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.exceptions.SubscriptionDoesNotAllowException;
import org.spotifumtp37.model.subscription.FreePlan;
import org.spotifumtp37.model.subscription.PremiumBase;
import org.spotifumtp37.model.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {

    private Playlist playlist;
    private User creator;
    private User premiumUser;
    private User freeUser;
    private Song song1;
    private Song song2;
    private Song song3;
    private List<Song> songList;

    @BeforeEach
    void setUp() {
        // Create users with different subscription plans
        creator = new User("Creator", "creator@mail.com", "123 Creator St", new PremiumBase(), "password", 0, new ArrayList<>());
        premiumUser = new User("Premium", "premium@mail.com", "123 Premium St", new PremiumBase(), "password", 0, new ArrayList<>());
        freeUser = new User("Free", "free@mail.com", "123 Free St", new FreePlan(), "password", 0, new ArrayList<>());
        
        // Create test songs
        song1 = new Song("Song1", "Artist1", "Publisher1", "Lyrics1", "Notes1", "Rock", 180);
        song2 = new Song("Song2", "Artist2", "Publisher2", "Lyrics2", "Notes2", "Pop", 200);
        song3 = new Song("Song3", "Artist3", "Publisher3", "Lyrics3", "Notes3", "Jazz", 220);
        
        // Create song list
        songList = new ArrayList<>();
        songList.add(song1);
        songList.add(song2);
        songList.add(song3);
        
        // Create playlist
        playlist = new Playlist(creator, "Test Playlist", "A test playlist", 0, "public", songList);
    }

    @Test
    void testClone() {
        Playlist cloned = playlist.clone();
        
        // Verify it's a different object
        assertNotSame(playlist, cloned);
        
        // Verify attributes are equal
        assertEquals(playlist.getPlaylistName(), cloned.getPlaylistName());
        assertEquals(playlist.getPlaylistDescription(), cloned.getPlaylistDescription());
        assertEquals(playlist.getNumberOfFollowers(), cloned.getNumberOfFollowers());
        assertEquals(playlist.getStatus(), cloned.getStatus());
        assertEquals(playlist.getSongs().size(), cloned.getSongs().size());
        
        // Verify changing clone doesn't affect original
        cloned.setPlaylistName("Changed Name");
        assertNotEquals(playlist.getPlaylistName(), cloned.getPlaylistName());
    }

    @Test
    void testEquals() {
        // Same playlist equals itself
        assertEquals(playlist, playlist);
        
        // Create a new playlist with the same properties
        Playlist samePropPlaylist = new Playlist(creator, "Test Playlist", "A test playlist", 
                                         0, "public", songList);
        // Instead of testing with clone, explicitly set the current song to be the same
        samePropPlaylist.setCurrentSong(playlist.getCurrentSong());
        assertEquals(playlist, samePropPlaylist);
        
        // Different playlist
        Playlist different = new Playlist(creator, "Different Playlist", "Different description", 
                                   10, "private", songList);
        assertNotEquals(playlist, different);
        
        // Null and different type
        assertNotEquals(playlist, null);
        assertNotEquals(playlist, "Not a playlist");
    }

    @Test
    void testToString() {
        String result = playlist.toString();
        
        // Check that toString contains key information
        assertTrue(result.contains("Test Playlist"));
        assertTrue(result.contains("A test playlist"));
        assertTrue(result.contains("public"));
    }

    @Test
    void getCreator() {
        assertEquals(creator, playlist.getCreator());
    }

    @Test
    void getPlaylistName() {
        assertEquals("Test Playlist", playlist.getPlaylistName());
    }

    @Test
    void getPlaylistDescription() {
        assertEquals("A test playlist", playlist.getPlaylistDescription());
    }

    @Test
    void getNumberOfFollowers() {
        assertEquals(0, playlist.getNumberOfFollowers());
    }

    @Test
    void getStatus() {
        assertEquals("public", playlist.getStatus());
    }

    @Test
    void getSongs() {
        List<Song> retrievedSongs = playlist.getSongs();
        
        // Check size
        assertEquals(3, retrievedSongs.size());
        
        // Check contents
        assertTrue(retrievedSongs.contains(song1));
        assertTrue(retrievedSongs.contains(song2));
        assertTrue(retrievedSongs.contains(song3));
        
        // Verify it's a defensive copy (modifying returned list shouldn't affect playlist)
        retrievedSongs.clear();
        assertEquals(3, playlist.getSongs().size());
    }

    @Test
    void getCurrentSong() {
        Song currentSong = playlist.getCurrentSong();
        
        // Verify current song is one of the songs in the playlist
        assertTrue(songList.contains(currentSong));
    }

    @Test
    void setCreatorUsername() {
        User newCreator = new User("NewCreator", "new@mail.com", "456 New St", new PremiumBase(), "newpass", 0, new ArrayList<>());
        playlist.setCreatorUsername(newCreator);
        assertEquals(newCreator, playlist.getCreator());
    }

    @Test
    void setPlaylistName() {
        playlist.setPlaylistName("New Name");
        assertEquals("New Name", playlist.getPlaylistName());
    }

    @Test
    void setPlaylistDescription() {
        playlist.setPlaylistDescription("New description");
        assertEquals("New description", playlist.getPlaylistDescription());
    }

    @Test
    void setNumberOfFollowers() {
        playlist.setNumberOfFollowers(100);
        assertEquals(100, playlist.getNumberOfFollowers());
    }

    @Test
    void setStatus() {
        // Change to private
        playlist.setStatus("private");
        assertEquals("private", playlist.getStatus());
        assertTrue(playlist.isPrivate());
        assertFalse(playlist.isPublic());
        
        // Change back to public
        playlist.setStatus("public");
        assertEquals("public", playlist.getStatus());
        assertFalse(playlist.isPrivate());
        assertTrue(playlist.isPublic());
    }

    @Test
    void setSongs() {
        List<Song> newSongs = new ArrayList<>();
        Song newSong = new Song("NewSong", "NewArtist", "NewPublisher",
                "NewLyrics", "NewNotes", "NewGenre", 240);
        newSongs.add(newSong);

        playlist.setSongs(newSongs);

        // setSongs REPLACES the existing songs (clears first, then adds)
        List<Song> updatedSongs = playlist.getSongs();
        assertEquals(1, updatedSongs.size());           // só a nova música
        assertEquals("NewSong", updatedSongs.get(0).getName());
    }

    @Test
    void setCurrentSong() {
        Song newCurrentSong = new Song("Current", "CurrentArtist", "CurrentPublisher", "CurrentLyrics", "CurrentNotes", "CurrentGenre", 300);
        playlist.setCurrentSong(newCurrentSong);
        assertEquals(newCurrentSong, playlist.getCurrentSong());
    }

    @Test
    void isPrivate() {
        // Initially public
        assertFalse(playlist.isPrivate());
        
        // Set to private
        playlist.setStatus("private");
        assertTrue(playlist.isPrivate());
        
        // Case insensitivity check
        playlist.setStatus("PRIVATE");
        assertTrue(playlist.isPrivate());
    }

    @Test
    void isPublic() {
        // Initially public
        assertTrue(playlist.isPublic());
        
        // Set to private
        playlist.setStatus("private");
        assertFalse(playlist.isPublic());
        
        // Case insensitivity check
        playlist.setStatus("PUBLIC");
        assertTrue(playlist.isPublic());
    }

    @Test
    void play() {
        // Initial plays count
        int initialPlays = song1.getTimesPlayed();
        
        // Set song1 as current song
        playlist.setCurrentSong(song1);
        
        // Play the playlist
        playlist.play(premiumUser);
        
        // Verify song's play count increased
        assertEquals(initialPlays + 1, song1.getTimesPlayed());
    }

    @Test
    void next_PremiumUser() {
        // Set current song
        playlist.setCurrentSong(song1);
        Song initialSong = playlist.getCurrentSong();
        
        // Call next with premium user
        playlist.next(premiumUser);
        
        // Should move to next song sequentially
        assertNotEquals(initialSong, playlist.getCurrentSong());
        assertEquals(song2, playlist.getCurrentSong());
        
        // Test wraparound
        playlist.setCurrentSong(song3);
        playlist.next(premiumUser);
        assertEquals(song1, playlist.getCurrentSong());
    }

    @Test
    void next_FreeUser() {
        // Set current song
        playlist.setCurrentSong(song1);
        Song initialSong = playlist.getCurrentSong();
        
        // Call next with free user
        playlist.next(freeUser);
        
        // Should change to a random different song
        assertNotEquals(initialSong, playlist.getCurrentSong());
    }

    @Test
    void nextShuffle() {
        // Set current song
        playlist.setCurrentSong(song1);
        Song initialSong = playlist.getCurrentSong();
        
        // Call nextShuffle
        playlist.nextShuffle();
        
        // Should change to a different song
        assertNotEquals(initialSong, playlist.getCurrentSong());
    }

    @Test
    void previous_PremiumUser() throws SubscriptionDoesNotAllowException {
        // Set current song to song2
        playlist.setCurrentSong(song2);
        
        // Call previous with premium user
        playlist.previous(premiumUser);
        
        // Should go to previous song
        assertEquals(song1, playlist.getCurrentSong());
        
        // Test wraparound
        playlist.setCurrentSong(song1);
        playlist.previous(premiumUser);
        assertEquals(song3, playlist.getCurrentSong());
    }

    @Test
    void previous_FreeUser() {
        // Set current song
        playlist.setCurrentSong(song2);
        
        // Call previous with free user - should throw exception
        assertThrows(SubscriptionDoesNotAllowException.class, () -> {
            playlist.previous(freeUser);
        });
    }

    @Test
    void addSong_Success() throws SubscriptionDoesNotAllowException {
        // Create new song
        Song newSong = new Song("NewSong", "NewArtist", "NewPublisher", "NewLyrics", "NewNotes", "NewGenre", 250);
        
        // Add to playlist
        playlist.addSong(newSong);
        
        // Verify it was added
        List<Song> songs = playlist.getSongs();
        assertEquals(4, songs.size());
        assertTrue(songs.contains(newSong));
    }

    @Test
    void addSong_AlreadyExists() {
        // Try to add song1 which is already in the playlist
        assertThrows(UnsupportedOperationException.class, () -> {
            playlist.addSong(song1);
        });
    }

    @Test
    void addSong_SubscriptionDoesNotAllow() {
        // Create user with subscription that doesn't allow creating playlists
        User restrictedUser = new User("Restricted", "restricted@mail.com", "123 Restricted St", new FreePlan() {
            @Override
            public boolean canCreatePlaylist() {
                return false;
            }
        }, "password", 0, new ArrayList<>());
        
        // Set as creator
        playlist.setCreatorUsername(restrictedUser);
        
        // Try to add a song
        Song newSong = new Song("NewSong", "NewArtist", "NewPublisher", "NewLyrics", "NewNotes", "NewGenre", 250);
        
        assertThrows(SubscriptionDoesNotAllowException.class, () -> {
            playlist.addSong(newSong);
        });
    }

    @Test
    void deleteSong_Success() throws SubscriptionDoesNotAllowException {
        // Delete song1
        playlist.deleteSong(song1);
        
        // Verify it was removed
        List<Song> songs = playlist.getSongs();
        assertEquals(2, songs.size());
        assertFalse(songs.contains(song1));
    }

    @Test
    void deleteSong_DoesNotExist() {
        // Create song that's not in playlist
        Song nonExistentSong = new Song("NonExistent", "NonExistentArtist", "NonExistentPublisher", 
                                       "NonExistentLyrics", "NonExistentNotes", "NonExistentGenre", 300);
        
        // Try to delete it
        assertThrows(UnsupportedOperationException.class, () -> {
            playlist.deleteSong(nonExistentSong);
        });
    }

    @Test
    void deleteSong_SubscriptionDoesNotAllow() {
        // Create user with subscription that doesn't allow creating playlists
        User restrictedUser = new User("Restricted", "restricted@mail.com", "123 Restricted St", new FreePlan() {
            @Override
            public boolean canCreatePlaylist() {
                return false;
            }
        }, "password", 0, new ArrayList<>());
        
        // Set as creator
        playlist.setCreatorUsername(restrictedUser);
        
        // Try to delete a song
        assertThrows(SubscriptionDoesNotAllowException.class, () -> {
            playlist.deleteSong(song1);
        });
    }

    @Test
    void constructor_WithParameters() {
        // Test parameterized constructor
        Playlist paramPlaylist = new Playlist(creator, "Param Playlist", "Param Description", 10, "private", songList);
        
        assertEquals(creator, paramPlaylist.getCreator());
        assertEquals("Param Playlist", paramPlaylist.getPlaylistName());
        assertEquals("Param Description", paramPlaylist.getPlaylistDescription());
        assertEquals(10, paramPlaylist.getNumberOfFollowers());
        assertEquals("private", paramPlaylist.getStatus());
        assertEquals(3, paramPlaylist.getSongs().size());
        
        // Verify a current song was selected
        assertNotNull(paramPlaylist.getCurrentSong());
        assertTrue(songList.contains(paramPlaylist.getCurrentSong()));
    }
    
    @Test
    void constructor_Copy() {
        // Test copy constructor
        Playlist copyPlaylist = new Playlist(playlist);
        
        assertEquals(playlist.getCreator(), copyPlaylist.getCreator());
        assertEquals(playlist.getPlaylistName(), copyPlaylist.getPlaylistName());
        assertEquals(playlist.getPlaylistDescription(), copyPlaylist.getPlaylistDescription());
        assertEquals(playlist.getNumberOfFollowers(), copyPlaylist.getNumberOfFollowers());
        assertEquals(playlist.getStatus(), copyPlaylist.getStatus());
        assertEquals(playlist.getSongs().size(), copyPlaylist.getSongs().size());
        
        // Verify a current song was selected
        assertNotNull(copyPlaylist.getCurrentSong());
        assertTrue(songList.contains(copyPlaylist.getCurrentSong()));
    }
    
    @Test
    void testSingleSongBehavior() {
        // Create playlist with only one song
        List<Song> singleSongList = new ArrayList<>();
        singleSongList.add(song1);
        Playlist singleSongPlaylist = new Playlist(creator, "Single Song", "Only one song", 0, "public", singleSongList);
        
        // Verify current song is the only song
        assertEquals(song1, singleSongPlaylist.getCurrentSong());
        
        // Test next with a single song - shouldn't change current song
        singleSongPlaylist.next(premiumUser);
        assertEquals(song1, singleSongPlaylist.getCurrentSong());
        
        // Test shuffle with a single song - shouldn't change current song
        singleSongPlaylist.nextShuffle();
        assertEquals(song1, singleSongPlaylist.getCurrentSong());
    }
    
}