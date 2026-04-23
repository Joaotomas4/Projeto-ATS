package org.spotifumtp37.model.subscription;

import java.io.Serializable;

/**
 * Represents the base premium subscription plan.
 * Users with this plan receive a fixed point bonus and have access to most features,
 * excluding the favourites list.
 * This class implements {@link SubscriptionPlan} and {@link Serializable}.
 */
public class PremiumBase implements Serializable, SubscriptionPlan {
    /**
     * Adds points by giving a fixed bonus of 10 points to the existing points.
     *
     * @param points The current points of the user.
     * @return The current points plus 10.
     */
    @Override
    public double addPoints(double points) {
        return points + 10;
    }

    /**
     * Allows playlist creation.
     *
     * @return Always {@code true}.
     */
    @Override
    public boolean canCreatePlaylist() {
        return true;
    }

    /**
     * Allows browsing of playlists.
     *
     * @return Always {@code true}.
     */
    @Override
    public boolean canBrowsePlaylist() {
        return true;
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