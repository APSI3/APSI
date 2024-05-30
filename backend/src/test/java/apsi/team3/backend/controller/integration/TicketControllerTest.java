package apsi.team3.backend.controller.integration;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.TestHelper;
import apsi.team3.backend.helpers.MailSender;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.model.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    private MailSender mailSender;

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
                "ticketType": {"id": 2},
                "holder": {"id": 2},
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
    public void testGetTicketByUserIdReturnsTicket() throws Exception {
        LoggedUserDTO loggedUser = login(UserType.PERSON);
        String from = "2024-04-10T00:00:00Z";
        String to = "2024-05-10T00:00:00Z";
        int pageIndex = 0;
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var todayDateStr = LocalDate.now().format(formatter);
        String ticketToJSON = "{\"id\": 1,\n" +
                "\"ticketTypeId\": 2,\n" +
                "\"holderId\": 2,\n" +
                "\"purchaseDate\": 2024-05-30,\n" +
                "\"holderFirstName\": Jan,\n" +
                "\"holderLastName\": Kowalski,\n" +
                "\"eventId\": 1,\n" +
                "\"eventName\": testowe wydarzenie1,\n" +
                "\"eventStartDate\": 2024-04-18,\n" +
                "\"eventEndDate\": 2024-04-18}";
        String qrCode = QRCodeGenerator.generateQRCode(ticketToJSON);
        String expectedJson = String.format("""
            {
                "items": [
                    {
                        "id": 1,
                        "ticketType": { "id": 2 },
                        "holder": { "id": 2 },
                        "purchaseDate": "%s",
                        "qrcode": "%s"
                    }
                ],
                "pageIndex":0,
                "totalItems":1,
                "totalPages":1
            }
        """, todayDateStr, qrCode);

        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/user/2")
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
        Mockito.mockStatic(MailSender.class);
        LoggedUserDTO loggedUser = login(UserType.PERSON);
        TicketDTO ticket = DTOMapper.toDTO(TestHelper.getTestTicket());

        String qrCode = QRCodeGenerator.generateQRCode(ticket.toJSON());
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var todayDateStr = LocalDate.now().format(formatter);
        String expectedJson = String.format("""
            {
                "id": 2,
                "ticketType": { "id": 1 },
                "holder": { "id": 2 },
                "holderFirstName": "jan",
                "holderLastName": "kowalski",
                "purchaseDate": "%s"
            }
        """, todayDateStr, qrCode);

        String request = String.format("""
            {
                "ticketType": { "id": 1 },
                "holder" : { "id": 2 },
                "event" : { "id": 1, "startDate": "2024-04-18", "endDate": "2024-04-18" },
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
