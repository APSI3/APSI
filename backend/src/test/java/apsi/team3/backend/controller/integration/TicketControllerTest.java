package apsi.team3.backend.controller.integration;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.helpers.QRCodeGenerator;
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
    final static String LOGIN = "testuser1";
    final static String PASSWORD = "apsi";

    private LoggedUserDTO login() throws Exception {
        LoginRequest loginRequest = new LoginRequest(LOGIN, PASSWORD);
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
        LoggedUserDTO loggedUser = login();
        String expectedJson = """
            {
                "id": 1,
                "ticketTypeId": 2,
                "holderId": 2,
                "purchaseDate": "2024-05-17",
                "qrcode": null
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/1").header("Authorization", loggedUser.getAuthHeader()))
                .andExpect(MockMvcResultMatchers.status().isOk())
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
        LoggedUserDTO loggedUser = login();
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TicketDTO ticket = new TicketDTO(2L, 2L, 2L, LocalDate.parse("2024-05-17", dtf));
        String qrCode = QRCodeGenerator.generateQRCode(ticket.toString());
        String expectedJson = String.format("""
            {
                "id": 2,
                "ticketTypeId": 2,
                "holderId": 2,
                "purchaseDate": "2024-05-17",
                "qrcode": "%s"
            }
        """, qrCode);
        String request = """
            {
                "ticketTypeId": 2,
                "holderId": 2,
                "purchaseDate": "2024-05-17"
            }
        """;
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