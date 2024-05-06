package apsi.team3.backend;

import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;

public class TestHelper {
    public static Event getTestEvent(Long eventId, String name) {
        return new Event(
                eventId,
                name,
                null,
                null,
                null,
                null,
                "description",
                new User(1L, "login", "hash", "salt", UserType.ORGANIZER, null),
                null,
                null
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