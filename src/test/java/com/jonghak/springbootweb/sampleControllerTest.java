package com.jonghak.springbootweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @Test
    public void mediaType() throws Exception {
        this.mockMvc.perform(get("/mediaType")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("mediaType"));


        // 415 error
        this.mockMvc.perform(get("/mediaType")
                        .contentType(MediaType.APPLICATION_XML))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());

    }

    @Test
    void mediaTypeAccept() throws Exception {
        this.mockMvc.perform(get("/mediaTypeAccept")
                        .contentType(MediaType.APPLICATION_JSON)  // 요청 시 type
                        .accept(MediaType.TEXT_PLAIN)                   // 응답 시 type, 없으면 서버의 produces 설정에 따라 응답 type이 설정됨
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("mediaTypeAccept"));

        // 406 error
        this.mockMvc.perform(get("/mediaTypeAccept")
                        .contentType(MediaType.APPLICATION_JSON)  // 요청 시 type
                        .accept(MediaType.APPLICATION_JSON)                   // 응답 시 type, 없으면 서버의 produces 설정에 따라 응답 type이 설정됨
                )
                .andDo(print())
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void mediaTypeNot() throws Exception {
        this.mockMvc.perform(get("/mediaTypeNot")
                        .contentType(MediaType.APPLICATION_XML)  // 요청 시 type
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("mediaTypeNot"));

        // 415 error
        this.mockMvc.perform(get("/mediaTypeAccept")
                        .contentType(MediaType.APPLICATION_JSON)  // 요청 시 type
                )
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void headers() throws Exception {
        this.mockMvc.perform(get("/headers")
                        .header(HttpHeaders.AUTHORIZATION, "111")
                )
                .andDo(print())
                .andExpect(status().isOk());

        // 404 error
        this.mockMvc.perform(get("/headers")
                        .header(HttpHeaders.AUTHORIZATION, "222")
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void params() throws Exception {
        this.mockMvc.perform(get("/params")
                        .param("name", "jonghak"))
                .andDo(print())
                .andExpect(status().isOk());

        // 400 error
        this.mockMvc.perform(get("/params"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}