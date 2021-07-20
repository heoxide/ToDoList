package com.example.todolist.views;

import com.example.todolist.models.SystemUser;
import com.example.todolist.services.SystemUserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route
public class LoginView extends VerticalLayout {
    private final SystemUserService systemUserService;
    public LoginView(SystemUserService systemUserService){
        this.systemUserService = systemUserService;

        addClassName("login-box");

        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(loginEvent -> {

            SystemUser result = systemUserService.login(loginEvent.getUsername(),loginEvent.getPassword());

            if (result.getId()!=null)
            {
                VaadinSession.getCurrent().getSession().setAttribute("LoggedInSystemUserId",result.getId());
                UI.getCurrent().getPage().setLocation("/");
            }else
            {
                loginForm.setError(true);
            }
        });

        add(loginForm);
    }
}
