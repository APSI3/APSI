package apsi.team3.backend.DTOs;

import apsi.team3.backend.model.Country;
import apsi.team3.backend.model.Event;
import apsi.team3.backend.model.EventSection;
import apsi.team3.backend.model.Location;
import apsi.team3.backend.model.Ticket;
import apsi.team3.backend.model.TicketType;
import apsi.team3.backend.model.User;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public static Event toEntity(EventDTO event) {
        var organizer = User.builder().id(event.getOrganizerId()).build();
        var loc = event.getLocation() != null ? 
            Location.builder().id(event.getLocation().getId()).build() :
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
            .sections(null)
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

    public static EventSection toEntity(SectionDTO sectionDTO, Event existingEvent) {
        var event = existingEvent == null ? Event.builder().id(sectionDTO.getEventId()).build() : existingEvent;

        return EventSection.builder()
            .id(sectionDTO.getId())
            .event(event)
            .name(sectionDTO.getName())
            .capacity(sectionDTO.getCapacity())
            .build();
    }

    public static Ticket toEntity(TicketDTO ticket) {
        User user = User.builder().id(ticket.getHolderId()).build();
        TicketType ticketType = TicketType.builder().id(ticket.getTicketTypeId()).build();
        return Ticket.builder()
                .id(ticket.getId())
                .holder(user)
                .purchaseDate(ticket.getPurchaseDate())
                .ticketType(ticketType)
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

        var sections = event.getSections();
        if (sections == null)
            sections = new ArrayList<>();

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
            images.stream().map(i -> i.getId()).toList(),
            new ArrayList<>()
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

    public static SectionDTO toDTO(EventSection section, int ticketsBought) {
        return new SectionDTO(
            section.getId(),
            section.getEvent().getId(),
            section.getName(),
            section.getCapacity(),
            ticketsBought);
    }

    public static TicketDTO toDTO(Ticket ticket) {
        return new TicketDTO(
            ticket.getId(),
            ticket.getTicketType().getId(),
            ticket.getHolder().getId(),
            ticket.getSection().getId(),
            ticket.getPurchaseDate(),
            ""
        );
    }
}
