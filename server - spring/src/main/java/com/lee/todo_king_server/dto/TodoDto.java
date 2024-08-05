package com.lee.todo_king_server.dto;

import com.lee.todo_king_server.entity.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class TodoDto {
    Long id;
    String text;

    public TodoEntity toEntity() {
        return new TodoEntity(this.text);
    }
}
