package com.jonghak.springbootweb;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class Event {

    interface ValidateName {};
    interface ValidateLimit {};

    private int id;

    @NotEmpty(groups = ValidateName.class)
    private String name;

    @Min(value=0, groups = {ValidateLimit.class})
    private Integer limit;
}
