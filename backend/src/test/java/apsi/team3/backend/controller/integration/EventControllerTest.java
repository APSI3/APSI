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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    public void testGetAllEventsReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testGetAllEventsReturnsListOfAllEventsFromTestDB() throws Exception {
        LoggedUserDTO loggedUser = login();
        String expectedJson = """
            [
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
        """;
        mockMvc.perform(MockMvcRequestBuilders.get("/events").header("Authorization", loggedUser.getAuthHeader()))
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
        LoggedUserDTO loggedUserDTO = login();
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
        LoggedUserDTO loggedUser = login();
        String expectedJson = """
            {
                "id": 3,
                "name": "test name",
                "startDate": "2026-11-01",
                "endDate": "2027-01-01",
                "description": "test description",
                "organizerId": 1
            }
        """;
        String request = """
            {
                "name": "test name",
                "startDate": "2026-11-01",
                "endDate": "2027-01-01",
                "description": "test description"
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .header("Authorization", loggedUser.getAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
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
        LoggedUserDTO loggedUser = login();
        String expectedJson = """
            {
                "id": 2,
                "name": "changed name",
                "startDate": "2026-11-01",
                "endDate": "2027-01-01",
                "description": "test description",
                "organizerId": 3
            }
        """;
        String request = """
            {
                "id": 2,
                "name": "changed name",
                "startDate": "2026-11-01",
                "endDate": "2027-01-01",
                "description": "test description",
                "organizerId": 3
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.put("/events/2")
                        .header("Authorization", loggedUser.getAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testDeleteReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    public void testDeleteReturns204() throws Exception {
        LoggedUserDTO loggedUser = login();
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/1")
                        .header("Authorization", loggedUser.getAuthHeader()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
