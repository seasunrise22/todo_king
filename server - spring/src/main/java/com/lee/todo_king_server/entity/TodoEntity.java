package com.lee.todo_king_server.entity;

import com.lee.todo_king_server.dto.TodoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "todo")
@Entity
@ToString
@Getter
@NoArgsConstructor
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String text;

    public TodoEntity(String text) {
        this.text = text;
    }

    public TodoDto toDto() {
        return new TodoDto(this.getId(), this.getText());
    }
}
