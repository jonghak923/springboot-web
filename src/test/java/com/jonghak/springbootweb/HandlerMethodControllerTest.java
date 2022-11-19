package com.jonghak.springbootweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
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

    @Test
    public void eventsFormSubmit() throws Exception {
        MockHttpServletRequest request = this.mockMvc.perform(get("/eventsFormSubmit/formSubmit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("events/formSubmit"))
                .andExpect(model().attributeExists("event"))
                .andExpect(request().sessionAttribute("event", notNullValue()))
                .andReturn().getRequest();

        Object event = request.getSession().getAttribute("event");
        System.out.println(event);

    }

    @Test
    public void postEventsFormSubmit() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post("/eventsFormSubmit")
                        .param("name", "jonghak")
                        .param("limit", "-10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());

        // TEST에서 modelAndView 정보를 체크하고 싶을때 아래와 같이 확인
        ModelAndView modelAndView = resultActions.andReturn().getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        System.out.println(model.size());
    }

    @Test
    public void redirectFlashEvent() throws Exception {
        Event flashEvent = new Event();
        flashEvent.setName("Flash Event");
        flashEvent.setLimit(10);

        this.mockMvc.perform(get("/events/list")
                    .sessionAttr("visitTime", LocalDateTime.now())
                    .flashAttr("flashEvent", flashEvent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//p").nodeCount(2));

        /**
         * - XPath
         *  ● https://www.w3schools.com/xml/xpath_syntax.asp
         *  ● https://www.freeformatter.com/xpath-tester.html#ad-output
         */
    }


}