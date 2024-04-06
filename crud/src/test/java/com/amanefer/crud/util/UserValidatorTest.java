package com.amanefer.crud.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.amanefer.crud.entities.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserValidatorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void valid() throws Exception {
        User user = new User();
        user.setUsername("correct5465");
//        user.setPassword("password");
        String userJson = objectMapper.writeValueAsString(user);
        ResultActions result = mockMvc.perform(post("/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .param("selectedRole", "ROLE_ADMIN"));

        result.andExpect(status().isOk());
 }

    @Test
    public void validError() throws Exception {
        User user = new User();
//        user.setUsername("correct");
//        user.setPassword("password");
        String userJson = objectMapper.writeValueAsString(user);
        ResultActions result = mockMvc.perform(post("/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .param("selectedRole", "ROLE_ADMIN"));

        result.andExpect(status().is4xxClientError());
    }

}
