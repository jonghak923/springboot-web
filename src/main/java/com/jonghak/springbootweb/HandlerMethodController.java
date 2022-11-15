package com.jonghak.springbootweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
