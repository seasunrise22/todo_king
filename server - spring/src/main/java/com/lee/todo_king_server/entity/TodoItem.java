package com.lee.todo_king_server.entity;

import jakarta.persistence.*;

@Table(name = "todoitem")
@Entity
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
}
