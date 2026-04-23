package org.spotifumtp37.delegate;

import org.spotifumtp37.model.album.MultimediaSong;
import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.exceptions.SubscriptionDoesNotAllowException;
import org.spotifumtp37.model.playlist.Playable;
import org.spotifumtp37.model.user.User;

import java.util.Scanner;
import java.io.IOException;

public class PlayerUI {

    private final Scanner scanner;

    public PlayerUI(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Play a "playable": shows lyrics and reacts to next/prev/stop.
     */
    public void playSong(Playable playable, User user) throws IOException {
        boolean continuePlaying = true;

        while (continuePlaying) {

            playable.play(user);

            Song song = playable.getCurrentSong();
            System.out.printf("Now playing \"%s\" by %s%n", song.getName(), song.getArtist());
            if (song.isExplicit()) {
                System.out.println("Attention! This is an explicit song!");
            }

            if (song.isMultimedia()) {
                System.out.println("This song has a music video!");
                System.out.println("You can watch it at " + ((MultimediaSong) song).getVideoLink());
            }

            System.out.println("Lyrics:\n" + song.getLyrics());
            System.out.println("\nControls: ");
            System.out.print("n=next, ");
            if (user.getSubscriptionPlan().canBrowsePlaylist()) {
                System.out.print("p=prev, ");
                System.out.print("r=shuffle random song, ");
            }
            System.out.print("s=stop\n");
            boolean waitingForInput = true;
            while (waitingForInput) {
                System.out.print("Command: ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;
                char cmd = input.charAt(0);
                switch (cmd) {
                    case 'n': // next
                        playable.next(user);
                        System.out.println("Skipping to next...");
                        waitingForInput = false;
                        break;
                    case 'r': // nextshuffle (para users premium darem shuffle)
                        playable.nextShuffle();
                        System.out.println("Skipping to next on shuffle mode...");
                        waitingForInput = false;
                        break;
                    case 'p': // prev
                        try {
                            playable.previous(user);
                            System.out.println("Going to previous...");
                        } catch (SubscriptionDoesNotAllowException e) {
                            System.out.println("Cannot go back." + e.getMessage());
                        }
                        waitingForInput = false;
                        break;
                    case 's': // stop
                        System.out.println("Stopped.");
                        continuePlaying = false;
                        waitingForInput = false;
                        break;
                    default:
                        System.out.println("Unknown command. Try n (next), p (prev), s (stop).");
                }
            }
            System.out.println("Track finished.\n");

        }
    }
}