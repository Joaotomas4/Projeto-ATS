package org.spotifumtp37.model.subscription;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionPlanTest {

    // ── FreePlan ───────────────────────────────────────────────────────────────

    @Test
    void freePlanAddPointsAdds5() {
        FreePlan plan = new FreePlan();
        assertEquals(5.0, plan.addPoints(0.0));
        assertEquals(55.0, plan.addPoints(50.0));
        assertEquals(105.0, plan.addPoints(100.0));
    }

    @Test
    void freePlanCannotCreatePlaylist() {
        assertFalse(new FreePlan().canCreatePlaylist());
    }

    @Test
    void freePlanCannotBrowsePlaylist() {
        assertFalse(new FreePlan().canBrowsePlaylist());
    }

    @Test
    void freePlanCannotAccessFavouritesList() {
        assertFalse(new FreePlan().canAccessFavouritesList());
    }

    // ── PremiumBase ────────────────────────────────────────────────────────────

    @Test
    void premiumBaseAddPointsAdds10() {
        PremiumBase plan = new PremiumBase();
        assertEquals(10.0, plan.addPoints(0.0));
        assertEquals(60.0, plan.addPoints(50.0));
        assertEquals(110.0, plan.addPoints(100.0));
    }

    @Test
    void premiumBaseCanCreatePlaylist() {
        assertTrue(new PremiumBase().canCreatePlaylist());
    }

    @Test
    void premiumBaseCanBrowsePlaylist() {
        assertTrue(new PremiumBase().canBrowsePlaylist());
    }

    @Test
    void premiumBaseCannotAccessFavouritesList() {
        assertFalse(new PremiumBase().canAccessFavouritesList());
    }

    // ── PremiumTop ─────────────────────────────────────────────────────────────

    @Test
    void premiumTopAddPointsApplies2Point5Percent() {
        PremiumTop plan = new PremiumTop();
        assertEquals(0.0, plan.addPoints(0.0));
        assertEquals(102.5, plan.addPoints(100.0), 0.001);
        assertEquals(205.0, plan.addPoints(200.0), 0.001);
    }

    @Test
    void premiumTopCanCreatePlaylist() {
        assertTrue(new PremiumTop().canCreatePlaylist());
    }

    @Test
    void premiumTopCanBrowsePlaylist() {
        assertTrue(new PremiumTop().canBrowsePlaylist());
    }

    @Test
    void premiumTopCanAccessFavouritesList() {
        assertTrue(new PremiumTop().canAccessFavouritesList());
    }

    // ── Comparação entre planos ────────────────────────────────────────────────

    @Test
    void freePlanEarnsLessPointsThanPremiumBase() {
        FreePlan free = new FreePlan();
        PremiumBase base = new PremiumBase();
        // A partir de 0 pontos: Free dá 5, Base dá 10
        assertTrue(base.addPoints(0) > free.addPoints(0));
    }

    @Test
    void premiumTopEarnsMoreThanBaseForHighPoints() {
        PremiumBase base = new PremiumBase();
        PremiumTop top = new PremiumTop();
        // Com 500 pontos: Top dá 512.5, Base dá 510
        assertTrue(top.addPoints(500) > base.addPoints(500));
    }

    @Test
    void premiumTopEarnsLessThanBaseForLowPoints() {
        PremiumBase base = new PremiumBase();
        PremiumTop top = new PremiumTop();
        // Com 0 pontos: Top dá 0, Base dá 10
        assertTrue(base.addPoints(0) > top.addPoints(0));
    }
}
