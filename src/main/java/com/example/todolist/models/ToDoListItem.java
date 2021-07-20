package com.example.todolist.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoListItem extends AbstractEntity {


    private String name;
    private String content;

    @ManyToOne
    private ToDoList toDoList;
}
