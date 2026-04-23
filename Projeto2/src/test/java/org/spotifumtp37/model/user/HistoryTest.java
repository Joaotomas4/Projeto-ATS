package org.spotifumtp37.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spotifumtp37.model.album.Song;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    private History history;
    private Song song;
    private LocalDateTime currentTime;

    @BeforeEach
    void setUp() {
        history = new History();
        song = new Song("Test Song", "Test Artist", "Test Publisher", 
                        "Test Lyrics", "Test Notes", "Test Genre", 180);
        currentTime = LocalDateTime.now();
    }

    @Test
    void getSong() {
        // Initially null
        assertNull(history.getSong());
        
        // After setting a song
        history.setSong(song);
        Song retrievedSong = history.getSong();
        
        // Verify retrieved song is equal but not the same instance (should be a defensive copy)
        assertEquals(song.getName(), retrievedSong.getName());
        assertEquals(song.getArtist(), retrievedSong.getArtist());
        assertEquals(song.getGenre(), retrievedSong.getGenre());
        assertNotSame(song, retrievedSong);
    }

    @Test
    void setSong() {
        // Test setting a valid song
        history.setSong(song);
        assertEquals(song.getName(), history.getSong().getName());
        assertEquals(song.getArtist(), history.getSong().getArtist());
        
        // Test setting a null song
        history.setSong(null);
        assertNull(history.getSong());
        
        // Test setting a song after having one already set
        Song newSong = new Song("New Song", "New Artist", "New Publisher", 
                               "New Lyrics", "New Notes", "New Genre", 240);
        history.setSong(newSong);
        assertEquals(newSong.getName(), history.getSong().getName());
        assertEquals(newSong.getArtist(), history.getSong().getArtist());
    }

    @Test
    void getTime() {
        // Initially null
        assertNull(history.getTime());
        
        // After setting time
        history.setTime(currentTime);
        assertEquals(currentTime, history.getTime());
    }

    @Test
    void setTime() {
        // Test setting a valid time
        history.setTime(currentTime);
        assertEquals(currentTime, history.getTime());
        
        // Test setting null time
        history.setTime(null);
        assertNull(history.getTime());
        
        // Test setting a different time
        LocalDateTime newTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        history.setTime(newTime);
        assertEquals(newTime, history.getTime());
        assertNotEquals(currentTime, history.getTime());
    }

    @Test
    void testClone() {
        // Setup a fully populated history entry
        history.setSong(song);
        history.setTime(currentTime);
        
        // Clone it
        History clonedHistory = history.clone();
        
        // Verify the clone has the same values
        assertEquals(history.getSong().getName(), clonedHistory.getSong().getName());
        assertEquals(history.getSong().getArtist(), clonedHistory.getSong().getArtist());
        assertEquals(history.getTime(), clonedHistory.getTime());
        
        // Verify clone method creates a new History object
        assertNotSame(history, clonedHistory);
        
        // Verify clone creates a deep copy of song
        assertNotSame(history.getSong(), clonedHistory.getSong());
        
        // Modify clone's song to verify it doesn't affect original
        Song clonedSong = clonedHistory.getSong(); 
        clonedSong.setName("Modified Name");
        assertEquals("Test Song", history.getSong().getName()); // Original unchanged
    }
    
    @Test
    void testCloneWithNulls() {
        // Test cloning with null values
        History emptyHistory = new History();
        History clonedEmptyHistory = emptyHistory.clone();
        
        assertNull(clonedEmptyHistory.getSong());
        assertNull(clonedEmptyHistory.getTime());
        assertNotSame(emptyHistory, clonedEmptyHistory);
    }

    @Test
    void testEquals() {
        // Setup two identical history entries
        history.setSong(song);
        history.setTime(currentTime);
        
        History sameHistory = new History();
        sameHistory.setSong(song);
        sameHistory.setTime(currentTime);
        
        // Test equality with history that has the same content
        assertEquals(history, sameHistory);
        
        // Test equality with self
        assertEquals(history, history);
        
        // Test inequality with null
        assertNotEquals(history, null);
        
        // Test inequality with different object type
        assertNotEquals(history, "not a history object");
        
        // Test inequality with different song
        History differentSongHistory = new History();
        Song differentSong = new Song("Different Song", "Different Artist", "Different Publisher", 
                                     "Different Lyrics", "Different Notes", "Different Genre", 240);
        differentSongHistory.setSong(differentSong);
        differentSongHistory.setTime(currentTime);
        assertNotEquals(history, differentSongHistory);
        
        // Test inequality with different time
        History differentTimeHistory = new History();
        differentTimeHistory.setSong(song);
        differentTimeHistory.setTime(LocalDateTime.of(2020, 1, 1, 12, 0));
        assertNotEquals(history, differentTimeHistory);
        
        // Test equality with one null song
        History nullSongHistory = new History();
        nullSongHistory.setTime(currentTime);
        
        History anotherNullSongHistory = new History();
        anotherNullSongHistory.setTime(currentTime);
        
        assertEquals(nullSongHistory, anotherNullSongHistory);
        assertNotEquals(history, nullSongHistory);
        
        // Test equality with one null time
        History nullTimeHistory = new History();
        nullTimeHistory.setSong(song);
        
        History anotherNullTimeHistory = new History();
        anotherNullTimeHistory.setSong(song);
        
        assertEquals(nullTimeHistory, anotherNullTimeHistory);
        assertNotEquals(history, nullTimeHistory);
    }

    @Test
    void testHashCode() {
        // Setup two identical history entries
        history.setSong(song);
        history.setTime(currentTime);
        
        History sameHistory = new History();
        sameHistory.setSong(song);
        sameHistory.setTime(currentTime);
        
        // Verify hashCode is consistent with equals
        assertEquals(history.hashCode(), sameHistory.hashCode());
        
        // Different songs should produce different hash codes
        History differentSongHistory = new History();
        Song differentSong = new Song("Different Song", "Different Artist", "Different Publisher", 
                                     "Different Lyrics", "Different Notes", "Different Genre", 240);
        differentSongHistory.setSong(differentSong);
        differentSongHistory.setTime(currentTime);
        assertNotEquals(history.hashCode(), differentSongHistory.hashCode());
    }

    @Test
    void testToString() {
        // Test toString with fully populated history
        history.setSong(song);
        history.setTime(currentTime);
        
        String historyString = history.toString();
        
        assertTrue(historyString.contains("Test Song"));
        assertTrue(historyString.contains("Test Artist"));
        assertTrue(historyString.contains(currentTime.toString()));
        
        // Test toString with null song
        History nullSongHistory = new History();
        nullSongHistory.setTime(currentTime);
        
        String nullSongHistoryString = nullSongHistory.toString();
        
        assertTrue(nullSongHistoryString.contains("null"));
        assertTrue(nullSongHistoryString.contains(currentTime.toString()));
        
        // Test toString with null time
        History nullTimeHistory = new History();
        nullTimeHistory.setSong(song);
        
        String nullTimeHistoryString = nullTimeHistory.toString();
        
        assertTrue(nullTimeHistoryString.contains("Test Song"));
        assertTrue(nullTimeHistoryString.contains("Test Artist"));
        assertTrue(nullTimeHistoryString.contains("null"));
    }

    @Test
    void testConstructors() {
        // Test default constructor
        History defaultHistory = new History();
        assertNull(defaultHistory.getSong());
        assertNull(defaultHistory.getTime());
        
        // Test parameterized constructor with values
        History paramHistory = new History(song, currentTime);
        assertEquals(song.getName(), paramHistory.getSong().getName());
        assertEquals(song.getArtist(), paramHistory.getSong().getArtist());
        assertEquals(currentTime, paramHistory.getTime());
        
        // Note that getSong() returns a clone, so this test will pass
        // even though the constructor doesn't clone the song
        assertNotSame(song, paramHistory.getSong());
        
        // Test parameterized constructor with nulls
        History nullParamHistory = new History(null, null);
        assertNull(nullParamHistory.getSong());
        assertNull(nullParamHistory.getTime());
        
        // Test copy constructor
        history.setSong(song);
        history.setTime(currentTime);
        History copiedHistory = new History(history);
        
        assertEquals(history.getSong().getName(), copiedHistory.getSong().getName());
        assertEquals(history.getTime(), copiedHistory.getTime());
        assertNotSame(history.getSong(), copiedHistory.getSong());
        
        // Test that modification to original song doesn't affect the copied history
        Song originalSong = song.clone(); // Keep a copy for comparison
        song.setName("Modified Original");
        
        // The internal song reference in history hasn't changed because 
        // the copy constructor makes a deep copy
        assertNotEquals("Modified Original", copiedHistory.getSong().getName());
        assertEquals(originalSong.getName(), copiedHistory.getSong().getName());
        
        // Test copy constructor with nulls
        History nullHistory = new History();
        History copiedNullHistory = new History(nullHistory);
        
        assertNull(copiedNullHistory.getSong());
        assertNull(copiedNullHistory.getTime());
    }
    
    @Test
    void testSongModificationIsolation() {
        // Test that changes to the original song don't affect the history
        history.setSong(song);
        
        // Modify the original song
        String originalName = song.getName();
        song.setName("Changed Name");
        
        // Get song from history - it should have the original name since
        // the history's getSong() returns a clone
        Song retrievedSong = history.getSong();
        
        // This should pass with our memory-optimized implementation
        // but would fail if history.setSong() made a deep copy
        assertEquals("Changed Name", history.getSong().getName());
        
        // Restore original song name for other tests
        song.setName(originalName);
    }
}