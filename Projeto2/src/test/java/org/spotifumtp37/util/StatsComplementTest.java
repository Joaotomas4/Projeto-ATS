package org.spotifumtp37.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spotifumtp37.model.album.Song;
import org.spotifumtp37.model.subscription.FreePlan;
import org.spotifumtp37.model.subscription.PremiumBase;
import org.spotifumtp37.model.user.History;
import org.spotifumtp37.model.user.User;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes complementares ao StatsTest existente.
 * Cobre o método getTopListenerFromDate que não estava testado.
 */
class StatsComplementTest {

    private Map<String, User> users;
    private Song song1;
    private Song song2;

    @BeforeEach
    void setUp() {
        users = new HashMap<>();
        song1 = new Song("Song1", "Artist1", "Publisher1", "Lyrics1", "Notes1", "Rock", 180);
        song2 = new Song("Song2", "Artist2", "Publisher2", "Lyrics2", "Notes2", "Pop", 240);
    }

    // ── getTopListenerFromDate ─────────────────────────────────────────────────

    @Test
    void getTopListenerFromDateReturnsNullForNullMap() {
        assertNull(Stats.getTopListenerFromDate(null, LocalDateTime.now()));
    }

    @Test
    void getTopListenerFromDateReturnsNullForEmptyMap() {
        assertNull(Stats.getTopListenerFromDate(new HashMap<>(), LocalDateTime.now()));
    }

    @Test
    void getTopListenerFromDateReturnsNullForNullDate() {
        User user = new User("User1", "u@email.com", "Addr", new FreePlan(), "pw", 0, new ArrayList<>());
        users.put("User1", user);
        assertNull(Stats.getTopListenerFromDate(users, null));
    }

    @Test
    void getTopListenerFromDateFiltersCorrectlyByDate() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(5);

        // User1 ouviu 2 músicas ANTES do cutoff e 1 DEPOIS
        List<History> history1 = new ArrayList<>();
        history1.add(new History(song1, cutoff.minusDays(2))); // antes
        history1.add(new History(song2, cutoff.minusDays(1))); // antes
        history1.add(new History(song1, cutoff.plusDays(1)));  // depois ← conta

        // User2 ouviu 3 músicas DEPOIS do cutoff
        List<History> history2 = new ArrayList<>();
        history2.add(new History(song1, cutoff.plusDays(1)));  // depois ← conta
        history2.add(new History(song2, cutoff.plusDays(2)));  // depois ← conta
        history2.add(new History(song1, cutoff.plusDays(3)));  // depois ← conta

        User user1 = new User("User1", "u1@email.com", "Addr1", new FreePlan(), "pw1", 0, history1);
        User user2 = new User("User2", "u2@email.com", "Addr2", new PremiumBase(), "pw2", 0, history2);

        users.put("User1", user1);
        users.put("User2", user2);

        // User2 tem 3 plays após cutoff vs User1 com 1
        User top = Stats.getTopListenerFromDate(users, cutoff);
        assertNotNull(top);
        assertEquals("User2", top.getName());
    }

    @Test
    void getTopListenerFromDateWithExactCutoffDateIsInclusive() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 12, 0);

        List<History> history1 = new ArrayList<>();
        history1.add(new History(song1, cutoff)); // exatamente no cutoff ← deve contar

        List<History> history2 = new ArrayList<>();
        history2.add(new History(song1, cutoff.minusSeconds(1))); // antes ← não conta

        User user1 = new User("User1", "u1@email.com", "Addr1", new FreePlan(), "pw1", 0, history1);
        User user2 = new User("User2", "u2@email.com", "Addr2", new FreePlan(), "pw2", 0, history2);

        users.put("User1", user1);
        users.put("User2", user2);

        User top = Stats.getTopListenerFromDate(users, cutoff);
        assertNotNull(top);
        assertEquals("User1", top.getName());
    }

    @Test
    void getTopListenerFromDateWithNoHistoryAfterCutoff() {
        LocalDateTime cutoff = LocalDateTime.now().plusDays(1); // cutoff no futuro

        List<History> history = new ArrayList<>();
        history.add(new History(song1, LocalDateTime.now().minusDays(1))); // passado ← não conta

        User user = new User("User1", "u@email.com", "Addr", new FreePlan(), "pw", 0, history);
        users.put("User1", user);

        // Nenhum play após cutoff mas ainda retorna o único user (com 0 plays)
        User top = Stats.getTopListenerFromDate(users, cutoff);
        assertNotNull(top); // stream.max devolve o único elemento existente
    }

    @Test
    void getTopListenerFromDateWithSingleUser() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(3);

        List<History> history = new ArrayList<>();
        history.add(new History(song1, cutoff.plusDays(1)));
        history.add(new History(song2, cutoff.plusDays(2)));

        User user = new User("Solo", "solo@email.com", "Addr", new FreePlan(), "pw", 0, history);
        users.put("Solo", user);

        User top = Stats.getTopListenerFromDate(users, cutoff);
        assertNotNull(top);
        assertEquals("Solo", top.getName());
    }
}
