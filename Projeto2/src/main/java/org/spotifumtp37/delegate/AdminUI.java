package org.spotifumtp37.delegate;

import org.spotifumtp37.model.SpotifUMData;
import org.spotifumtp37.model.album.Album;
import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.exceptions.AlreadyExistsException;
import org.spotifumtp37.exceptions.DoesntExistException;
import org.spotifumtp37.model.playlist.Playlist;
import org.spotifumtp37.model.user.User;
import org.spotifumtp37.util.JsonDataParser;
import org.spotifumtp37.util.Stats;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AdminUI {
    private final JsonDataParser parser;
    private final Scanner scanner;
    private final SpotifUMData modelData;

    public AdminUI(SpotifUMData modelData, Scanner scanner) {
        this.modelData = modelData;
        this.scanner = scanner;
        this.parser = new JsonDataParser();
    }

    public void showAdminMenu() {
        NewMenu adminMenu = new NewMenu(new String[]{
                "Manage Albums",
                "Manage Users",
                "Manage Playlists",
                "Manage Data",
                "View System Stats / Execute Queries"
        });

        adminMenu.setHandler(1, this::showAlbumManagementMenu);
        adminMenu.setHandler(2, this::showUserManagementMenu);
        adminMenu.setHandler(3, this::showPlaylistManagementMenu);
        adminMenu.setHandler(4, this::showDataMenu);
        adminMenu.setHandler(5, this::showStatisticsMenu);

        adminMenu.run();
    }

    //submenus do admin
    public void showAlbumManagementMenu() {
        NewMenu albumMenu = new NewMenu(new String[]{
                "Add New Album",
                "Delete Album",
                "View All Albums"
        });


        albumMenu.setHandler(1, this::createAlbum);
        albumMenu.setHandler(2, this::deleteAlbum);
        albumMenu.setHandler(3, this::viewAllAlbums);

        albumMenu.run();
    }

    public void showUserManagementMenu() {
        NewMenu userManagementMenu = new NewMenu(new String[]{
                "Delete User",
                "View All Users"
        });


        userManagementMenu.setHandler(1, this::deleteUser);
        userManagementMenu.setHandler(2, this::viewAllUsers);

        userManagementMenu.run();
    }

    public void showPlaylistManagementMenu() {
        NewMenu playlistManagementMenu = new NewMenu(new String[]{
                "Create Playlist",
                "Delete Playlist",
                "View All Playlists"
        });

        playlistManagementMenu.setHandler(1, this::createPlaylistAdmin);
        playlistManagementMenu.setHandler(2, this::deletePlaylistAdmin);
        playlistManagementMenu.setHandler(3, this::viewAllPlaylists);

        playlistManagementMenu.run();
    }

    public void showDataMenu() {
        NewMenu dataMenu = new NewMenu(new String[]{
                "Import Data from JSON",
                "Export Data to JSON",
                "Serialize System Data",
                "Deserialize System Data",
                "Clear System Data",
                "DEBUG PRINT DATA"
        });

        dataMenu.setHandler(1, this::loadData);
        dataMenu.setHandler(2, this::saveData);
        dataMenu.setHandler(3, this::serializeData);
        dataMenu.setHandler(4, this::deserializeData);
        dataMenu.setHandler(5, this::clearSystemData);
        dataMenu.setHandler(6, this::printCurrentData);

        dataMenu.run();
    }

    public void showStatisticsMenu() {
        NewMenu statsMenu = new NewMenu(new String[]{
                "Get Most Played Song",
                "Get Most Listened Artist",
                "Get the all time top Listener (Most Songs Listened)",
                "Get Top the top Listener since a given time (Most Songs Listened)",
                "Get User With Most Points",
                "Get Most Played Genre",
                "Count Public Playlists",
                "Get User Who Created Most Playlists"
        });

        statsMenu.setHandler(1, () -> {
            Song song = Stats.getMostPlayedSong(modelData.getMapAlbumsCopy());
            if (song != null) {
                System.out.println("Most Played Song: " + song.getName() + " by " + song.getArtist() +
                        " (Played " + song.getTimesPlayed() + " times)");
            } else {
                System.out.println("No song data available or no songs played.");
            }
        });
        statsMenu.setHandler(2, () -> {
            String artist = Stats.getMostListenedArtist(modelData.getMapAlbumsCopy());
            if (artist != null) {
                System.out.println("Most Listened Artist: " + artist);
            } else {
                System.out.println("No artist data available or no songs played.");
            }
        });
        statsMenu.setHandler(3, () -> {
            User user = Stats.getTopListener(modelData.getMapUsers());
            if (user != null) {
                System.out.println("Top Listener (Most Songs Listened): " + user.getName() +
                        " (Listening History Size: " + user.getHistory().size() + ")");
            } else {
                System.out.println("No user data available.");
            }
        });
        statsMenu.setHandler(4, this::handleTopListenerFromDate);
        statsMenu.setHandler(5, () -> {
            User user = Stats.getUserWithMostPoints(modelData.getMapUsers());
            if (user != null) {
                System.out.println("User With Most Points: " + user.getName() +
                        " (Points: " + user.getPontos() + ")");
            } else {
                System.out.println("No user data available for points calculation.");
            }
        });
        statsMenu.setHandler(6, () -> {
            String genre = Stats.mostListenedGenre(modelData.getMapUsers());
            if (genre != null) {
                System.out.println("Most Played Genre: " + genre);
            } else {
                System.out.println("No genre data available from user histories.");
            }
        });

        statsMenu.setHandler(7, () -> {
            long count = Stats.countPublicPlaylists(modelData.getMapPlaylists());
            System.out.println("Number of Public Playlists: " + count);
        });

        statsMenu.setHandler(8, () -> {
            User user = Stats.userWithMostPlaylists(modelData.getMapPlaylists());
            if (user != null) {
                System.out.println("User Who Created Most Playlists: " + user.getName());
            } else {
                System.out.println("No playlist data available or no creators found.");
            }
        });

        statsMenu.run();
    }

    //handlers

    public void handleTopListenerFromDate() {
        System.out.print("Enter start date (yyyy-MM-dd): ");
        String input = scanner.nextLine().trim();
        LocalDate startDate;
        try {
            startDate = LocalDate.parse(input);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }
        LocalDateTime fromDate = startDate.atStartOfDay();

        User top = Stats.getTopListenerFromDate(modelData.getMapUsers(), fromDate);
        if (top != null) {
            System.out.println("User who listened to the most songs since " + startDate + ": " + top.getName());
        } else {
            System.out.println("No user history found for the given period.");
        }
    }


    public void saveData() {
        System.out.print("Enter file path to save: ");
        String path = scanner.nextLine();
        try {
            this.saveToJson(path);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadData() {
        System.out.print("Enter file path to load: ");
        String path = scanner.nextLine();
        try {
            this.loadFromJson(path);
            System.out.println("Data loaded successfully!");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    public void loadFromJson(String filePath) throws IOException {
        SpotifUMData loaded = parser.fromJsonData(filePath);
        modelData.setMapAlbums(loaded.getMapAlbumsCopy());
        modelData.setMapUsers(loaded.getMapUsers());
        modelData.setMapPlaylists(loaded.getMapPlaylists());
    }

    public void saveToJson(String filePath) throws IOException {
        parser.toJsonData(modelData, filePath);
    }

    public void serializeData() {
        System.out.print("Enter filename to save serialized data (data/serial/spotifumdata.ser suggested): ");
        String filename = scanner.nextLine().trim();

        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
            try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(modelData);
                System.out.println("Data serialized successfully!");
            } catch (IOException e) {
                System.out.println("Error serializing data: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error serializing data: " + e.getMessage());
        }
    }

    public void deserializeData() {
        System.out.print("Enter filename to load serialized data (data/serial/spotifumdata.ser suggested): ");
        String filename = scanner.nextLine().trim();

        try (FileInputStream fileIn = new FileInputStream(filename)) {
            try (ObjectInputStream in = new ObjectInputStream(fileIn)) {
                SpotifUMData deserializedData = (SpotifUMData) in.readObject();
                this.modelData.setMapAlbums(deserializedData.getMapAlbumsCopy());
                this.modelData.setMapUsers(deserializedData.getMapUsers());
                this.modelData.setMapPlaylists(deserializedData.getMapPlaylists());

                System.out.println("Data deserialized successfully!");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error deserializing data: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error deserializing data: " + e.getMessage());
        }
    }

    public void clearSystemData() {
        modelData.setMapAlbums(new HashMap<>());
        modelData.setMapUsers(new HashMap<>());
        modelData.setMapPlaylists(new HashMap<>());
        System.out.println(" SpotifUMData has been cleared.");
    }

    public void printCurrentData() {
        System.out.println(modelData.toString());
    }

    public void createAlbum() {
        System.out.println("Admin: Create New Album");

        String title;
        System.out.print("Album title: ");
        title = scanner.nextLine().trim();
        while (this.modelData.existsAlbum(title)) {
            System.out.print("An Album with this title already exists. Please choose another title: ");
            title = scanner.nextLine().trim();
        }

        System.out.print("Artist: ");
        String artist = scanner.nextLine().trim();

        int ano = 0;
        do {
            System.out.print("Release Year (ex: 1973): ");
            try {
                ano = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please type an integer.");
            }
            if (ano <= 1900 || ano > LocalDate.now().getYear()) {
                System.out.println("Please type a valid year (between 1900 and today's).");
                ano = 0;
            }
        } while (ano == 0);

        System.out.print("Genre: ");
        String genre = scanner.nextLine().trim();

        Album album = new Album(title, artist, ano, genre, new ArrayList<>());

        int nSongs;
        do {
            System.out.print("How many songs will this album have?");
            try {
                nSongs = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                nSongs = 0;
            }
            if (nSongs <= 0) {
                System.out.println("Please type a number greater than 0.");
            }
        } while (nSongs <= 0);

        for (int i = 1; i <= nSongs; i++) {
            System.out.printf("---- Song %d/%d ----%n", i, nSongs);

            System.out.print("Song name: ");
            String SongName = scanner.nextLine().trim();

            System.out.print("Publisher: ");
            String publisher = scanner.nextLine().trim();

            System.out.print("Lyrics: ");
            String lyrics = scanner.nextLine().trim();

            System.out.print("Musical Notes: ");
            String musicalNotes = scanner.nextLine().trim();

            System.out.print("The song's genre: ");
            String musicGenre = scanner.nextLine().trim();

            int duration;
            do {
                System.out.print("Duration (seconds): ");
                try {
                    duration = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    duration = 0;
                }
                if (duration <= 0) {
                    System.out.println("Please type a number greater than 0.");
                }
            } while (duration <= 0);

            System.out.print("Is the Song Explicit? (y/n): ");
            boolean isExplicit = scanner.nextLine().trim().equalsIgnoreCase("y");
            boolean isMultimedia = false;
            if (!isExplicit) {
                System.out.println("Does this song have a Music Video? (y/n): ");
                isMultimedia = scanner.nextLine().trim().equalsIgnoreCase("y");
            }
            String videoLink = "";
            if (isMultimedia) {
                System.out.println("Type the link for the music video:");
                videoLink = scanner.nextLine().trim();
            }
            album.addSong(SongName, publisher, lyrics, musicalNotes, musicGenre, duration, isExplicit, isMultimedia, videoLink);
            System.out.println("Song added to album successfully!");
        }

        try {
            album.setCurrentSong();
            this.modelData.addAlbum(album);
            System.out.println("Album created successfully!");
        } catch (AlreadyExistsException e) {
            System.out.println("Error. An album with that title already exists." + e.getMessage());
        }
    }

    public void deleteAlbum() {
        System.out.println("Admin: Delete Album");
        System.out.print("Title of the album to delete: ");
        String title = scanner.nextLine().trim();

        while (!modelData.existsAlbum(title)) {
            System.out.print("Album not found. Enter another title (or 'exit' to cancel): ");
            title = scanner.nextLine().trim();
            if (title.equalsIgnoreCase("exit")) {
                System.out.println("Operation canceled.");
                return;
            }
        }

        System.out.printf("Are you sure you want to delete the album '%s'? (y/n): ", title);
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Operation Cancelled.");
            return;
        }

        try {
            modelData.removeAlbum(title);
            System.out.println("Album deleted successfully.");
        } catch (DoesntExistException e) {
            System.out.println("Error album not found.");
        }
    }

    public void createPlaylistAdmin() {
        System.out.println("Admin: Create Playlist");
        scanner.nextLine();

        User playlistOwner = null;
        System.out.print("Enter username of the playlist owner (leave blank for a general/system playlist): ");
        String ownerUsername = scanner.nextLine().trim();
        if (!ownerUsername.isEmpty()) {
            try {
                playlistOwner = modelData.getUser(ownerUsername);
            } catch (DoesntExistException e) {
                System.out.println("User '" + ownerUsername + "' not found. Playlist will be general if you proceed without a valid owner.");
            }
        }

        List<Song> songs = new ArrayList<>();
        System.out.println("Enter the name for the new playlist: ");
        String nomePlaylist = scanner.nextLine().trim();

        while (this.modelData.existsPlaylist(nomePlaylist)) {
            System.out.println("A playlist with this name already exists. Please choose another name: ");
            nomePlaylist = scanner.nextLine().trim();
        }
        System.out.println("Give the playlist a description:");
        String playlistDescription = scanner.nextLine().trim();
        String estado;
        do {
            System.out.println("Estado (private ou public):");
            estado = scanner.nextLine().trim();
            if (!estado.equalsIgnoreCase("private")
                    && !estado.equalsIgnoreCase("public")) {
                System.out.println("Invalid Option! Write only 'private' ou 'public'.");
            }
        } while (!estado.equalsIgnoreCase("private")
                && !estado.equalsIgnoreCase("public"));
        int n;
        System.out.println("How many songs to add initially?: ");
        while (!scanner.hasNextInt()) {
            System.out.println("That's not a valid number. Please enter an integer:");
            scanner.next();
        }
        n = scanner.nextInt();
        scanner.nextLine();

        while (n <= 0) {
            System.out.println("Type a number greater than 0: ");
            while (!scanner.hasNextInt()) {
                System.out.println("That's not a valid number. Please enter an integer:");
                scanner.next();
            }
            n = scanner.nextInt();
            scanner.nextLine();
        }

        while (n > 0) {
            while (true) {
                System.out.println("Type the name of the album of the song you want to add to the playlist: ");
                String nomeAlbum = scanner.nextLine().trim();

                try {
                    Album album = this.modelData.getAlbum(nomeAlbum);
                    System.out.println("Songs in album '" + album.getTitle() + "':");
                    album.getSongsCopy().forEach(song -> System.out.println("- " + song.getName()));
                    while (true) {
                        System.out.println("Type the name of the song you want to add to the playlist: ");
                        String musicName = scanner.nextLine().trim();
                        try {
                            songs.add(this.modelData.getSong(musicName, nomeAlbum));
                            System.out.println("Song '" + musicName + "' added to playlist");
                            break;
                        } catch (DoesntExistException e) {
                            System.out.println("Incorrect song name, does not exist in album '" + nomeAlbum + "'. Try again.");
                        }
                    }
                    break;

                } catch (DoesntExistException e) {
                    System.out.println("Incorrect album name, does not exist. Try again.");
                }
            }
            n--;
        }

        Playlist playlist = new Playlist(playlistOwner, nomePlaylist, playlistDescription, 0, estado, songs);
        try {
            this.modelData.addPlaylist(playlist);
            System.out.println("Playlist '" + nomePlaylist + "' created successfully!");
        } catch (AlreadyExistsException e) {
            System.out.println("Error: A playlist with that name already exists.");
        }
    }


    public void deletePlaylistAdmin() {
        scanner.nextLine();
        System.out.println("Admin: Delete Playlist");
        System.out.print("Enter the name of the playlist to delete: ");
        String nome = scanner.nextLine().trim();

        if (!modelData.existsPlaylist(nome)) {
            System.out.println("Playlist not found.");
            return;
        }

        System.out.printf("Are you sure you want to delete the playlist '%s'? (y/n): ", nome);
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Operation cancelled.");
            return;
        }

        try {
            modelData.removePlaylist(nome);
            System.out.println("Playlist '" + nome + "' deleted successfully!");
        } catch (DoesntExistException e) {
            System.out.println("Error: Playlist not found during deletion attempt.");
        }
    }

    public void viewAllAlbums() {
        System.out.println("\n--- All Albums ---");
        Map<String, Album> albums = modelData.getMapAlbumsCopy();

        if (albums.isEmpty()) {
            System.out.println("No albums found in the system.");
            return;
        }

        for (Album album : albums.values()) {
            System.out.println(album.toString());
            System.out.println("----------------------------------------");
        }
        System.out.println(albums.size() + " album(s) listed.");
    }

    public void viewAllPlaylists() {
        System.out.println("\n--- All Playlists ---");
        Map<String, Playlist> playlists = modelData.getMapPlaylists();

        if (playlists.isEmpty()) {
            System.out.println("No playlists found in the system.");
            return;
        }

        for (Playlist playlist : playlists.values()) {
            System.out.println(playlist.toString());
            System.out.println("----------------------------------------");
        }
        System.out.println(playlists.size() + " playlist(s) listed.");

    }

    public void viewAllUsers() {
        System.out.println("\n--- All Users ---");
        Map<String, User> users = modelData.getMapUsers();

        if (users.isEmpty()) {
            System.out.println("No users found in the system.");
            return;
        }

        for (User user : users.values()) {
            // Assuming User class has a meaningful toString() method
            System.out.println(user.toString());
            System.out.println("----------------------------------------");
        }
        System.out.println(users.size() + " user(s) listed.");
    }

    public void deleteUser() {
        System.out.println("Admin: Delete User");
        // scanner.nextLine(); // Consume any leftover newline from previous input if necessary
        System.out.print("Enter the username of the user to delete: ");
        String username = scanner.nextLine().trim();

        while (!modelData.existsUser(username)) {
            System.out.print("User not found. Enter another username (or 'exit' to cancel): ");
            username = scanner.nextLine().trim();
            if (username.equalsIgnoreCase("exit")) {
                System.out.println("Operation canceled.");
                return;
            }
        }

        System.out.printf("Are you sure you want to delete the user '%s'? This action cannot be undone. (y/n): ", username);
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Operation Cancelled.");
            return;
        }

        try {
            modelData.removeUser(username);
            System.out.println("User '" + username + "' deleted successfully.");
        } catch (DoesntExistException e) {
            System.out.println("Error: User not found during deletion attempt.");
        }
    }
}