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
}
