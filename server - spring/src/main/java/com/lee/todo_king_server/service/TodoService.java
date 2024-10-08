package com.lee.todo_king_server.service;

import com.lee.todo_king_server.dto.TodoDto;
import com.lee.todo_king_server.entity.TodoEntity;
import com.lee.todo_king_server.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {
    @Autowired
    TodoRepository todoRepository;

    // 할 일 목록 생성
    @Transactional
    public TodoDto create(TodoDto dto) {
        // 클라이언트로부터 json 데이터 잘 받았나 테스트
        System.out.println(dto.toString());

        // 받은 json 데이터를 엔티티 객체로 변환
        TodoEntity itemEntity = dto.toEntity();
        System.out.println(itemEntity.toString());

        // 변환된 엔티티 객체를 JPA로 데이터베이스에 저장
        TodoEntity createdEntity = todoRepository.save(itemEntity);
        System.out.println(createdEntity.toString()); // id값 할당 잘 됐나 테스트

        // 변환된 엔티티 객체를 dto객체로 변환
        TodoDto createdDto = createdEntity.toDto();
        System.out.println(createdDto.toString());

        return createdDto;
    }

    // 전체 할 일 목록 반환
    public List<TodoDto> lists() {
        List<TodoEntity> todoList = todoRepository.findAll();
        System.out.println(todoList);

        // tdoList 하나하나 꺼내서 TodoEntity를 TodoDto로 변환
        return todoList.stream().map(TodoEntity::toDto).collect(Collectors.toList());
    }
}
