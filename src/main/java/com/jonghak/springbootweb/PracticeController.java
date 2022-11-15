package com.jonghak.springbootweb;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PracticeController {

    @RequestMapping(method = RequestMethod.GET, value = "/events/{id}")
    @ResponseBody
    public String firstMethod(@PathVariable String id) {
        return "firstMethod " + id;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String twoMethod() {
        return "twoMethod";
    }

    @DeleteMapping("/events/{id}")
    @ResponseBody
    public String threeMethod(@PathVariable String id) {
        return "threeMethod " + id;
    }

    @PutMapping(value = "/events/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String fourMethod(@PathVariable String id) {
        return "fourMethod " + id;
    }

}
