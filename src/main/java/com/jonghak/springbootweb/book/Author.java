package com.jonghak.springbootweb.book;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Author {

    @JsonView(BookJsonView.ComplexView.class)
    private Long id;

    @JsonView(BookJsonView.ComplexView.class)
    private String firstName;

    @JsonView(BookJsonView.ComplexView.class)
    private String lastName;

    private String email;

    private String address;

    private LocalDateTime joinedAt;

}
