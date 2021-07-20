package com.example.todolist.repositories;

import com.example.todolist.models.ToDoList;
import com.example.todolist.models.ToDoListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
//findByNameContainingAndToDoListId
import java.util.List;
//public interface ToDoListItemRepository extends CrudRepository<ToDoListItem,Long>, JpaRepository<ToDoListItem,Long>
public interface ToDoListItemRepository extends CrudRepository<ToDoListItem,Long>, JpaRepository<ToDoListItem,Long> {
    List<ToDoListItem> findByNameContainingAndToDoListId(String name, Long toDoListId);
}
