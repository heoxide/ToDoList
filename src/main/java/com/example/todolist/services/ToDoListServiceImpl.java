package com.example.todolist.services;

import com.example.todolist.models.ToDoList;
import com.example.todolist.repositories.ToDoListRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ToDoListServiceImpl implements ToDoListService {
    private final ToDoListRepository toDoListRepository;
    public ToDoListServiceImpl(ToDoListRepository toDoListRepository) {
        this.toDoListRepository = toDoListRepository;
    }
    @Override
    public Set<ToDoList> getList() {
        Set<ToDoList> toDoListSet =new HashSet<>();
        toDoListRepository.findAll().iterator().forEachRemaining(toDoListSet::add);
        return toDoListSet;
    }
    @Override
    public Set<ToDoList> getList(String filter, Long systemUserId) {
        Set<ToDoList> toDoListSet =new HashSet<>();
        toDoListRepository.findByNameContainingAndSystemUserId(filter,systemUserId).iterator().forEachRemaining(toDoListSet::add);
        return toDoListSet;
    }
    @Override
    public ToDoList save(ToDoList p) {
        return toDoListRepository.save(p);
    }

    @Override
    public void delete(ToDoList p) {
        toDoListRepository.delete(p);
    }
}
