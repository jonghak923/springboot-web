package com.jonghak.springbootweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class HandlerMethodControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getEvent() throws Exception {
        this.mockMvc.perform(get("/events/1;name=jonghak"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("jonghak"));
    }


    @Test
    public void getEventParam() throws Exception {
        this.mockMvc.perform(post("/eventParams?name=jonghak&limit=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("jonghak"))
                .andExpect(jsonPath("limit").value(10));
    }

    @Test
    public void getEventParamMap() throws Exception {
        this.mockMvc.perform(post("/eventParamMap")
                        .param("name", "jonghak"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("jonghak"));
    }

    @Test
    public void postEventForm() throws Exception {
        this.mockMvc.perform(get("/eventParams/form"))
                .andDo(print())
                .andExpect(view().name("events/form")) // view 이름 확인
                .andExpect(model().attributeExists("event"));  // model에 event라는 명칭 존재여부 확인
    }

    @Test
    public void eventModel() throws Exception {
        this.mockMvc.perform(post("/eventModel/name/jonghak")
                .param("limit", "-10"))
                .andDo(print())
                .andExpect(status().isOk());
    }



}