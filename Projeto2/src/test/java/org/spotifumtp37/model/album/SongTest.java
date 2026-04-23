package org.spotifumtp37.model.album;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    private Song song;
    private Song explicitSong;
    private MultimediaSong multimediaSong;

    @BeforeEach
    void setUp() {
        song = new Song("Halo", "Beyonce", "Columbia", "Remember those walls...", "C G Am F", "Pop", 240);
        explicitSong = new ExplicitSong("Explicit Song", "Eminem", "Interscope", "Explicit lyrics here", "Dm G C", "Hip Hop", 180);
        multimediaSong = new MultimediaSong("Video Song", "Madonna", "Warner", "Video song lyrics", "A E F", "Pop", 210, "https://example.com/video");
    }

    @Test
    void getName() {
        assertEquals("Halo", song.getName());
        assertEquals("Explicit Song", explicitSong.getName());
        assertEquals("Video Song", multimediaSong.getName());
    }

    @Test
    void setName() {
        song.setName("Single Ladies");
        assertEquals("Single Ladies", song.getName());
        
        song.setName("");
        assertEquals("", song.getName()); // Testing edge case with empty string
        
        song.setName(null);
        assertNull(song.getName()); // Testing edge case with null value
    }

    @Test
    void getArtist() {
        assertEquals("Beyonce", song.getArtist());
        assertEquals("Eminem", explicitSong.getArtist());
        assertEquals("Madonna", multimediaSong.getArtist());
    }

    @Test
    void setArtist() {
        song.setArtist("Adele");
        assertEquals("Adele", song.getArtist());
        
        song.setArtist("");
        assertEquals("", song.getArtist()); // Testing with empty string
        
        song.setArtist(null);
        assertNull(song.getArtist()); // Testing with null value
    }

    @Test
    void getPublisher() {
        assertEquals("Columbia", song.getPublisher());
        assertEquals("Interscope", explicitSong.getPublisher());
        assertEquals("Warner", multimediaSong.getPublisher());
    }

    @Test
    void setPublisher() {
        song.setPublisher("Sony");
        assertEquals("Sony", song.getPublisher());
        
        song.setPublisher("");
        assertEquals("", song.getPublisher()); // Testing with empty string
        
        song.setPublisher(null);
        assertNull(song.getPublisher()); // Testing with null value
    }

    @Test
    void getLyrics() {
        assertEquals("Remember those walls...", song.getLyrics());
        assertEquals("Explicit lyrics here", explicitSong.getLyrics());
        assertEquals("Video song lyrics", multimediaSong.getLyrics());
    }

    @Test
    void setLyrics() {
        song.setLyrics("Hello, it's me...");
        assertEquals("Hello, it's me...", song.getLyrics());
        
        song.setLyrics("");
        assertEquals("", song.getLyrics()); // Testing with empty string
        
        song.setLyrics(null);
        assertNull(song.getLyrics()); // Testing with null value
    }

    @Test
    void getMusicalNotes() {
        assertEquals("C G Am F", song.getMusicalNotes());
        assertEquals("Dm G C", explicitSong.getMusicalNotes());
        assertEquals("A E F", multimediaSong.getMusicalNotes());
    }

    @Test
    void setMusicalNotes() {
        song.setMusicalNotes("F#m D A E");
        assertEquals("F#m D A E", song.getMusicalNotes());
        
        song.setMusicalNotes("");
        assertEquals("", song.getMusicalNotes()); // Testing with empty string
        
        song.setMusicalNotes(null);
        assertNull(song.getMusicalNotes()); // Testing with null value
    }

    @Test
    void getGenre() {
        assertEquals("Pop", song.getGenre());
        assertEquals("Hip Hop", explicitSong.getGenre());
        assertEquals("Pop", multimediaSong.getGenre());
    }

    @Test
    void setGenre() {
        song.setGenre("Rock");
        assertEquals("Rock", song.getGenre());
        
        song.setGenre("");
        assertEquals("", song.getGenre()); // Testing with empty string
        
        song.setGenre(null);
        assertNull(song.getGenre()); // Testing with null value
    }

    @Test
    void getDurationInSeconds() {
        assertEquals(240, song.getDurationInSeconds());
        assertEquals(180, explicitSong.getDurationInSeconds());
        assertEquals(210, multimediaSong.getDurationInSeconds());
    }

    @Test
    void setDurationInSeconds() {
        song.setDurationInSeconds(180);
        assertEquals(180, song.getDurationInSeconds());
        
        song.setDurationInSeconds(0);
        assertEquals(0, song.getDurationInSeconds()); // Testing edge case with zero
        
        song.setDurationInSeconds(-10);
        assertEquals(-10, song.getDurationInSeconds()); // Testing edge case with negative value
        // Note: In a production system, you might want validation to prevent negative durations
    }

    @Test
    void getTimesPlayed() {
        assertEquals(0, song.getTimesPlayed()); // Default should be 0
        assertEquals(0, explicitSong.getTimesPlayed());
        assertEquals(0, multimediaSong.getTimesPlayed());
    }

    @Test
    void setTimesPlayed() {
        song.setTimesPlayed(5);
        assertEquals(5, song.getTimesPlayed());
        
        song.setTimesPlayed(0);
        assertEquals(0, song.getTimesPlayed()); // Testing with zero
        
        song.setTimesPlayed(-1);
        assertEquals(-1, song.getTimesPlayed()); // Testing with negative value
        // Note: In a production system, validation might prevent negative play counts
    }

    @Test
    void incrementTimesPlayed() {
        // Initial count should be 0
        assertEquals(0, song.getTimesPlayed());
        
        // Increment once
        song.incrementTimesPlayed();
        assertEquals(1, song.getTimesPlayed());
        
        // Increment again
        song.incrementTimesPlayed();
        assertEquals(2, song.getTimesPlayed());
        
        // Set to a specific value then increment
        song.setTimesPlayed(10);
        song.incrementTimesPlayed();
        assertEquals(11, song.getTimesPlayed());
    }

    @Test
    void isExplicit() {
        assertFalse(song.isExplicit());
        assertTrue(explicitSong.isExplicit());
        assertFalse(multimediaSong.isExplicit());
    }

    @Test
    void isMultimedia() {
        assertFalse(song.isMultimedia());
        assertFalse(explicitSong.isMultimedia());
        assertTrue(multimediaSong.isMultimedia());
    }

    @Test
    void getVideoLink() {
        // Only MultimediaSong should have a video link
        assertTrue(multimediaSong instanceof MultimediaSong);
        assertEquals("https://example.com/video", ((MultimediaSong) multimediaSong).getVideoLink());
    }

    @Test
    void testEquals() {
        // Test equality with identical song
        Song copy = new Song(song);
        assertEquals(song, copy);
        
        // Test equality with same data but different object
        Song sameSong = new Song("Halo", "Beyonce", "Columbia", "Remember those walls...", "C G Am F", "Pop", 240);
        assertEquals(song, sameSong);
        
        // Test inequality with different song
        Song diff = new Song("Other", "Other", "Other", "", "", "Pop", 240);
        assertNotEquals(song, diff);
        
        // Test inequality with null
        assertNotEquals(song, null);
        
        // Test inequality with different object type
        assertNotEquals(song, "notASong");
        
        // Test equality with self
        assertEquals(song, song);
        
        // Test equality with songs that differ in one property
        Song diffName = new Song("Different", "Beyonce", "Columbia", "Remember those walls...", "C G Am F", "Pop", 240);
        assertNotEquals(song, diffName);
        
        Song diffArtist = new Song("Halo", "Different", "Columbia", "Remember those walls...", "C G Am F", "Pop", 240);
        assertNotEquals(song, diffArtist);
        
        Song diffPublisher = new Song("Halo", "Beyonce", "Different", "Remember those walls...", "C G Am F", "Pop", 240);
        assertNotEquals(song, diffPublisher);
        
        Song diffDuration = new Song("Halo", "Beyonce", "Columbia", "Remember those walls...", "C G Am F", "Pop", 300);
        assertNotEquals(song, diffDuration);
    }

    @Test
    void testHashCode() {
        // Same songs should have same hash code
        Song copy = new Song(song);
        assertEquals(song.hashCode(), copy.hashCode());
        
        // Different songs should have different hash codes
        Song diff = new Song("Other", "Other", "Other", "", "", "Other", 120);
        assertNotEquals(song.hashCode(), diff.hashCode());
        
        // Same key properties (name, artist, duration) should produce same hash
        Song sameKey = new Song("Halo", "Beyonce", "Different", "Different", "Different", "Different", 240);
        assertEquals(song.hashCode(), sameKey.hashCode());
        
        // If one key property differs, hash should differ
        Song diffKey = new Song("Halo", "Different", "Columbia", "Remember those walls...", "C G Am F", "Pop", 240);
        assertNotEquals(song.hashCode(), diffKey.hashCode());
    }

    @Test
    void testClone() {
        // Test regular song cloning
        Song cloned = song.clone();
        assertEquals(song, cloned);
        assertNotSame(song, cloned);
        
        // Test explicit song cloning
        Song clonedExplicit = explicitSong.clone();
        assertEquals(explicitSong, clonedExplicit);
        assertNotSame(explicitSong, clonedExplicit);
        assertTrue(clonedExplicit.isExplicit());
        
        // Test multimedia song cloning
        Song clonedMultimedia = multimediaSong.clone();
        assertEquals(multimediaSong, clonedMultimedia);
        assertNotSame(multimediaSong, clonedMultimedia);
        assertTrue(clonedMultimedia.isMultimedia());
        assertEquals(((MultimediaSong) multimediaSong).getVideoLink(), ((MultimediaSong) clonedMultimedia).getVideoLink());
    }

    @Test
    void testToString() {
        String str = song.toString();
        
        // ToString should contain important song information
        assertTrue(str.contains("name='Halo'"));
        assertTrue(str.contains("artist='Beyonce'"));
        assertTrue(str.contains("publisher='Columbia'"));
        assertTrue(str.contains("durationInSeconds=240"));
        assertTrue(str.contains("timesPlayed=0"));
        
        // Test toString for explicit song
        String explicitStr = explicitSong.toString();
        assertTrue(explicitStr.contains("name='Explicit Song'"));
        assertTrue(explicitStr.contains("artist='Eminem'"));
        
        // Test toString for multimedia song 
        String multimediaStr = multimediaSong.toString();
        assertTrue(multimediaStr.contains("name='Video Song'"));
        assertTrue(multimediaStr.contains("artist='Madonna'"));
    }

    @Test
    void testCopyConstructor() {
        // Test copy constructor with regular song
        Song copiedSong = new Song(song);
        assertEquals(song.getName(), copiedSong.getName());
        assertEquals(song.getArtist(), copiedSong.getArtist());
        assertEquals(song.getPublisher(), copiedSong.getPublisher());
        assertEquals(song.getLyrics(), copiedSong.getLyrics());
        assertEquals(song.getMusicalNotes(), copiedSong.getMusicalNotes());
        assertEquals(song.getGenre(), copiedSong.getGenre());
        assertEquals(song.getDurationInSeconds(), copiedSong.getDurationInSeconds());
        assertEquals(song.getTimesPlayed(), copiedSong.getTimesPlayed());
        assertNotSame(song, copiedSong);
        
        // Test copy constructor with explicit song
        ExplicitSong copiedExplicit = new ExplicitSong((ExplicitSong) explicitSong);
        assertEquals(explicitSong.getName(), copiedExplicit.getName());
        assertEquals(explicitSong.getArtist(), copiedExplicit.getArtist());
        assertTrue(copiedExplicit.isExplicit());
        assertNotSame(explicitSong, copiedExplicit);
        
        // Test copy constructor with multimedia song
        MultimediaSong copiedMultimedia = new MultimediaSong(multimediaSong);
        assertEquals(multimediaSong.getName(), copiedMultimedia.getName());
        assertEquals(multimediaSong.getArtist(), copiedMultimedia.getArtist());
        assertTrue(copiedMultimedia.isMultimedia());
        assertEquals(((MultimediaSong) multimediaSong).getVideoLink(), copiedMultimedia.getVideoLink());
        assertNotSame(multimediaSong, copiedMultimedia);
    }

    @Test
    void testDefaultConstructor() {
        // Test default constructor
        Song defaultSong = new Song();
        assertEquals("", defaultSong.getName());
        assertEquals("", defaultSong.getArtist());
        assertEquals("", defaultSong.getPublisher());
        assertEquals("", defaultSong.getLyrics());
        assertEquals("", defaultSong.getMusicalNotes());
        assertEquals("", defaultSong.getGenre());
        assertEquals(0, defaultSong.getDurationInSeconds());
        assertEquals(0, defaultSong.getTimesPlayed());
    }
}