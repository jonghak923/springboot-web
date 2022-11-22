package com.jonghak.springbootweb.book;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * - JSON View
 *  ● 도메인 또는 DTO 클래스를 이용해서 다양한 JSON 뷰를 만들 수 있는 기능!!
 *
 * - 전제조건
 *  ● spring.jackson.mapper.default-view-inclusion=false 이여야 함
 *  ● Spring Boot는 기본설정이 false라 영향도 없음
 *
 * - 참고
 *  ● https://github.com/FasterXML/jackson-databind/wiki/JacksonFeatures
 *  ● https://github.com/FasterXML/jackson-databind/wiki/Mapper-Features
 *  ● https://spring.io/blog/2014/12/02/latest-jackson-integration-improvements-in-spring
 */
@RestController
public class BookController {

    @GetMapping("/books")
    @JsonView(BookJsonView.ComplexView.class)
    public List<Book> getBooks() {
        return books();
    }

    private List<Book> books() {
        Author author = new Author();
        author.setFirstName("jonghak");
        author.setLastName("choi");
        author.setEmail("jonghak@email.com");
        author.setId(100l);
        author.setAddress("경기도 군포시 부곡동");
        author.setJoinedAt(LocalDateTime.now());

        Book book = new Book();
        book.setId(1l);
        book.setTitle("spring boot");
        book.setIsbn("skdjfkjsdkfsd");
        book.setPublished(LocalDateTime.now());
        book.setAuthors(Set.of(author));

        return List.of(book);
    }
}
