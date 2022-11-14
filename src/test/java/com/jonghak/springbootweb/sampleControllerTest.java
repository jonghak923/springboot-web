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
class sampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void hello() throws Exception {
        this.mockMvc.perform(get("/hello"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/hello"))
                .andDo(print())
                .andExpect(status().isOk());

        // 405 error
        this.mockMvc.perform(put("/hello"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void regExp() throws Exception {
        this.mockMvc.perform(get("/hello/jonghak"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hello jonghak"));
    }

    @Test
    void hi() throws Exception {
        this.mockMvc.perform(get("/hi/jonghak"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hi jonghak"))
                .andExpect(handler().handlerType(SampleController.class)) // 요청 받는 Handler의 클래스명 확인
                .andExpect(handler().methodName("hiJonghak"));  // 요청 받는 Handler의 함수명 확인
    }
}