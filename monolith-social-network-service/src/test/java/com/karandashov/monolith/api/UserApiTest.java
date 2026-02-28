package com.karandashov.monolith.api;

import com.karandashov.monolith.BaseWithPostgresTest;
import com.karandashov.monolith.dto.request.RegisterRequest;
import com.karandashov.monolith.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
public class UserApiTest extends BaseWithPostgresTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void successRegisterTest() throws Exception {
        String request = """
                {
                  "password": "test-password",
                  "first_name": "test-first_name",
                  "second_name": "test-second_name",
                  "birthdate": "2026-02-28",
                  "biography": "test-biography",
                  "city": "test-city"
                }
                """;

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_id").exists());
    }

    @Test
    void successLoginTest() throws Exception {

        var registerResponse = userService.register(new RegisterRequest(
                "test-password",
                "test-first_name",
                "test-second_name",
                LocalDate.now(),
                "test-biography",
                "test-city"
        ));
        var userId = registerResponse.userId();

        String request = """
                {
                  "id": "$userId",
                  "password": "test-password"
                }
                """;
        request = request.replace("$userId", userId.toString());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void successMeTest() throws Exception {
        var registerResponse = userService.register(new RegisterRequest(
                "test-password",
                "FirstName",
                "LastName",
                LocalDate.now(),
                "test-biography",
                "test-city"
        ));
        var userId = registerResponse.userId();

        var loginResponse = userService.login(new com.karandashov.monolith.dto.request.LoginRequest(
                userId,
                "test-password"
        ));
        var token = loginResponse.token();

        mockMvc.perform(post("/me")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.first_name").value("FirstName"))
                .andExpect(jsonPath("$.second_name").value("LastName"));
    }

    @Test
    void successGetUserTest() throws Exception {
        var registerResponse = userService.register(new RegisterRequest(
                "test-password",
                "FirstName",
                "LastName",
                LocalDate.now(),
                "test-biography",
                "test-city"
        ));
        var userId = registerResponse.userId();

        mockMvc.perform(get("/user/get/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.first_name").value("FirstName"))
                .andExpect(jsonPath("$.second_name").value("LastName"));
    }

    @Test
    void successSearchUserTest() throws Exception {
        userService.register(new RegisterRequest(
                "test-password",
                "Konstantin",
                "Osipov",
                LocalDate.now(),
                "biography",
                "Moscow"
        ));

        mockMvc.perform(get("/user/search")
                        .param("first_name", "Konst")
                        .param("last_name", "Osi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].first_name").value("Konstantin"))
                .andExpect(jsonPath("$[0].second_name").value("Osipov"));
    }
}
