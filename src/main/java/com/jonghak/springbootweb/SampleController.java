package com.jonghak.springbootweb;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * - HTTP Method
 *  ● GET, POST, PUT, PATCH, DELETE, ...
 *
 * - GET 요청
 *  ● 클라이언트가 서버의 리소스를 요청할 때 사용한다.
 *  ● 캐싱 할 수 있다. (조건적인 GET으로 바뀔 수 있다.)
 *  ● 브라우저 기록에 남는다.
 *  ● 북마크 할 수 있다.
 *  ● 민감한 데이터를 보낼 때 사용하지 말 것. (URL에 다 보이니까)
 *  ● idempotent : 연산을 여러 번 적용하더라도 결과가 달라지지 않는 성질, 연산을 여러 번 반복하여도 한 번만 수행된 것과 같은 성질을 의미
 *
 * - POST 요청
 *  ● 클라이언트가 서버의 리소스를 수정하거나 새로 만들 때 사용한다.
 *  ● 서버에 보내는 데이터를 POST 요청 본문에 담는다.
 *  ● 캐시할 수 없다.
 *  ● 브라우저 기록에 남지 않는다.
 *  ● 북마크 할 수 없다.
 *  ● 데이터 길이 제한이 없다.
 *  ● idempotent 하지 않다 : 요청 시 마다 결과가 달라질 수 있다.
 *
 * - PUT 요청
 *  ● URI에 해당하는 데이터를 새로 만들거나 수정할 때 사용한다.
 *  ● POST와 다른 점은 “URI”에 대한 의미가 다르다.
 *      ○ POST의 URI는 보내는 데이터를 처리할 리소스를 지칭하며 -> 리소스의 생성 작업으로 요청 시 다른 리소스가 return되어 idempotent하지 않다라고 한다.
 *      ○ PUT의 URI는 보내는 데이터에 해당하는 리소스를 지칭한다. -> 특정 리소스의 수정 작업이므로 요청 시 특정 리소스 정보만 return되어 idempotent하다고 한다.
 *  ● Idempotent
 *
 * - PATCH 요청
 *  ● PUT과 비슷하지만, 기존 엔티티와 새 데이터의 차이점만 보낸다는 차이가 있다. -> 특정 리소스의 일부 정보만 수정 작업
 *  ● PUT와 다른 점은 “URI”에 대한 의미가 다르다.
 *      ○ PUT는 특정 리소스의 모든 data를 수정한다.
 *      ○ PATCH는 특정 리소스의 일부 data만 수정한다.
 *  ● Idempotent
 *
 * - DELETE 요청
 *  ● URI에 해당하는 리소스를 삭제할 때 사용한다.
 *  ● Idempotent
 *
 * - 스프링 웹 MVC에서 HTTP method 맵핑하기
 *  ● @RequestMapping(method=RequestMethod.GET)
 *  ● @RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
 *  ● @GetMapping, @PostMapping, ...
 */
@Controller
public class SampleController {

    /**
     * - URI 확장자 맵핑 지원 : hello or hello.json or hello.xml 다 호출됨
     *  ● 이 기능은 권장하지 않습니다. (스프링 부트에서는 기본으로 이 기능을 사용하지 않도록 설정 해 줌)
     *      ○ 보안 이슈 (RFD Attack : 사용자가 신뢰된 도메인으로 연결된 악의적인 링크를 통해 파일을 다운로드하고 해당 파일을 실행시킬 경우 운영체제 레벨에서 명령어 실행이 가능해진다.)
     *      ○ URI 변수, Path 매개변수, URI 인코딩을 사용할 때 할 때 불명확 함.
     *  ● URI에 .확정자를 안쓰는게 추세
     *      ○ Header의 accept 정보에 응답 시 Type을 지정함 (contentType은 요청 시 type을 지정함)
     *      ○ 차선책으로 ?type=xml 파라미터를 사용하여 구분
     */
    @RequestMapping(value = "/hello", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @GetHelloMapping
    @ResponseBody
    public String helloCustom() {
        return "helloCustom";
    }


    /**
     * - 정규 표현식으로 맵핑할 수도 있습니다.
     *  ● /{name:정규식}
     */
    @RequestMapping("/hello/{name:[a-z]+}")
    @ResponseBody
    public String regExp(@PathVariable String name) {
        return "hello " + name;
    }

    /**
     * - 요청 식별자로 맵핑하기
     *  ● @RequestMapping은 다음의 패턴을 지원합니다.
     *  ● ?: 한 글자 (“/author/???” => “/author/123”)
     *  ● *: 여러 글자 (“/author/*” => “/author/keesun”)
     *  ● **: 여러 패스 (“/author/** => “/author/keesun/book”)
     *
     * - 패턴이 중복되는 경우에는?
     *  ● 가장 구체적으로 맵핑되는 핸들러를 선택합니다.
     *  ex) /hi/** , /hi/jonghak일 경우 구체적인 /hi/jonghak이 호출됨
     */
    @RequestMapping("/hi/**")
    @ResponseBody
    public String hi() {
        return "hi jonghak";
    }

    @RequestMapping("/hi/jonghak")
    @ResponseBody
    public String hiJonghak() {
        return "hi jonghak";
    }

    /**
     * - 특정한 타입의 데이터를 담고 있는 요청만 처리하는 핸들러
     *  ● @RequestMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
     *  ● Content-Type 헤더로 필터링
     *  ● 매치 되는 않는 경우에 415 Unsupported Media Type 응답
     *
     * - class의 ReuqestMapping에 일괄로 선언 가능
     *  ● 클래스에 선언한 @RequestMapping에 사용한 것과 조합이 되지 않고 메소드에 사용한 @RequestMapping의 설정으로 덮어쓴다.
     */
    @RequestMapping(value = "/mediaType", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String mediaType() {
        return "mediaType";
    }

    /**
     * - 특정한 타입의 응답을 만드는 핸들러
     *  ● @RequestMapping(produces=”application/json”)
     *  ● Accept 헤더로 필터링 (하지만 살짝... 오묘함 : Client가 Accept값 없이 요청하는 경우 서버의 produces 설정대로 return type이 결정됨)
     *  ● 매치 되지 않는 경우에 406 Not Acceptable 응답
     */
    @RequestMapping(
            value = "/mediaTypeAccept",
            consumes = "!" + MediaType.APPLICATION_JSON_VALUE,    // 요청 시 type (header.contentType)
            produces = MediaType.TEXT_PLAIN_VALUE           // 응답 시 type (header.accept)
    )
    @ResponseBody
    public String mediaTypeAccept() {
        return "mediaTypeAccept";
    }

    /**
     * Not (!)을 사용해서 특정 미디어 타입이 아닌 경우로 맵핑 할 수도 있다
     */
    @RequestMapping(value = "/mediaTypeNot", consumes = "!" + MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String mediaTypeNot() {
        return "mediaTypeNot";
    }

    /**
     * - 특정한 헤더가 있는 요청을 처리하고 싶은 경우
     *  ● @RequestMapping(headers = “key”)
     *
     * - 특정한 헤더가 없는 요청을 처리하고 싶은 경우
     *  ● @RequestMapping(headers = “!key”)
     *
     * - 특정한 헤더 키/값이 있는 요청을 처리하고 싶은 경우
     *  ● @RequestMapping(headers = “key=value”)
     *
     */
    @GetMapping(value = "/headers", headers = HttpHeaders.AUTHORIZATION + "=111")
    @ResponseBody
    public String headers() {
        return "headers";
    }

    /**
     * - 특정한 요청 매개변수 키를 가지고 있는 요청을 처리하고 싶은 경우
     *  ● @RequestMapping(params = “a”)
     *
     * - 특정한 요청 매개변수가 없는 요청을 처리하고 싶은 경우
     *  ● @RequestMapping(params = “!a”)
     *
     * - 특정한 요청 매개변수 키/값을 가지고 있는 요청을 처리하고 싶은 경우
     *  ● @RequestMapping(params = “a=b”)
     */
    @GetMapping(value = "/params", params = "name=jonghak")
    @ResponseBody
    public String params() {
        return "headers";
    }

}
