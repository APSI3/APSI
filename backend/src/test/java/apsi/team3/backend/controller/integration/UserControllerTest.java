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
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    static ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser1", "apsi");
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSession() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser1", "apsi");
        String stringLoginRequest = objectMapper.writeValueAsString(loginRequest);
        String responseContent = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringLoginRequest)
                )
                .andReturn().getResponse().getContentAsString();
        LoggedUserDTO loggedUserDTO = objectMapper.readValue(responseContent, LoggedUserDTO.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/session").header("Authorization", loggedUserDTO.getAuthHeader()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("true"));
    }
}
