package com.lee.todo_king.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class TodoDto {
    Long id;
    String text;

    public TodoDto(String text) {
        this.text = text;
    }
}
