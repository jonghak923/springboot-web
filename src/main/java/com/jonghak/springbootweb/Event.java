package com.jonghak.springbootweb;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class Event {

    interface ValidateName {};
    interface ValidateLimit {};

    private Integer id;

    @NotEmpty(groups = ValidateName.class)
    private String name;

    @Min(value=0, groups = {ValidateLimit.class})
    private Integer limit;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future(message = "That's impossible.")
    private LocalDate startDate;
}
