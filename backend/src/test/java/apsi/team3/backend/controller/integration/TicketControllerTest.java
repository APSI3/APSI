package apsi.team3.backend.controller.integration;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.ExtendedTicketDTO;
import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.model.UserType;
import apsi.team3.backend.services.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class TicketControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private MailService mailService;

    static ObjectMapper objectMapper;
    final static String ADMIN_LOGIN = "testuser1";
    final static String PERSON_LOGIN = "person";
    final static String PASSWORD = "apsi";

    private LoggedUserDTO login(UserType type) throws Exception {
        LoginRequest loginRequest = new LoginRequest(type == UserType.SUPERADMIN ? ADMIN_LOGIN : PERSON_LOGIN, PASSWORD);
        String stringLoginRequest = objectMapper.writeValueAsString(loginRequest);
        String responseContent = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringLoginRequest)
                )
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseContent, LoggedUserDTO.class);
    }

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetByIdReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testGetTicketByIdReturnsTicket() throws Exception {
        LoggedUserDTO loggedUser = login(UserType.PERSON);
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var todayDateStr = LocalDate.now().format(formatter);
        String expectedJson = String.format("""
            {
                "id": 1,
                "ticketTypeId": 2,
                "holderId": 2,
                "holderFirstName": "Jan",
                "holderLastName": "Kowalski",
                "purchaseDate": "%s"
            }
        """, todayDateStr);

        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/1").header("Authorization", loggedUser.getAuthHeader()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testGetExtendedTicketsByUserId() throws Exception {
        LoggedUserDTO loggedUser = login(UserType.PERSON);
        String from = "2024-04-10T00:00:00Z";
        String to = "2024-05-10T00:00:00Z";
        int pageIndex = 0;
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var todayDateStr = LocalDate.now().format(formatter);
        TicketDTO ticket = new ExtendedTicketDTO(
                1L,
                2L,
                2L,
                1L,
                LocalDate.now(), null,
                "Janan",
                "Kowalski",
                "testowe wydarzenie1",
                LocalDate.of(2024, 4, 18), null, LocalDate.of(2024, 4, 18), null, "drogie bilety", BigDecimal.valueOf(19.99)
                );
        String qrCode = QRCodeGenerator.generateQRCode(ticket.toString());

        String expectedJson = String.format("""
            {
                "items":[
                    {
                        "id":1,
                        "ticketTypeId":2,
                        "holderId":2,
                        "eventId":1,
                        "purchaseDate":"%s",
                        "holderFirstName":"Jan",
                        "holderLastName":"Kowalski",
                        "eventName":"testowe wydarzenie1",
                        "eventStartDate":"2024-04-18",
                        "eventStartTime":null,
                        "eventEndDate":"2024-04-18",
                        "eventEndTime":null,
                        "ticketTypeName":"drogie bilety",
                        "price":19.99,
                        "qrcode":"%s"
                    }
                ],
                "pageIndex":0,
                "totalItems":1,
                "totalPages":1
            }
        """, todayDateStr, qrCode);

        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/user/2/extended")
                        .param("from", from)
                        .param("to", to)
                        .param("pageIndex", String.valueOf(pageIndex))
                        .header("Authorization", loggedUser.getAuthHeader()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testCreateTicketReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tickets/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    public void testCreateTicketReturnsCreatedObject() throws Exception {
        LoggedUserDTO loggedUser = login(UserType.PERSON);
        TicketDTO ticket = new TicketDTO(2L, 2L, 2L, 1L, LocalDate.now(), "", "jan", "kowalski");
        String qrCode = QRCodeGenerator.generateQRCode(ticket.toJSON(new EventDTO(1L, "test", LocalDate.of(2024, 4, 18), null, LocalDate.of(2024, 4, 18), null, "", null, null, null, null)));
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var todayDateStr = LocalDate.now().format(formatter);
        String expectedJson = String.format("""
            {
                "id": 2,
                "ticketTypeId": 2,
                "holderId": 2,
                "holderFirstName": "jan",
                "holderLastName": "kowalski",
                "purchaseDate": "%s"
            }
        """, todayDateStr, qrCode);
        String request = String.format("""
            {
                "ticketTypeId": 2,
                "holderFirstName": "jan",
                "holderLastName": "kowalski"
            }
        """, todayDateStr);
        mockMvc.perform(MockMvcRequestBuilders.post("/tickets")
                .header("Authorization", loggedUser.getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(content().json(expectedJson));

        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/2").header("Authorization", loggedUser.getAuthHeader()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().json(expectedJson));
    }
}
