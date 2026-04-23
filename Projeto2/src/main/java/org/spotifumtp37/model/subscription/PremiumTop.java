package org.spotifumtp37.model.subscription;

import java.io.Serializable;

/**
 * Represents the top-tier premium subscription plan.
 * Users with this plan receive a percentage-based point bonus and have access to all features.
 * This class implements {@link SubscriptionPlan} and {@link Serializable}.
 */
public class PremiumTop implements Serializable, SubscriptionPlan {
    /**
     * Adds points by applying a 2.5% bonus to the existing points.
     *
     * @param points The current points of the user.
     * @return The current points multiplied by 1.025.
     */
    @Override
    public double addPoints(double points) {
        return 1.025 * points;
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
     * Allows access to the favourites list.
     *
     * @return Always {@code true}.
     */
    @Override
    public boolean canAccessFavouritesList() {
        return true;
    }
}