package com.jonghak.springbootweb.book;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class Book {

    @JsonView(BookJsonView.SimpleView.class)
    private Long id;

    @JsonView(BookJsonView.SimpleView.class)
    private String isbn;

    @JsonView(BookJsonView.ComplexView.class)
    private LocalDateTime published;

    @JsonView(BookJsonView.ComplexView.class)
    private Set<Author> authors;

    @JsonView(BookJsonView.SimpleView.class)
    private String title;


}
