package com.example.todolist.services;

import com.example.todolist.models.ToDoList;
import com.example.todolist.models.ToDoListItem;

import java.util.Set;

public interface ToDoListItemService {
    Set<ToDoListItem> getToDoListItems();
    Set<ToDoListItem> getToDoListItems(String filter, Long toDoListId);
    Set<ToDoListItem> getToDoListItems(Long toDoListId);
    ToDoListItem save (ToDoListItem tdli);
    void delete(ToDoListItem tdli);
}
