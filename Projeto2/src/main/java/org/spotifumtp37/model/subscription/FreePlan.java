package org.spotifumtp37.model.subscription;

import java.io.Serializable;

/**
 * Represents the free subscription plan.
 * Users with this plan receive a small fixed point bonus and have limited access to features.
 * This class implements {@link SubscriptionPlan} and {@link Serializable}.
 */
public class FreePlan implements SubscriptionPlan, Serializable {

    /**
     * Adds points by giving a fixed bonus of 5 points to the existing points.
     *
     * @param points The current points of the user.
     * @return The current points plus 5.
     */
    @Override
    public double addPoints(double points) {
        return 5 + points;
    }

    /**
     * Denies playlist creation.
     *
     * @return Always {@code false}.
     */
    @Override
    public boolean canCreatePlaylist() {
        return false;
    }

    /**
     * Denies browsing of playlists (e.g., forcing shuffle mode).
     *
     * @return Always {@code false}.
     */
    @Override
    public boolean canBrowsePlaylist() {
        return false;
    }

    /**
     * Denies access to the favourites list.
     *
     * @return Always {@code false}.
     */
    @Override
    public boolean canAccessFavouritesList() {
        return false;
    }
}