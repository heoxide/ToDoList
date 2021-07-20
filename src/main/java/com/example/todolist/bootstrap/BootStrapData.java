package com.example.todolist.bootstrap;

import com.example.todolist.models.ToDoList;
import com.example.todolist.models.SystemUser;
import com.example.todolist.models.ToDoListItem;
import com.example.todolist.services.ToDoListItemService;
import com.example.todolist.services.ToDoListService;
import com.example.todolist.services.SystemUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {
    private final ToDoListService toDoListService;
    private final SystemUserService systemUserService;
    private final ToDoListItemService toDoListItemService;

    public BootStrapData(ToDoListService toDoListService, SystemUserService systemUserService, ToDoListItemService toDoListItemService) {
        this.toDoListService = toDoListService;
        this.systemUserService = systemUserService;
        this.toDoListItemService = toDoListItemService;
    }


    @Override
    public void run(String... args) throws Exception {


        SystemUser systemUser=new SystemUser();
        systemUser.setEmail("admin");
        systemUser.setPassword("123");
        systemUserService.save(systemUser);


        SystemUser systemUser2=new SystemUser();
        systemUser2.setEmail("admin2");
        systemUser2.setPassword("12345");
        systemUserService.save(systemUser2);

        ToDoList toDoList =new ToDoList();
        toDoList.setName("Malzemeler");
        toDoList.setDate("10/03/2021");
        //toDoList.setPhoneNumber("0544 444 4444");
        toDoList.setSystemUser(systemUser);
        toDoListService.save(toDoList);

        ToDoListItem toDoListItem = new ToDoListItem();
        toDoListItem.setName("deneme1");
        toDoListItem.setContent("miriba");
        toDoListItem.setToDoList(toDoList);
        toDoListItemService.save(toDoListItem);

        ToDoList toDoList2 =new ToDoList();
        toDoList2.setName("Alışveriş Listesi");
        toDoList2.setDate("15/09/2021");
        //toDoList2.setPhoneNumber("0544 555 5555");
        toDoList2.setSystemUser(systemUser);
        toDoListService.save(toDoList2);

        ToDoList toDoList3 =new ToDoList();
        toDoList3.setName("Yapılacaklar");
        toDoList3.setDate("02/07/2021");
        //toDoList3.setPhoneNumber("0544 555 5555");
        toDoList3.setSystemUser(systemUser2);
        toDoListService.save(toDoList3);


    }
}
