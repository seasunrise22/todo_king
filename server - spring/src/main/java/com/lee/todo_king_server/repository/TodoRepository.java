package com.lee.todo_king_server.repository;

import com.lee.todo_king_server.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
}
