package apsi.team3.backend.DTOs;

import apsi.team3.backend.model.Country;
import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.Location;
import apsi.team3.backend.model.Ticket;
import apsi.team3.backend.model.TicketType;
import apsi.team3.backend.model.User;

import java.util.ArrayList;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class DTOMapper {
    public static User toEntity(UserDTO user) {
        return User.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .build();
    }

    public static Event toEntity(EventDTO event) {
        var organizer = User.builder().id(event.getOrganizerId()).build();
        var loc = event.getLocation() != null ?
                toEntity(event.getLocation()) :
                null;
    
        return Event.builder()
            .id(event.getId())
            .name(event.getName())
            .startDate(event.getStartDate())
            .startTime(event.getStartTime())
            .endDate(event.getEndDate())
            .endTime(event.getEndTime())
            .description(event.getDescription())
            .organizer(organizer)
            .location(loc)
            .ticketTypes(null)
            .build();
    }

    public static TicketType toEntity(TicketTypeDTO ticketType, Event existingEvent) {
        var event = existingEvent == null ?
            Event.builder().id(ticketType.getEventId()).build() :
            existingEvent;

        return TicketType.builder()
                .id(ticketType.getId())
                .event(event)
                .name(ticketType.getName())
                .price(ticketType.getPrice())
                .quantityAvailable(ticketType.getQuantityAvailable())
                .build();
    }

    public static Ticket toEntity(TicketDTO ticket) {
        var ticketHolder = ticket.getHolder();
        if (ticket.getEvent() == null) {
            return null;
        }
        var event = toEntity(ticket.getEvent());
        User user = User.builder()
                .id(ticketHolder.getId())
                .login(ticketHolder.getLogin())
                .email(ticketHolder.getEmail())
                .build();
        TicketType ticketType = DTOMapper.toEntity(ticket.getTicketType(), event);
        return Ticket.builder()
            .id(ticket.getId())
            .holder(user)
            .purchaseDate(LocalDate.now())
            .ticketType(ticketType)
            .holderFirstName(ticket.getHolderFirstName())
            .holderLastName(ticket.getHolderLastName())
            .build();
    }

    public static Location toEntity(LocationDTO loc) {
        var user = User.builder().id(loc.getCreator_id()).build();
        var country = Country.builder().id(loc.getCountry_id()).build();
        return Location.builder()
            .id(loc.getId())
            .creator(user)
            .country(country)
            .apartment_nr(loc.getApartment_nr())
            .building_nr(loc.getBuilding_nr())
            .capacity(loc.getCapacity())
            .description(loc.getDescription())
            .city(loc.getCity())
            .street(loc.getStreet())
            .zip_code(loc.getZip_code())
            .build();
    }

    public static EventDTO toDTO(Event event) {
        var images = event.getImages();
        if (images == null)
            images = new ArrayList<>();

        var ticketTypes = event.getTicketTypes();
        if (ticketTypes == null)
            ticketTypes = new ArrayList<>();

        return new EventDTO(
            event.getId(),
            event.getName(),
            event.getStartDate(),
            event.getStartTime(),
            event.getEndDate(),
            event.getEndTime(),
            event.getDescription(),
            event.getOrganizer().getId(),
            event.getLocation() != null ? DTOMapper.toDTO(event.getLocation()) : null,
            ticketTypes.stream().map(DTOMapper::toDTO).toList(),
            images.stream().map(i -> i.getId()).toList()
        );
    }

    public static LoggedUserDTO toDTO(User user, String header) {
        return new LoggedUserDTO(user.getId(), user.getLogin(), user.getEmail(), header, user.getType());
    }

    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getLogin(), user.getEmail());
    }

    public static LocationDTO toDTO(Location loc) {
        return new LocationDTO(
            loc.getId(), 
            loc.getCountry().getId(),
            loc.getCapacity(),
            loc.getDescription(),
            loc.getCity(),
            loc.getStreet(),
            loc.getBuilding_nr(),
            loc.getApartment_nr(),
            loc.getZip_code(),
            loc.getCreator().getId()
        );
    }

    public static CountryDTO toDTO(Country country) {
        return new CountryDTO(country.getId(), country.getCode(), country.getFull_name());
    }

    public static TicketTypeDTO toDTO(TicketType ticketType) {
        return new TicketTypeDTO(
                ticketType.getId(),
                ticketType.getEvent().getId(),
                ticketType.getName(),
                ticketType.getPrice(),
                ticketType.getQuantityAvailable()
        );
    }

    public static TicketDTO toDTO(Ticket ticket) {
        var event = ticket.getTicketType().getEvent();
        return new TicketDTO(
            ticket.getId(),
            DTOMapper.toDTO(ticket.getTicketType()),
            DTOMapper.toDTO(ticket.getHolder()),
            DTOMapper.toDTO(event),
            ticket.getPurchaseDate(),
            null,
            ticket.getHolderFirstName(),
            ticket.getHolderLastName()
        );
    }
}
