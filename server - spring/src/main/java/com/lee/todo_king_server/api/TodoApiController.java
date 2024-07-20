package com.lee.todo_king_server.api;

import com.lee.todo_king_server.dto.TodoForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoApiController {

    // 전체 할 일 목록 조회
    @GetMapping("/api/todo/lists")
    public String lists() {
        return "todoList return";
    }

    // 할 일 추가
    @PostMapping("/api/todo/create")
    public ResponseEntity<TodoForm> create(@RequestBody TodoForm dto) {
        System.out.println(dto.toString());

        return ResponseEntity.ok(dto);
    }
}
