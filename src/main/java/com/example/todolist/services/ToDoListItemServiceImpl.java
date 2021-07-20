package com.example.todolist.services;

import com.example.todolist.models.ToDoList;
import com.example.todolist.models.ToDoListItem;
import com.example.todolist.repositories.ToDoListItemRepository;
import com.example.todolist.repositories.ToDoListRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
public  class ToDoListItemServiceImpl implements ToDoListItemService{
    private final ToDoListItemRepository toDoListItemRepository;

    public ToDoListItemServiceImpl(ToDoListItemRepository toDoListItemRepository) {
        this.toDoListItemRepository = toDoListItemRepository;
    }

    @Override
    public Set<ToDoListItem> getToDoListItems() {
        Set<ToDoListItem> toDoListItemSet =new HashSet<>();
        toDoListItemRepository.findAll().iterator().forEachRemaining(toDoListItemSet::add);
        return toDoListItemSet;
    }

    @Override
    public Set<ToDoListItem> getToDoListItems(String filter, Long toDoListId) {
        Set<ToDoListItem> toDoListItemSet =new HashSet<>();
        toDoListItemRepository.findByNameContainingAndToDoListId(filter, toDoListId).iterator().forEachRemaining(toDoListItemSet::add);
        return toDoListItemSet;
    }

    @Override
    public Set<ToDoListItem> getToDoListItems(Long toDoListId) {
        return null;
    }


    @Override
    public ToDoListItem save(ToDoListItem tdli) {
        return toDoListItemRepository.save(tdli);
    }

    @Override
    public void delete(ToDoListItem tdli) {
        toDoListItemRepository.delete(tdli);
    }
}
