package com.jonghak.springbootweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class EventApiTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void createEventRequestBody() throws Exception {
        Event event = new Event();
        event.setName("jonghak");
        event.setLimit(20);

        String jsonParam = objectMapper.writeValueAsString(event);

        this.mockMvc.perform(post("/api/events/requestBody")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParam))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("jonghak"))
                .andExpect(jsonPath("limit").value("20"));
    }

    @Test
    void createEventHttpEntity() throws Exception {
        Event event = new Event();
        event.setName("jonghak");
        event.setLimit(20);

        String jsonParam = objectMapper.writeValueAsString(event);

        this.mockMvc.perform(post("/api/events/httpEntity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParam))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("jonghak"))
                .andExpect(jsonPath("limit").value("20"));
    }

    @Test
    void createEventResponseEntity() throws Exception {
        Event event = new Event();
        event.setName("responseEntity");
        event.setLimit(-10);

        String jsonParam = objectMapper.writeValueAsString(event);

        this.mockMvc.perform(post("/api/events/responseEntity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParam))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}