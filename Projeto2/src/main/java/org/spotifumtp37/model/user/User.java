package org.spotifumtp37.model.user;

import org.spotifumtp37.model.album.Album;
import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.model.subscription.FreePlan;
import org.spotifumtp37.model.subscription.PremiumTop;
import org.spotifumtp37.model.subscription.SubscriptionPlan;

import java.util.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a user in the system.
 * This class stores user information, subscription details, listening history, and points.
 * It is serializable, allowing its state to be saved and restored.
 */
public class User implements Serializable {
    /**
     * The name of the user.
     */
    private String name;
    /**
     * The email address of the user.
     */
    private String email;
    /**
     * The physical address of the user.
     */
    private String address;
    /**
     * The current subscription plan of the user.
     */
    private SubscriptionPlan subscriptionplan;
    /**
     * The password for the user's account.
     */
    private String password;
    /**
     * The points accumulated by the user.
     */
    private double pontos;
    /**
     * The listening history of the user, containing records of played songs.
     */
    private List<History> history;

    /**
     * Constructs a new User with specified details.
     *
     * @param name             The name of the user.
     * @param email            The email address of the user.
     * @param address          The physical address of the user.
     * @param subscriptionPlan The subscription plan for the user.
     * @param password         The password for the user's account.
     * @param pontos           The initial points for the user.
     * @param history          The initial listening history for the user. A deep copy of the list and its elements is made.
     */
    public User(String name, String email, String address, SubscriptionPlan subscriptionPlan, String password, double pontos, List<History> history) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.subscriptionplan = subscriptionPlan;
        this.password = password;
        this.pontos = pontos;
        this.history = new ArrayList<>();
        for (History h : history) {
            this.history.add(h.clone());
        }
    }


    /**
     * Copy constructor for User.
     * Creates a new User object by copying the details from another User object.
     * The listening history is deep copied by cloning each {@link History} object.
     *
     * @param other The User object to copy.
     */
    public User(User other) {
        this.name = other.getName();
        this.email = other.getEmail();
        this.address = other.getAddress();
        this.subscriptionplan = other.getSubscriptionPlan(); // Assumes SubscriptionPlan is immutable or properly handled
        this.password = other.getPassword();
        this.pontos = other.getPontos();
        this.history = new ArrayList<>();
        for (History h : other.history) {
            this.history.add(h.clone());
        }
    }

    /**
     * Default constructor for User.
     * Initializes the user with default values: empty strings for name, email, address, and password,
     * a new {@link FreePlan} for the subscription, 0 points, and an empty listening history.
     */
    public User() {
        this.name = "";
        this.email = "";
        this.address = "";
        this.subscriptionplan = new FreePlan();
        this.password = "";
        this.pontos = 0;
        this.history = new ArrayList<>();
    }

    /**
     * Gets the name of the user.
     *
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the physical address of the user.
     *
     * @return The user's physical address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the current subscription plan of the user.
     *
     * @return The user's {@link SubscriptionPlan}.
     */
    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionplan;
    }

    /**
     * Gets the password of the user.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the points accumulated by the user.
     *
     * @return The user's points.
     */
    public double getPontos() {
        return pontos;
    }

    /**
     * Gets a copy of the user's listening history.
     * Each {@link History} object in the list is a clone of the original.
     *
     * @return A new list containing clones of the user's {@link History} records.
     */
    public List<History> getHistory() {
        List<History> copy = new ArrayList<>();
        for (History h : this.history) {
            copy.add(h.clone());
        }
        return copy;
    }

    /**
     * Sets the user's subscription plan.
     *
     * @param newPlan The new {@link SubscriptionPlan} for the user.
     * @throws IllegalArgumentException if the newPlan is null.
     */
    public void setSubscriptionPlan(SubscriptionPlan newPlan) {
        if (newPlan != null) {
            this.subscriptionplan = newPlan;  // Update the user's subscription plan with the new one
        } else {
            throw new IllegalArgumentException("Subscription plan cannot be null.");
        }
    }

    /**
     * Sets the points for the user.
     *
     * @param pontos The new point value.
     */
    public void setPontos(double pontos) {
        this.pontos = pontos;
    }

    /**
     * Sets the user's listening history.
     * The provided list of history records is deep copied.
     *
     * @param history A list of {@link History} records to set. Each element will be cloned.
     */
    public void setHistory(List<History> history) {
        this.history = new ArrayList<>();
        for (History h : history) {
            this.history.add(h.clone());
        }
    }

    /**
     * Sets the name of the user.
     *
     * @param name The new name for the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The new email address for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the physical address of the user.
     *
     * @param address The new physical address for the user.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the password for the user's account.
     *
     * @param password The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Adds points to the user's account based on their current subscription plan.
     * The calculation of points is delegated to the {@link SubscriptionPlan#addPoints(double)} method.
     */
    public void addPoints() {
        double newPoints = subscriptionplan.addPoints(pontos); // Get the new points from the plan
        setPontos(newPoints);
    }

    /**
     * Creates and returns a deep copy of this User object.
     *
     * @return A clone of this User instance.
     */
    public User clone() {
        return new User(this);
    }

    /**
     * Updates the user's subscription to a {@link PremiumTop} plan and adds 100 points.
     *
     * @param newPlan The new {@link PremiumTop} subscription plan.
     */
    public void updatePremiumTop(PremiumTop newPlan) {
        this.setSubscriptionPlan(newPlan);
        this.pontos += 100;
    }

    /**
     * Adds a new song to the user's listening history.
     * A new {@link History} record is created with the current time and a clone of the provided song.
     *
     * @param song The {@link Song} to add to the history. A clone of this song will be stored.
     */
    public void updateHistory(Song song) {
        History h = new History();
        LocalDateTime time = LocalDateTime.now();
        h.setSong(song.clone());
        h.setTime(time);
        this.history.add(h);
    }

    /**
     * Returns a string representation of the User object.
     *
     * @return A string detailing the user's properties.
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", subscription plan=" + subscriptionplan +
                ", password='" + password + '\'' +
                ", pontos=" + pontos +
                ", history=" + history +
                '}';
    }

    /**
     * Compares this User object to another object for equality.
     * Two User objects are considered equal if their names are equal.
     *
     * @param o The object to compare with this User object.
     * @return {@code true} if the given object is a User and has the same name; {@code false} otherwise.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name); // Compare by name
    }

    /**
     * Determines the user's most frequently listened to genre based on their listening history.
     *
     * @return The top genre as a String, or null if the history is empty or genres are not available.
     */
    public String getTopGenre() {
        if (history == null || history.isEmpty()) return null;

        Map<String, Integer> genreCount = new HashMap<>();
        for (History h : history) {
            Song song = h.getSong();
            if (song != null && song.getGenre() != null) {
                String genre = song.getGenre();
                genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
            }
        }
        return genreCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Creates a playlist of songs belonging to the user's top genre.
     *
     * @param albumMap A map of album titles to {@link Album} objects, used to find songs.
     * @return A list of {@link Song} objects from the top genre. Returns an empty list if the top genre is null or albumMap is null.
     */
    public List<Song> createTopGenrePlaylist(Map<String, Album> albumMap) {
        String topGenre = getTopGenre();
        if (topGenre == null || albumMap == null) return Collections.emptyList();

        List<Song> result = new ArrayList<>();
        for (Album album : albumMap.values()) {
            for (Song song : album.getSongs()) {
                if (topGenre.equals(song.getGenre())) {
                    result.add(song);
                }
            }
        }
        return result;
    }

    /**
     * Creates a playlist of songs from the user's top genre, constrained by a maximum total duration.
     * Songs are added to the playlist until the maximum time is reached.
     *
     * @param albumMap A map of album titles to {@link Album} objects.
     * @param maxTime  The maximum total duration of the playlist in seconds.
     * @return A list of {@link Song} objects. Returns an empty list if the top genre is null or albumMap is null.
     */
    public List<Song> createTopGenrePlaylistWithinTime(Map<String, Album> albumMap, int maxTime) {
        String topGenre = getTopGenre();
        if (topGenre == null || albumMap == null) return Collections.emptyList();

        int totalTime = 0;
        List<Song> playlist = new ArrayList<>();
        outer:
        for (Album album : albumMap.values()) {
            for (Song song : album.getSongs()) {
                if (topGenre.equals(song.getGenre())) {
                    int songTime = song.getDurationInSeconds();
                    if (totalTime + songTime > maxTime) break outer;
                    playlist.add(song);
                    totalTime += songTime;
                }
            }
        }
        return playlist;
    }

    /**
     * Creates a playlist of explicit songs belonging to the user's top genre.
     *
     * @param albumMap A map of album titles to {@link Album} objects.
     * @return A list of explicit {@link Song} objects from the top genre. Returns an empty list if the top genre is null or albumMap is null.
     */
    public List<Song> createTopGenreExplicitPlaylist(Map<String, Album> albumMap) {
        String topGenre = getTopGenre();
        if (topGenre == null || albumMap == null) return Collections.emptyList();

        List<Song> result = new ArrayList<>();
        for (Album album : albumMap.values()) {
            for (Song song : album.getSongs()) {
                if (topGenre.equals(song.getGenre()) && song.isExplicit()) {
                    result.add(song);
                }
            }
        }
        return result;
    }
}