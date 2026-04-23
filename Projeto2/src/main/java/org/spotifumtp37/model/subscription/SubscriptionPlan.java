package org.spotifumtp37.model.subscription;

import java.io.Serializable;

/**
 * Defines the contract for different subscription plans in the SpotifUM application.
 * Each subscription plan determines how user points are calculated and what features
 * a user can access.
 * This interface extends {@link Serializable} to allow subscription plan objects
 * to be persisted.
 */
public interface SubscriptionPlan extends Serializable {
    /**
     * Calculates and returns the number of points a user earns based on their current points
     * and the rules of this subscription plan.
     *
     * @param points The current points of the user before applying the plan's bonus.
     * @return The new total points after applying the plan's logic.
     */
    double addPoints(double points);

    /**
     * Determines if a user with this subscription plan is allowed to create playlists.
     *
     * @return {@code true} if playlist creation is allowed, {@code false} otherwise.
     */
    boolean canCreatePlaylist();

    /**
     * Determines if a user with this subscription plan is allowed to browse playlists
     * (e.g., skip to next/previous songs in a non-shuffle mode).
     *
     * @return {@code true} if playlist browsing is allowed, {@code false} otherwise.
     */
    boolean canBrowsePlaylist();

    /**
     * Determines if a user with this subscription plan can access their "Favourites" list.
     *
     * @return {@code true} if access to the favourites list is allowed, {@code false} otherwise.
     */
    boolean canAccessFavouritesList();
}