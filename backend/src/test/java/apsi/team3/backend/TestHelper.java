package apsi.team3.backend;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import apsi.team3.backend.model.*;

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
            false,
            new User(1L, "login", "hash", "salt", UserType.ORGANIZER, "email", null),
            new ArrayList<>(),
            null,
            new ArrayList<>(),
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
        return new User(userId, login, "hash", "salt", UserType.ORGANIZER, "email", null);
    }

    public static User getTestUser() {
        return getTestUser(1L, "login");
    }

    public static TicketType getTestTicketType() {
        return new TicketType(1L, getTestEvent(), "test ticket type", new BigDecimal(1l), 10);
    }

    public static EventSection getTestSection() {
        return new EventSection(1L, getTestEvent(), "test section", 100);
    }

    public static Ticket getTestTicket() {
        var holder = TestHelper.getTestUser();
        var ticketType = TestHelper.getTestTicketType();
        var section = TestHelper.getTestSection();
        return new Ticket(2L, holder, section, ticketType, LocalDate.now(), "jan", "kowalski");
    }
}
