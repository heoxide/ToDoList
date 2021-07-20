package com.example.todolist.repositories;

import com.example.todolist.models.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToDoListRepository extends CrudRepository<ToDoList,Long>, JpaRepository<ToDoList,Long> {
    List<ToDoList> findByNameContainingAndSystemUserId(String name, Long toDoListId);
}
