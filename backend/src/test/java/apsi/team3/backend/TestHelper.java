package apsi.team3.backend;

import java.time.LocalDate;
import java.util.ArrayList;

import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;

public class TestHelper {
    public static Event getTestEvent(Long eventId, String name) {
        return new Event(
                eventId,
                name,
                LocalDate.now(),
                null,
                LocalDate.now().plusDays(7),
                null,
                "description",
                new User(1L, "login", "hash", "salt", UserType.ORGANIZER, null),
                new ArrayList<>(),
                null,
                new ArrayList<>()
        );
    }

    public static Event getTestEvent(Long eventId) {
        return getTestEvent(eventId, "name");
    }

    public static Event getTestEvent() {
        return getTestEvent(1L, "name");
    }

    public static User getTestUser(Long userId, String login) {
        return new User(userId, login, "hash", "salt", UserType.ORGANIZER, null);
    }

    public static User getTestUser() {
        return getTestUser(1L, "login");
    }
}
