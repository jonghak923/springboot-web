package com.jonghak.springbootweb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class HandlerMethodController {

    /**
     * - @PathVariable
     *  ● 요청 URI 패턴의 일부를 핸들러 메소드 아규먼트로 받는 방법.
     *  ● 타입 변환 지원.
     *  ● (기본)값이 반드시 있어야 한다. : @PathVariable(required = false)로 주면 반드시 없어도 됨
     *  ● Optional 지원.
     *
     * - @MatrixVariable
     *  ● 요청 URI 패턴에서 키/값 쌍의 데이터를 메소드 아규먼트로 받는 방법
     *  ● 타입 변환 지원.
     *  ● (기본)값이 반드시 있어야 한다.
     *  ● Optional 지원.
     *  ● 이 기능은 기본적으로 비활성화 되어 있음. 활성화 하려면 다음과 같이 설정해야 함.
     *    (WebConfig.configurePathMatch의 urlPathHelper.setRemoveSemicolonContetnt(false)로 되어 있어야함)
     */
    @GetMapping("/events/{id}")
    @ResponseBody
    public Event getEvent(@PathVariable Integer id, @MatrixVariable String name) {
        Event event = new Event();
        event.setId(id);
        event.setName(name);
        return event;

    }

    /**
     * - @RequestParam
     *  ● 요청 매개변수에 들어있는 단순 타입 데이터를 메소드 아규먼트로 받아올 수 있다.
     *  ● 값이 반드시 있어야 한다.
     *      ○ required=false 또는 Optional을 사용해서 부가적인 값으로 설정할 수도 있다.
     *  ● String이 아닌 값들은 타입 컨버전을 지원한다.
     *  ● Map<String, String> 또는 MultiValueMap<String, String>에 사용해서 모든 요청 매개변수를 받아 올 수도 있다.
     *  ● 이 애노테이션은 생략 할 수 잇다.
     *
     * - 요청 매개변수란?
     *  ● 쿼리 매개변수 : http://localhost:8080?name=jonghak
     *  ● 폼 데이터 :
     */
    @PostMapping("/eventParams")
    @ResponseBody
    public Event getEventParams(@RequestParam String name,
                                @RequestParam Integer limit) {
        Event event = new Event();
        event.setName(name);
        event.setLimit(limit);
        return event;

    }

    @PostMapping("/eventParamMap")
    @ResponseBody
    public Event getEventParams(@RequestParam Map<String, String> params) {
        Event event = new Event();
        event.setName(params.get("name"));
        return event;

    }

    @GetMapping("/eventParams/form")
    public String eventsForm(Model model) {
        Event event = new Event();
        event.setLimit(50);
        model.addAttribute("event", event);
        return "events/form";
    }


    /**
     * - @ModelAttribute
     *  ● 여러 곳에 있는 단순 타입 데이터를 복합 타입 객체로 받아오거나 해당 객체를 새로 만들 때 사용할 수 있다.
     *  ● 여러 곳? URI 패스, 요청 매개변수, 세션 등
     *  ● 생략 가능
     *
     * - 값을 바인딩 할 수 없는 경우에는?
     *  ● BindException 발생 400 에러
     *
     * - 바인딩 에러를 직접 다루고 싶은 경우
     *  ● BindingResult 타입의 아규먼트를 바로 오른쪽!!에 추가한다.
     *
     * - 바인딩 이후에 검증 작업을 추가로 하고 싶은 경우
     *  ● @Valid 또는 @Validated 애노테이션을 사용한다.
     *
     * - 참고
     *  ● https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     *
     * - @Validated
     *  ● 스프링 MVC 핸들러 메소드 아규먼트에 사용할 수 있으며 validation group이라는 힌트를 사용할 수 있다.
     *  ● @Valid 애노테이션에는 그룹을 지정할 방법이 없다.
     *  ● @Validated는 스프링이 제공하는 애노테이션으로 그룹 클래스를 설정할 수 있다.
     *
     */
    @PostMapping("/eventModel/name/{name}")
    @ResponseBody
    public Event eventModel(@Validated({Event.ValidateName.class}) @ModelAttribute Event event, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> {
                System.out.println(e.toString());
            });
        }
        return event;
    }

}
