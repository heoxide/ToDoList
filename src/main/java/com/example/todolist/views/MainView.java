package com.example.todolist.views;

import com.example.todolist.models.SystemUser;
import com.example.todolist.models.ToDoList;
import com.example.todolist.models.ToDoListItem;
import com.example.todolist.services.ToDoListItemService;
import com.example.todolist.services.ToDoListService;
import com.example.todolist.services.ToDoListServiceImpl;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.mail.*;

import java.util.ArrayList;
import java.util.List;

@Route
@CssImport("./themes/myapp/styles.css")
public class MainView extends VerticalLayout {
    private static ToDoListService toDoListService;
    private final ToDoListItemService toDoListItemService;

    Grid<ToDoList> grid = new Grid<>(ToDoList.class);
    Grid<ToDoListItem> gridItem = new Grid<>(ToDoListItem.class);
    TextField txtFilter = new TextField();
    TextField txtItemFilter = new TextField();
    TextField txtRecieverMail = new TextField();

    Dialog dialogGroup = new Dialog();
    Dialog dialogView = new Dialog();
    Dialog dialogItemUpdate = new Dialog();
    Dialog dialogShareMail = new Dialog();
    Dialog dialogNewItem = new Dialog();

    Binder<ToDoList> binderGroupUpdate = new Binder<>();
    Binder<ToDoListItem> binderView = new Binder<>();

    Button btnSend = new Button("Send");

    Long itemIdForEdition=0L;
    Long toDoListIdForEdition=0L;
    Long loggedInSystemUserId;
    Long toDoListFreshId;

    public static TextField txtName = new TextField("Name","Enter your to do group name");
    public static TextField txtCreatedAt = new TextField("Date");

    public static TextField txtNameItem = new TextField("Name", "Enter content's name");
    public static TextField txtContent = new TextField("Content", "Enter content");

    HtmlEmail email = new HtmlEmail();

    H3 componentHead = new H3("New Item");

    public MainView(ToDoListService toDoListService, ToDoListItemService toDoListItemService){
        this.toDoListService = toDoListService;
        this.toDoListItemService = toDoListItemService;


        if (VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId")==null){
            UI.getCurrent().getPage().setLocation("/login");
        }else {
            System.out.println("Logged in User ID");
            System.out.println(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
            loggedInSystemUserId=Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
        }

        Button btnNew = new Button("Add", VaadinIcon.INSERT.create());
        txtFilter.setPlaceholder("Key");
        Button btnFilter = new Button("Search", VaadinIcon.SEARCH.create());
        btnFilter.addClickListener(buttonClickEvent -> {
            refreshData(txtFilter.getValue());
        });

        Button btnItemNew = new Button("Add", VaadinIcon.INSERT.create());

        txtItemFilter.setPlaceholder("Key");
        Button btnItemFilter = new Button("Search", VaadinIcon.SEARCH.create());
        btnItemFilter.addClickListener(buttonClickEvent -> {
            refreshDataForItems(txtItemFilter.getValue().toString());
        });

        HorizontalLayout filterGroup = new HorizontalLayout();
        filterGroup.add(txtFilter,btnFilter);

        HorizontalLayout filterItemGroup = new HorizontalLayout();
        filterItemGroup.add(txtItemFilter,btnItemFilter);

        dialogGroup.setModal(true);
        dialogView.setModal(true);



        txtRecieverMail = new TextField("Email", "Enter email");


        binderGroupUpdate.bind(txtName, ToDoList::getName, ToDoList::setName);
        binderGroupUpdate.bind(txtCreatedAt, ToDoList::getDate, ToDoList::setDate);

        binderView.bind(txtNameItem, ToDoListItem::getName,ToDoListItem::setName);
        binderView.bind(txtContent, ToDoListItem::getContent,ToDoListItem::setContent);

        TextField txtToDoName = new TextField("Item Name","Enter item");

        FormLayout formLayout = new FormLayout();
        formLayout.add(txtName,txtCreatedAt);

        FormLayout formLayoutItem = new FormLayout();
        formLayoutItem.add(txtNameItem,txtContent);

        VerticalLayout verticalLayoutItem = new VerticalLayout();

        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");

        Button btnMailCancel = new Button("Cancel");
        Button btnSaveItem = new Button("Save");
        Button btnItemCancel = new Button("Cancel");

        btnCancel.addClickListener(buttonClickEvent -> {
            dialogGroup.close();
        });
        btnMailCancel.addClickListener(buttonClickEvent -> {
            dialogShareMail.close();
        });
        btnItemCancel.addClickListener(buttonClickEvent ->{
            dialogNewItem.close();
        });

        btnSave.addClickListener(buttonClickEvent -> {
            ToDoList toDoList = new ToDoList();
            try {
                binderGroupUpdate.writeBean(toDoList);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            toDoList.setId(itemIdForEdition);
            SystemUser loggedInSystemUser=new SystemUser();
            loggedInSystemUser.setId(loggedInSystemUserId);
            toDoList.setSystemUser(loggedInSystemUser);
            toDoListService.save(toDoList);
            refreshData(txtFilter.getValue().toString());
            dialogGroup.close();
        });

        btnSaveItem.addClickListener(buttonClickEvent -> {
            ToDoListItem toDoListItem = new ToDoListItem();
            try {
                binderView.writeBean(toDoListItem);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            toDoListItem.setId(toDoListIdForEdition);
            ToDoList toDoList = new ToDoList();
            toDoList.setId(toDoListFreshId);
            toDoListItem.setToDoList(toDoList);
            toDoListItemService.save(toDoListItem);
            refreshDataForItems(txtItemFilter.getValue().toString());
            dialogNewItem.close();
        });
        HorizontalLayout horizontalNewLayout=new HorizontalLayout();
        horizontalNewLayout.setSpacing(true);

        HorizontalLayout horizontalLayoutMail = new HorizontalLayout();
        horizontalLayoutMail.setSpacing(true);
        horizontalLayoutMail.add(btnMailCancel,btnSend);
        FormLayout mailLayout = new FormLayout();
        mailLayout.add(txtRecieverMail);
        horizontalNewLayout.add(btnCancel,btnSave);

        HorizontalLayout horizontalLayoutItem = new HorizontalLayout();
        horizontalLayoutItem.setSpacing(true);
        horizontalLayoutItem.add(btnSaveItem,btnItemCancel);

        HorizontalLayout horizontalLayoutSearch  = new HorizontalLayout();
        horizontalLayoutSearch.setSpacing(true);
        horizontalLayoutSearch.add(filterItemGroup);

        verticalLayoutItem.add(horizontalLayoutSearch,gridItem);
        verticalLayoutItem.setSpacing(true);

        btnNew.addClickListener(buttonClickEvent -> {
            itemIdForEdition = 0L;
            binderGroupUpdate.readBean(new ToDoList());
            dialogGroup.open();
        });
        btnItemNew.addClickListener(event -> {
            toDoListIdForEdition = 0L;
            binderView.readBean(new ToDoListItem());
            componentHead.setText("New Item");
            dialogNewItem.open();
        });
        ToDoList toDoList = new ToDoList();
        dialogGroup.add(new H3("New List"),formLayout,horizontalNewLayout);
        dialogView.add(new H3("To Do List of Items"),btnItemNew,filterItemGroup,gridItem);
        dialogView.setWidth("700px");


        dialogShareMail.add(new H3("Share Mail"),mailLayout,horizontalLayoutMail);
        dialogItemUpdate.add(new H3("Update"),formLayoutItem,horizontalLayoutItem);

        dialogNewItem.add(componentHead,formLayoutItem,horizontalLayoutItem);


        refreshData(txtFilter.getValue().toString());
        gridConfigurationForGroup();
        gridConfigurationForItems();

        Button btnLogOut = new Button("LogOut");
        btnLogOut.addClickListener(buttonClickEvent -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getPage().setLocation("/login");
        });

        add(new H2("To Do List"),btnLogOut,btnNew,filterGroup,grid);
    }

    private void configureSendButton(ToDoList toDoList){
        btnSend.addClickListener(buttonClickEvent -> {
            String string = "";
            string = txtRecieverMail.getValue();
            try {
                email.addTo(string);
                sendSimpleMail(string,toDoList);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            dialogShareMail.close();
        });
    }

    private void gridConfigurationForGroup() {
        grid.removeColumnByKey("id");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumns("name");
        grid.addColumn(e-> e != null ? e.getDate() : "" ).setHeader("Date");
        grid.addComponentColumn(item -> createActionButtons(item)).setHeader("Actions");
        grid.addComponentColumn(item -> creteButtonGridItemView(item)).setHeader("View");
        grid.addComponentColumn(item -> createButtonGridItemShare(item)).setHeader("Share");

    }

    private void gridConfigurationForItems() {
        gridItem.removeColumnByKey("id");
        gridItem.setSelectionMode(Grid.SelectionMode.NONE);
        gridItem.setColumns("name", "content");
        gridItem.addComponentColumn(item -> GroupItemsActions(gridItem, item)).setHeader("Actions");
    }

    private void refreshData(String filter){
        List<ToDoList> toDoListList = new ArrayList<>();
        toDoListList.addAll(toDoListService.getList(filter,loggedInSystemUserId));
        grid.setItems(toDoListList);
    }

    private void refreshDataForItems(String filter){
        List<ToDoListItem> toDoListItems = new ArrayList<>();
        toDoListItems.addAll(toDoListItemService.getToDoListItems(filter,toDoListFreshId));
        gridItem.setItems(toDoListItems);
    }

    private HorizontalLayout createActionButtons(ToDoList item) {
        @SuppressWarnings("unchecked")
        Button btnDelete = new Button("Delete");
        btnDelete.addClickListener(buttonClickEvent -> {
            ConfirmDialog dialog = new ConfirmDialog("Confirm delete",
                    "Are you sure you want to delete this record?", "Delete", confirmEvent -> {
                    toDoListService.delete(item);
                    refreshData(txtFilter.getValue().toString());
            },
                    "Cancel", cancelEvent -> {
            });
            dialog.setConfirmButtonTheme("error primary");
            dialog.open();
        });

        Button btnUpdate = new Button("Update");
        btnUpdate.addClickListener(buttonClickEvent -> {
            itemIdForEdition=item.getId();
            binderGroupUpdate.readBean(item);
            dialogGroup.open();
        });



        HorizontalLayout horizontalLayout=new HorizontalLayout();
        horizontalLayout.add(btnUpdate,btnDelete);

        return horizontalLayout;
    }


    private HorizontalLayout creteButtonGridItemView(ToDoList item) {

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button btnView = new Button("View");
        btnView.addClickListener(buttonClickEvent -> {
            toDoListFreshId = item.getId();
            refreshDataForItems("");
            dialogView.open();
        });

        horizontalLayout.add(btnView);

        return horizontalLayout;
    }
    private HorizontalLayout createButtonGridItemShare(ToDoList item){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button btnShare = new Button("Share");
        btnShare.addClickListener(event -> {

            toDoListFreshId = item.getId();
            configureSendButton(item);
            dialogShareMail.open();
        });

        horizontalLayout.add(btnShare);

        return horizontalLayout;

    }

    private HorizontalLayout ViewAdding (Grid<ToDoList> grid,ToDoList item){
        Button btnView = new Button("View");
        btnView.addClickListener(buttonClickEvent -> {
            System.out.println(grid.asSingleSelect().getValue().getId().toString() + " seçilen grid id");
            toDoListFreshId = Long.valueOf(grid.asSingleSelect().getValue().getId().toString());//ÖNEMLİ
            refreshDataForItems(txtItemFilter.getValue().toString());

            dialogView.open();
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(btnView);
        return horizontalLayout;
    }

    private HorizontalLayout GroupItemsActions (Grid<ToDoListItem> grid, ToDoListItem item){
        @SuppressWarnings("unchecked")
        Button btnChecked = new Button("Check");
        btnChecked.addClickListener(buttonClickEvent -> {
            toDoListItemService.delete(item);
            refreshDataForItems(txtItemFilter.getValue().toString());
        });

        Button btnUpdate1 = new Button("Update");
        btnUpdate1.addClickListener(buttonClickEvent -> {
            toDoListIdForEdition=item.getId();
            binderView.readBean(item);
            componentHead.setText("Update Item");
            dialogNewItem.open();


        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(btnChecked,btnUpdate1);
        return horizontalLayout;
    }
    public static void sendSimpleMail(String string, ToDoList toDoList) throws Exception {
        Email email = new SimpleEmail();


        System.out.println(toDoListService.getList());
        System.out.println();
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("h.fehim.arici@gmail.com","1972HaS1962KaN1990"));
        email.setDebug(false);
        email.setHostName("smtp.gmail.com");
        email.setFrom("h.fehim.arici@gmail.com");
        email.setSubject("Your To Do List");
        email.setMsg(toDoList.getName() + " " + toDoList.getDate());
        email.addTo(string);
        email.setTLS(true);
        email.send();
        System.out.println("Mail sent!");
    }



}
