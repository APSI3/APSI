package apsi.team3.backend.controller.integration;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    static ObjectMapper objectMapper;
    final static String LOGIN = "testuser1";
    final static String PASSWORD = "apsi";

    private LoggedUserDTO login(String login) throws Exception {
        LoginRequest loginRequest = new LoginRequest(login == null ? LOGIN : login, PASSWORD);
        String stringLoginRequest = objectMapper.writeValueAsString(loginRequest);
        String responseContent = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(stringLoginRequest)
        ).andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseContent, LoggedUserDTO.class);
    }

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllEventsReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testGetEventsReturnsListOfSomeEventsFromTestDB() throws Exception {
        LoggedUserDTO loggedUser = login(null);
        String expectedJson = """
            {
                "pageIndex": 0,
                "totalItems": 1,
                "totalPages": 1,
                "items": [
                    {
                        "id": 1,
                        "name": "testowe wydarzenie1",
                        "startDate": "2024-04-18",
                        "endDate": "2024-04-18",
                        "description": "testowe wydarzenie dodane w migracji",
                        "organizerId": 3
                    }
                ]
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.get("/events?from=2024-04-10T00:00:00Z&to=2024-05-10T00:00:00Z&pageIndex=0").header("Authorization", loggedUser.getAuthHeader()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedJson));
    }

    @Test
    public void testGetEventsReturnsListOfAllEventsFromTestDB() throws Exception {
        LoggedUserDTO loggedUser = login("organizer");
        String expectedJson = """
            {
                "pageIndex": 0,
                "totalItems": 2,
                "totalPages": 1,
                "items": [
                    {
                        "id": 1,
                        "name": "testowe wydarzenie1",
                        "startDate": "2024-04-18",
                        "endDate": "2024-04-18",
                        "description": "testowe wydarzenie dodane w migracji",
                        "organizerId": 3
                    },
                    {
                        "id": 2,
                        "name": "testowe wydarzenie2",
                        "startDate": "2024-05-18",
                        "endDate": "2024-05-18",
                        "description": "testowe wydarzenie dodane w migracji",
                        "organizerId": 3
                    }
                ]
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.get("/events?from=2024-04-10T00:00:00Z&to=2024-06-10T00:00:00Z&pageIndex=0").header("Authorization", loggedUser.getAuthHeader()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedJson));
    }

    @Test
    public void testGetEventByIdReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testGetEventByIdReturnsUser() throws Exception {
        LoggedUserDTO loggedUserDTO = login(null);
        String expectedJson = """
            {
                "id": 1,
                "name": "testowe wydarzenie1",
                "startDate": "2024-04-18",
                "endDate": "2024-04-18",
                "description": "testowe wydarzenie dodane w migracji",
                "organizerId": 3
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.get("/events/1").header("Authorization", loggedUserDTO.getAuthHeader()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testCreateEventByIdReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/events/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    public void testCreateEventReturnsCreatedObject() throws Exception {
        LoggedUserDTO loggedUser = login("organizer");
        String expectedJson = """
            {
                "id": 3,
                "name": "test name",
                "startDate": "2026-11-01",
                "endDate": "2027-01-01",
                "description": "test description",
                "organizerId": 3,
                "ticketTypes": [
                    {
                        "name": "type 1",
                        "quantityAvailable": 1,
                        "price": 2
                    }
                ],
                "sections": [
                    {
                        "name": "test",
                        "capacity": 1000
                    }
                ]
            }
        """;
        String request = """
            {
                "name": "test name",
                "startDate": "2026-11-01",
                "endDate": "2027-01-01",
                "description": "test description",
                "ticketTypes": [
                    {
                        "name": "type 1",
                        "quantityAvailable": 1,
                        "price": 2
                    }
                ],
                "sections": [
                    {
                        "name": "test",
                        "capacity": 1000
                    }
                ]
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.multipart("/events")
                .header("Authorization", loggedUser.getAuthHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("event", request))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(content().json(expectedJson));

        mockMvc.perform(MockMvcRequestBuilders.get("/events/3").header("Authorization", loggedUser.getAuthHeader()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testReplaceEventReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/events/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    public void testReplaceEventReplacesEvent() throws Exception {
        LoggedUserDTO loggedUser = login(null);

        String expectedJson = String.format("""
            {
                "id": 2,
                "name": "changed name",
                "startDate": "2026-11-01",
                "endDate": "2027-01-01",
                "description": "test description",
                "organizerId": 3,
                "ticketTypes": [
                    {
                        "name": "type 1",
                        "quantityAvailable": 1,
                        "price": 2
                    }
                ],
                "sections": [
                    {
                        "name": "test",
                        "capacity": 1000
                    }
                ]
            }
        """);

        String request = """
            {
                "id": 2,
                "name": "changed name",
                "startDate": "2026-11-01",
                "endDate": "2027-01-01",
                "description": "test description",
                "ticketTypes": [
                    {
                        "name": "type 1",
                        "quantityAvailable": 1,
                        "price": 2
                    }
                ],
                "sections": [
                    {
                        "name": "test",
                        "capacity": 1000
                    }
                ]
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.multipart("/events/2")
            .file(new MockMultipartFile("event", "", "application/json", request.getBytes(StandardCharsets.UTF_8)))
            .header("Authorization", loggedUser.getAuthHeader())
            .with(requestBuilder -> {
                requestBuilder.setMethod("PUT");
                return requestBuilder;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    public void testDeleteReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/events/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    public void testDeleteReturns204() throws Exception {
        LoggedUserDTO loggedUser = login(null);
        mockMvc.perform(MockMvcRequestBuilders.patch("/events/1")
            .header("Authorization", loggedUser.getAuthHeader()))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
