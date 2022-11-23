package com.jonghak.springbootweb;

import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
/**
 * - @SessionAttributes : 선언된 클래스 안에서만 선언된 모델명을 세션에 넣어준다.
 * - 모델 정보를 HTTP 세션에 저장해주는 애노테이션
 *  ● HttpSession을 직접 사용할 수도 있지만
 *  ● 이 애노테이션에 설정한 이름에 해당하는 모델 정보를 자동으로 세션에 넣어준다.
 *  ● @ModelAttribute는 세션에 있는 데이터도 바인딩한다.
 *  ● 여러 화면(또는 요청)에서 사용해야 하는 객체를 공유할 때 사용한다.
 *
 * - SessionStatus를 사용해서 세션 처리 완료를 알려줄 수 있다.
 *  ● 폼 처리 끝나고 세션을 비울 때 사용한다.
 *
 */
@SessionAttributes("event")
public class HandlerMethodController {

    @Autowired
    EventValidator eventValidator;

    @GetMapping("/events/error")
    public String eventError(Model model) throws EventException {

//        throw new RuntimeException();
        throw new EventException();

//        return "events/list";
    }

    @GetMapping("/events/modelAttribute")
    @ModelAttribute // 생략가능, view는 RequestToViewNameTranslator에 따라 URI(/events/modelAttribute)를 참조하여 찾음
    public Event eventsModelAttribute() {
        return new Event();
    }

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
    public Event eventModel(@Validated({Event.ValidateName.class}) @ModelAttribute("newEvent") Event event, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> {
                System.out.println(e.toString());
            });
        }
        return event;
    }

    @GetMapping("/eventsFormSubmit/formSubmit")
    public String eventsFormSubmit(Model model, HttpSession httpSession) {
        Event event = new Event();
        event.setLimit(50);
        model.addAttribute("event", event);
        httpSession.setAttribute("event", event);
        return "events/formSubmit";
    }

    /**
     * - 폼 서브밋 (에러 처리)
     * - 바인딩 에러 발생 시 Model에 담기는 정보
     *  ● Event
     *  ● BindingResult.event
     *
     * - Post / Redirect / Get 패턴
     *  ● https://en.wikipedia.org/wiki/Post/Redirect/Get
     *  ● Post 이후에 브라우저를 리프래시(새로고침) 하더라도 폼 서브밋이 발생하지 않도록 하는 패턴
     */
    @PostMapping("/eventsFormSubmit")
    public String eventsFormSubmit(@Validated({Event.ValidateLimit.class, Event.ValidateName.class}) @ModelAttribute Event event,
                                   BindingResult bindingResult,
                                   Model model,
                                   HttpSession httpSession) {
        if(bindingResult.hasErrors()) {
            return "events/formSubmit";
        }
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        model.addAttribute("eventList", eventList);

        return "redirect:events/list";
    }

    /**
     * - @SessionAttribute
     * - HTTP 세션에 들어있는 값 참조할 때 사용
     *  ● HttpSession을 사용할 때 비해 타입 컨버전을 자동으로 지원하기 때문에 조금 편리함.
     *  ● HTTP 세션에 데이터를 넣고 빼고 싶은 경우에는 HttpSession을 사용할 것.
     *
     * - @SessionAttributes와는 다르다.
     *  ● @SessionAttributes는 해당 컨트롤러 내에서만 동작.
     *      ○ 즉, 해당 컨트롤러 안에서 다루는 특정 모델 객체를 세션에 넣고 공유할 때 사용.
     *  ● @SessionAttribute는 컨트롤러 밖(인터셉터 또는 필터 등)에서 만들어 준 세션 데이터에 접근할 때 사용한다.
     */
    @GetMapping("/events/list")
    public String getEvents(@ModelAttribute("redirectEvent") Event newEvent, // @SessionAttributes에 선언한 명칭과 동일하게 하면 session에 먼저 찾음, 없는 경우 오류
                            @ModelAttribute("flashEvent") Event flashEvent, // session 등록하여 전달한 후 삭제됨 (일회성)
                            Model model,
                            HttpSession httpSession,
                            @SessionAttribute LocalDateTime visitTime) {

        System.out.println("@SessionAttribute visitTime :" + visitTime.toString());
        LocalDateTime httpSessionVisitTime = (LocalDateTime) httpSession.getAttribute("visitTime");
        System.out.println("httpSessionVisitTime visitTime :" + httpSessionVisitTime.toString());


        // redirct, flash로 전달된 객체는 모델에서도 꺼낼 수 있음
        Event redirectEventFromModel = (Event) model.asMap().get("redirectEvent");
        Event flashEventFromModel = (Event) model.asMap().get("flashEvent");

        Event event = new Event();
        event.setName("spring");
        event.setLimit(10);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        eventList.add(newEvent);
        eventList.add(flashEvent);

        model.addAttribute(eventList);

        return "events/list";
    }

    /**
     * - 멀티 폼 서브밋
     * - 세션을 사용해서 여러 폼에 걸쳐 데이터를 나눠 입력 받고 저장하기
     *  ● 이벤트 이름 입력받고
     *  ● 이벤트 제한 인원 입력받고
     *  ● 서브밋 -> 이벤트 목록으로!
     *
     * - 완료된 경우에 세션에서 모델 객체 제거하기
     *  ● SessionStatus : SessionStatus를 사용해서 세션 처리 완료를 알려줄 수 있다.
     *      ex) SessionStatus sessionStatus를 메소드 파라미터 부분에 선언하고 sessionStatus.setComplete(); 호출하면
     *          @SessionAttributes로 등록된 세션이 없어진다. (다른 방법으로 등록된 세션정보는 유지됨)
     *
     */
    @GetMapping("/events/form/name")
    public String eventsFormName(Model model, HttpSession httpSession) {
        Event newEvent = new Event();
        newEvent.setLimit(50);
        model.addAttribute("event", newEvent);
        httpSession.setAttribute("limit", "50");
        return "events/form-name";
    }

    @PostMapping("/events/form/name")
    public String eventsFormNameSubmit(@Validated({Event.ValidateName.class, Event.ValidateLimit.class}) @ModelAttribute Event event,
                                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "events/form-name";
        }

        // 특정 시점에 Validator을 실행하고 싶을 경우
//        eventValidator.validate(event, bindingResult);
//        if(bindingResult.hasErrors()) {
//            return "events/form-name";
//        }

        return "redirect:/events/form/limit";
    }

    @GetMapping("/events/form/limit")
    public String eventsFormLimit(Model model) {
        model.addAttribute("event", model.getAttribute("event"));
        return "events/form-limit";
    }

    @PostMapping("/events/form/limit")
    public String eventsFormLimitSubmit(@Validated({Event.ValidateName.class, Event.ValidateLimit.class}) @ModelAttribute Event event,
                                        BindingResult bindingResult,
                                        SessionStatus sessionStatus,
                                        HttpSession httpSession,
                                        RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "events/form-limit";
        }

        // session 비움
        sessionStatus.setComplete();

        /**
         * - RedirectAttributes : 리다이렉트 할 때 기본적으로 Model에 들어있는 primitive type 데이터는 URI 쿼리 매개변수에 추가된다.
         *  ● 스프링 부트에서는 이 기능이 기본적으로 비활성화 되어 있다.
         *  ● Ignore-default-model-on-redirect 프로퍼티를 사용해서 활성화 할 수 있다.
         *
         * - 원하는 값만 리다이렉트 할 때 전달하고 싶다면 RedirectAttributes에 명시적으로 추가할 수 있다. (URI 쿼리 파라미터)
         * - 리다이렉트 요청을 처리하는 곳에서 쿼리 매개변수를 @RequestParam 또는 @ModelAttribute로 받을 수 있다.
         */
        redirectAttributes.addAttribute("name", event.getName());
        redirectAttributes.addAttribute("limit", event.getLimit());

        /**
         * - Flash Attributes : 주로 리다이렉트시에 데이터를 전달할 때 사용한다.
         *  ● 데이터가 URI에 노출되지 않는다.
         *  ● 임의의 객체를 저장할 수 있다.
         *  ● 보통 HTTP 세션을 사용한다.
         * - 리다이렉트 하기 전에 데이터를 HTTP 세션에 저장하고 리다이렉트 요청을 처리 한 다음 그 즉시 제거한다.
         * - RedirectAttributes를 통해 사용할 수 있다.
         */
        redirectAttributes.addFlashAttribute("flashEvent", event);

        return "redirect:/events/list";
    }

}
