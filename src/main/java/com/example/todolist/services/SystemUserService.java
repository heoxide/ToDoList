package com.example.todolist.services;

import com.example.todolist.models.SystemUser;

public interface SystemUserService {
    SystemUser login(String email, String password);
    SystemUser save(SystemUser systemUser);
}
