package com.example.todolist.services;

import com.example.todolist.models.ToDoList;

import java.util.Set;

public interface ToDoListService {
    Set<ToDoList> getList();
    Set<ToDoList> getList(String filter, Long systemUserId);
    ToDoList save (ToDoList p);
    void delete(ToDoList p);
}
