package com.jonghak.springbootweb;

import com.sun.net.httpserver.Headers;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventApi {

    /**
     * - @ExceptionHandler : 특정 예외가 발생한 요청을 처리하는 핸들러 정의
     *  ● REST API의 경우 응답 본문에 에러에 대한 정보를 담아주고, 상태 코드를 설정하려면 ResponseEntity를 주로 사용한다.
     *
     * - 참고
     *  ● https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-exceptionhandler
     */
    @ExceptionHandler
    public ResponseEntity errorHandler() {
        return ResponseEntity.badRequest().body("can't create event as .... ");
    }

    /**
     * - @RequestBody
     *  ● 요청 본문(body)에 들어있는 데이터를 HttpMessageConveter를 통해 변환한 객체로 받아올 수 있다.
     *  ● @Valid 또는 @Validated를 사용해서 값을 검증 할 수 있다.
     *  ● BindingResult 아규먼트를 사용해 코드로 바인딩 또는 검증 에러를 확인할 수 있다.
     */
    @PostMapping("/requestBody")
    public Event createEventRequestBody(@RequestBody @Validated({Event.ValidateLimit.class, Event.ValidateName.class}) Event event,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error);
            });
        }
        return event;
    }

    /**
     * - HttpEntity
     *  ● @RequestBody와 비슷하지만 추가적으로 요청 헤더 정보를 사용할 수 있다.
     */
    @PostMapping("/httpEntity")
    public Event createEventHttpEntity(HttpEntity<Event> request) {
        MediaType contentType = request.getHeaders().getContentType();
        System.out.println(contentType);
        return request.getBody();
    }

    /**
     * - @ResponseBody
     * ● 데이터를 HttpMessageConverter를 사용해 응답 본문 메시지로 보낼 때 사용한다.
     * ● @RestController 사용시 자동으로 모든 핸들러 메소드에 적용된다.
     *
     * - ResponseEntity
     *  ● 응답 헤더 상태 코드 본문을 직접 다루고 싶은 경우에 사용한다.
     *  ● responseEntity를 return할 때 body를 채우거나 .build()를 해야함!!
     */
    @PostMapping("/responseEntity")
    public ResponseEntity createEventResponseEntity(@RequestBody @Validated({Event.ValidateLimit.class, Event.ValidateName.class}) Event event,
                                                           BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error);
            });
            // body를 채우거나
            //return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            // .build() 해야함
            return ResponseEntity.badRequest().build();
        }

        // create process

        return ResponseEntity.ok(event);
    }
}
