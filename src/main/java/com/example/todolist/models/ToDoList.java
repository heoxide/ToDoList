package com.example.todolist.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoList extends AbstractEntity {

    private String name;
    //private String surname;
    //private String phoneNumber;
    //private String groupName;
    private String date;

    @ManyToOne
    private SystemUser systemUser;

}
