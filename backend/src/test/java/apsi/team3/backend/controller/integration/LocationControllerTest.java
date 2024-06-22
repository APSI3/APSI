package apsi.team3.backend.controller.integration;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class LocationControllerTest {

    @Autowired
    MockMvc mockMvc;

    static ObjectMapper objectMapper;
    final static String LOGIN = "organizer";
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
    public void testGetAllLocationsReturns401ForUnauthorizedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/locations"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testGetLocationsPageableReturnsListOfSomeLocationsFromTestDB() throws Exception {
        LoggedUserDTO loggedUser = login();
        String expectedJson = """
            {
                "items":[
                    {
                        "id":1,
                        "country_id":1,
                        "capacity":55000,
                        "description":"PGE Narodowy",
                        "city":"Warszawa",
                        "street":"al. Księcia Józefa Poniatowskiego",
                        "building_nr":"1",
                        "apartment_nr":null,
                        "zip_code":"03-901",
                        "creator_id":3
                    },
                    {
                        "id":2,
                        "country_id":1,
                        "capacity":4806,
                        "description":"COS Torwar",
                        "city":"Warszawa",
                        "street":"Łazienkowska",
                        "building_nr":"6A",
                        "apartment_nr":null,
                        "zip_code":"00-449",
                        "creator_id":3
                    },
                    {
                        "id":3,
                        "country_id":1,
                        "capacity":90000,
                        "description":"Wembley Stadium",
                        "city":"Londyn",
                        "street":"11th Floor York House, Empire Way, Wembley",
                        "building_nr":null,
                        "apartment_nr":null,
                        "zip_code":"HA9 0WS",
                        "creator_id":3
                    }
                ],
                "pageIndex":0,
                "totalItems":3,
                "totalPages":1
            }
        """;
        mockMvc.perform(MockMvcRequestBuilders.get("/locations/pageable?pageIndex=0").header("Authorization", loggedUser.getAuthHeader()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }
}
