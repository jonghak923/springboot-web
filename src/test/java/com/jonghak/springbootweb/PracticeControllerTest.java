package com.jonghak.springbootweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class PracticeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void firstMethod() throws Exception {
        this.mockMvc.perform(get("/practice/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void twoMethod() throws Exception {
        this.mockMvc.perform(post("/practice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void threeMethod() throws Exception {
        this.mockMvc.perform(delete("/practice/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void fourMethod() throws Exception {
        this.mockMvc.perform(put("/practice/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}