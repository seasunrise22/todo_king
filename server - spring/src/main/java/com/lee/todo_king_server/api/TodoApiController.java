package com.lee.todo_king_server.api;

import com.lee.todo_king_server.dto.TodoDto;
import com.lee.todo_king_server.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TodoApiController {
    @Autowired
    TodoService todoService;

    // 전체 할 일 목록 조회
    @GetMapping("/api/todo/lists")
    public ResponseEntity<List<TodoDto>> lists() {
//        List<TodoDto> dtoList = todoService.lists();

        todoService.lists();
        return ResponseEntity.ok().build();
    }

    // 할 일 추가
    @PostMapping("/api/todo/create")
    public ResponseEntity<TodoDto> create(@RequestBody TodoDto dto) {
        TodoDto createdDto = todoService.create(dto);

        return (createdDto != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdDto) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
