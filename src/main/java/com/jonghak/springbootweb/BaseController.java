package com.jonghak.springbootweb;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * - @(Rest)ControllerAdvice : 예외 처리, 바인딩 설정, 모델 객체를 모든 컨트롤러 전반에 걸쳐 적용하고 싶은 경우에 사용한다. 
 *  ● RestConrollerAdvice vs ControllerAdvice : 간단하게 @ResponseBody 가 붙어 있고 아니고 차이
 *  ● @ExceptionHandler : 예외처리
 *  ● @InitBinder : 바인딩설정
 *  ● @ModelAttributes : 모델 객체 일괄설정
 * 
 * - 적용할 범위를 지정할 수도 있다.
 *  ● 특정 애노테이션을 가지고 있는 컨트롤러에만 적용하기
 *      ex) @ControllerAdvice(annotations = RestController.class)
 *  ● 특정 패키지 이하의 컨트롤러에만 적용하기
 *      ex) @ControllerAdvice("org.example.controllers")
 *  ● 특정 클래스 타입에만 적용하기
 *      ex) @ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
 *
 * - 참고
 *  ● https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-controller-advice
 *
 */
@RestControllerAdvice(assignableTypes = {HandlerMethodController.class, EventApi.class})
public class BaseController {

    /**
     * - @ExceptionHandler : 특정 예외가 발생한 요청을 처리하는 핸들러 정의
     *  ● 지원하는 메소드 아규먼트 (해당 예외 객체, 핸들러 객체, ...)
     *  ● 지원하는 리턴 값
     *  ● REST API의 경우 응답 본문에 에러에 대한 정보를 담아주고, 상태 코드를 설정하려면 ResponseEntity를 주로 사용한다.
     *
     * - 참고
     *  ● https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-exceptionhandler
     *
     */
    @ExceptionHandler
    public String eventErrorHandler(EventException exception, Model model) {
        model.addAttribute("message", "event error");
        return "events/error";
    }

//    @ExceptionHandler({EventException.class, RuntimeException.class})
//    public String runtimeErrorHandler(RuntimeException exception, Model model) {
//        model.addAttribute("message", "runtime error");
//        return "events/error";
//    }

    /**
     * - @InitBinder : 특정 컨트롤러에서 바인딩 또는 검증(validator) 설정을 변경하고 싶을 때 사용
     *
     * - 특정 모델 객체에만 바인딩 또는 Validator 설정을 적용하고 싶은 경우
     *  ● @InitBinder(“event”)
     *
     */
    @InitBinder("event")
    public void initEventBinder(WebDataBinder webDataBinder) {
        // binding 설정
        webDataBinder.setDisallowedFields("id"); // 블랙리스트 방식, 해당 명칭의 파라미터는 제외
        webDataBinder.setAllowedFields("name", "limit", "startDate"); // 화이트리스트 방식, 해당 명칭의 파라미터만 인입가능

        // validator 설정
        webDataBinder.addValidators(new EventValidatorImpl());

        // formatter 설정
//        webDataBinder.addCustomFormatter();
    }

    /**
     * - @ModelAttribute의 다른 사용법
     *  ● @RequestMapping을 사용한 핸들러 메소드의 아규먼트에 사용하기 (이미 살펴 봤습니다.)
     *  ● @Controller 또는 @ControllerAdvice (이 애노테이션은 뒤에서 다룹니다.)를 사용한 클래스에서 모델 정보를 초기화 or Default정보를 추가 할 때 사용한다.
     *  ● @RequestMapping과 같이 사용하면 해당 메소드에서 리턴하는 객체를 모델에 넣어 준다.
     *      ○ RequestToViewNameTranslator
     */
//    @ModelAttribute
//    public void categories(Model model) {
//        model.addAttribute("categories", List.of("study", "seminar", "hobby", "social"));
//    }

    @ModelAttribute("categories")
    public List<String> categories(Model model) {
        return List.of("study", "seminar", "hobby", "social");
    }

}
