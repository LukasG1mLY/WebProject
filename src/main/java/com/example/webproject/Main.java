package com.example.webproject;

import com.example.webproject.DatabaseUtils.DatabaseUtils;
import com.example.webproject.Listen.InfoBox;
import com.example.webproject.Listen.Ldap;
import com.example.webproject.Listen.Link;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@Route("")
public class Main extends Div {
    public DatabaseUtils databaseUtils;
    private final VerticalLayout layout_content;
    private final VerticalLayout layout_grid;
    private final VerticalLayout layout_dialog;
    private final Tab edit;
    private final Tab home;

    public Main() throws IOException {
        databaseUtils = new DatabaseUtils();
        layout_content = new VerticalLayout();
        layout_grid = new VerticalLayout();
        layout_dialog = new VerticalLayout();

        edit = new Tab(VaadinIcon.EDIT.create(), new Span("Bearbeiten"));
        home = new Tab(VaadinIcon.EDIT.create(), new Span("Hauptmenü"));

        Tabs tabs = new Tabs(home, edit);
        tabs.addSelectedChangeListener(e -> {
            setContent(e.getSelectedTab());
            setContent(tabs.getSelectedTab());
        });

        add(tabs, layout_content, layout_grid, layout_dialog);
    }
    private void setContent(@NotNull Tab tab) {

        layout_content.setVisible(true);
        layout_content.removeAll();

        layout_grid.removeAll();
        layout_grid.setVisible(true);

        layout_dialog.removeAll();
        layout_dialog.setVisible(true);
        layout_dialog.setSpacing(true);
        layout_dialog.setPadding(false);

        if (tab.equals(edit)) {

            List<InfoBox> InfoBox = databaseUtils.getInfo_InfoBox();

            Grid<InfoBox> InfoBox_grid = new Grid<>();
            InfoBox_grid.addColumn(com.example.webproject.Listen.InfoBox::getRolle).setHeader("Rolle");
            InfoBox_grid.addColumn(com.example.webproject.Listen.InfoBox::getInfo).setHeader("Info");
            InfoBox_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
            InfoBox_grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            InfoBox_grid.setItems(InfoBox);
            InfoBox_grid.setVisible(false);

            List<Link> Link = databaseUtils.getInfo_Link();
            Grid<Link> Link_grid = new Grid<>();
            Link_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
            Link_grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            Link_grid.addColumn(com.example.webproject.Listen.Link::getId).setHeader("ID").setFlexGrow(0).setSortable(true);
            Link_grid.addColumn(com.example.webproject.Listen.Link::getLinktext).setHeader("Linktext");
            Link_grid.addColumn(com.example.webproject.Listen.Link::getLink_grp_id).setHeader("Link GRP ID");
            Link_grid.addColumn(com.example.webproject.Listen.Link::getSort).setHeader("Sort");
            Link_grid.addColumn(com.example.webproject.Listen.Link::getDescription).setHeader("Description");
            Link_grid.addColumn(com.example.webproject.Listen.Link::getUrl_active).setHeader("Url Active");
            Link_grid.addColumn(com.example.webproject.Listen.Link::getUrl_inactive).setHeader("Url InActive");
            Link_grid.addColumn(com.example.webproject.Listen.Link::getActive).setHeader("Active");
            Link_grid.addColumn(com.example.webproject.Listen.Link::getAuth_level).setHeader("Auth Level");
            Link_grid.addColumn(com.example.webproject.Listen.Link::getNewtab).setHeader("New Tab");
            Link_grid.setItems(Link);
            Link_grid.setVisible(false);

            List<Ldap> Ldap = databaseUtils.getInfo_Ldap();
            Grid<Ldap> Ldap_grid = new Grid<>();
            Ldap_grid.addColumn(com.example.webproject.Listen.Ldap::getId).setHeader("ID").setFlexGrow(0);
            Ldap_grid.addColumn(com.example.webproject.Listen.Ldap::getContent).setHeader("Content").setAutoWidth(true);
            Ldap_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
            Ldap_grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            Ldap_grid.setItems(Ldap);
            Ldap_grid.setVisible(false);

            MenuBar menuBar = new MenuBar();
            menuBar.setVisible(true);

            MenuItem Infobox_item = menuBar.addItem("Infotext");
            MenuItem Ldap_item = menuBar.addItem("Ldap");
            MenuItem Link_item = menuBar.addItem("Link");

            menuBar.addThemeVariants(MenuBarVariant.LUMO_LARGE);

            Infobox_item.addClickListener(e -> {
                Button b1 = new Button("Abbrechen");
                b1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                b1.getStyle().set("margin-left", "auto");
                Button b2 = new Button("Speichern");
                b2.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                b2.getStyle().set("margin-right", "auto");
                TextField tf1 = new TextField();
                tf1.setLabel("Staff");
                tf1.setValue(databaseUtils.getInfoStaff());
                tf1.setWidthFull();
                TextField tf2 = new TextField();
                tf2.setLabel("Studenten");
                tf2.setValue(databaseUtils.getInfoStudent());
                tf2.setWidthFull();
                Dialog dialog = new Dialog();
                dialog.open();
                dialog.setWidth(40, Unit.PERCENTAGE);
                dialog.setHeaderTitle("Bearbeiten");
                dialog.add(tf1, tf2);
                dialog.getFooter().add(b2);
                dialog.getFooter().add(b1);

                b1.addClickListener(Click -> {
                    dialog.close();
                    Notification.show("Vorgang Abgebrochen").addThemeVariants(NotificationVariant.LUMO_ERROR);
                });
                b2.addClickListener(Click -> {
                    databaseUtils.editInfoStaff(tf1.getValue());
                    databaseUtils.editInfoStudent(tf2.getValue());
                    dialog.close();
                    Notification.show("Vorgang Abgeschlossen Bitte Aktualisieren sie die Seite").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                });
            });
            Ldap_item.addClickListener(e -> {

                Dialog d1 = new Dialog();

                Dialog d2 = new Dialog();

                Dialog d3 = new Dialog();
                d3.setWidth(60, Unit.PERCENTAGE);

                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");
                info.getStyle().set("color", "red");

                H2 H2 = new H2("Verzeichnis-Liste: Link");
                H2.getStyle().set("margin", "0 auto 0 0");

                H2 H3 = new H2("");
                H2.getStyle().set("margin", "0 auto 0 0");

                Button saveButton = new Button("Speichern");
                saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

                Button cancelButton = new Button("Nein, Abbrechen");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

                Button createButton = new Button(VaadinIcon.PLUS.create());
                createButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);

                Button closeButton = new Button(VaadinIcon.CLOSE.create());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                editButton.setEnabled(false);

                Button deleteButton = new Button(VaadinIcon.TRASH.create());
                deleteButton.setEnabled(false);
                deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                deleteButton.getStyle().set("margin", "0 auto 0 0 0 0");

                Button approveButton = new Button("Löschen");
                approveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

                Button button_ADD = new Button("Speichern");
                button_ADD.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

                Button button_ADD_1 = new Button("Abbrechen");
                button_ADD_1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

                TextField tf1 = new TextField("Content");

                HorizontalLayout heading = new HorizontalLayout(H2, closeButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                HorizontalLayout tools = new HorizontalLayout(H3, createButton, editButton, deleteButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                d1.open();
                d1.setCloseOnOutsideClick(false);
                d1.setWidthFull();

                Ldap_grid.setVisible(d1.isOpened());
                Ldap_grid.addSelectionListener(selection -> {
                    int size = selection.getAllSelectedItems().size();
                    boolean isSingleSelection = size == 1;
                    editButton.setEnabled(isSingleSelection);
                    deleteButton.setEnabled(size != 0);
                });
                createButton.addClickListener(Click -> {
                    d3.open();
                    d3.add(tf1);
                    d3.getFooter().add(button_ADD, button_ADD_1);
                });
                saveButton.addClickListener(Click -> {
                });
                cancelButton.addClickListener(Click -> {

                });
                closeButton.addClickListener(Click -> d1.close());
                deleteButton.addClickListener(Click -> {
                    d2.open();
                    d2.setHeaderTitle("Sind sie sicher, dass sie dieses Verzeichnis Unwiderruflich Löschen Wollen ?");
                    d2.getFooter().add(saveButton, cancelButton);

                });
                editButton.addClickListener(Click -> {
                });
                Ldap_grid.addItemClickListener(Click -> {

                    deleteButton.addClickListener(click -> {
                        d2.open();
                        d2.setHeaderTitle("Verzeichnis " + Click.getItem().getId() + " Löschen");
                        d2.add(info);
                        d2.getFooter().add(approveButton, cancelButton);
                    });
                    approveButton.addClickListener(click -> {
                        databaseUtils.deleteInfoLDAP(Integer.parseInt(Click.getItem().getId()));
                        UI.getCurrent().getPage().reload();
                    });
                });
                button_ADD.addClickListener(Click -> {
                    if (tf1.isEmpty()) {
                        tf1.setValue("N/A");
                    }
                    databaseUtils.addNewIdAndName_Ldap(tf1.getValue());
                    UI.getCurrent().getPage().reload();

                });

                d1.add(heading, tools, Ldap_grid);
            });
            Link_item.addClickListener(e -> {
                Dialog d1 = new Dialog();

                Dialog d2 = new Dialog();

                Dialog d3 = new Dialog();
                d3.setWidth(60, Unit.PERCENTAGE);

                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");
                info.getStyle().set("color", "red");

                H2 H2 = new H2("Verzeichnis-Liste: Link");
                H2.getStyle().set("margin", "0 auto 0 0");

                H2 H3 = new H2("");
                H2.getStyle().set("margin", "0 auto 0 0");

                Button saveButton = new Button("Speichern");
                saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

                Button cancelButton = new Button("Nein, Abbrechen");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

                Button createButton = new Button(VaadinIcon.PLUS.create());
                createButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);

                Button closeButton = new Button(VaadinIcon.CLOSE.create());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

                Button editButton = new Button(VaadinIcon.EDIT.create());
                editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                editButton.setEnabled(false);

                Button deleteButton = new Button(VaadinIcon.TRASH.create());
                deleteButton.setEnabled(false);
                deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                deleteButton.getStyle().set("margin", "0 auto 0 0 0 0");

                Button approveButton = new Button("Löschen");
                approveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

                TextField Linktext = new TextField("Linktext");
                Linktext.setWidthFull();
                TextField Link_Group_ID = new TextField("Link Group ID");
                Link_Group_ID.setWidthFull();
                TextField Sort = new TextField("Sort");
                Sort.setWidthFull();
                TextField Description = new TextField("Description");
                Description.setWidthFull();
                TextField URL_ACTIVE = new TextField("URL Active");
                URL_ACTIVE.setWidthFull();
                TextField URL_INACTIVE = new TextField("URL InActive");
                URL_INACTIVE.setWidthFull();
                TextField Active = new TextField("Active");
                Active.setWidthFull();
                TextField Auth_Level = new TextField("Auth Level");
                Auth_Level.setWidthFull();
                TextField NewTab = new TextField("NewTab");
                NewTab.setWidthFull();

                VerticalLayout dialogLayout = new VerticalLayout(Linktext, Link_Group_ID, Sort, Description, URL_ACTIVE, URL_INACTIVE, Active, Auth_Level, NewTab);
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);
                dialogLayout.setSizeFull();

                HorizontalLayout heading = new HorizontalLayout(H2, closeButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                HorizontalLayout tools = new HorizontalLayout(H3, createButton, editButton, deleteButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);
                d1.open();
                d1.setCloseOnOutsideClick(false);
                d1.setWidthFull();

                Link_grid.setVisible(d1.isOpened());
                Link_grid.addSelectionListener(selection -> {
                    int size = selection.getAllSelectedItems().size();
                    boolean isSingleSelection = size == 1;
                    editButton.setEnabled(isSingleSelection);
                    deleteButton.setEnabled(size != 0);
                });
                saveButton.addClickListener(Click -> {
                    if (Linktext.isEmpty()) {
                        Linktext.setValue("N/A");
                    }
                    if (Link_Group_ID.isEmpty()) {
                        Link_Group_ID.setValue("1");
                    }
                    if (Sort.isEmpty()) {
                        Sort.setValue("1");
                    }
                    if (Description.isEmpty()) {
                        Description.setValue("N/A");
                    }
                    if (URL_ACTIVE.isEmpty()) {
                        URL_ACTIVE.setValue("N/A");
                    }
                    if (URL_INACTIVE.isEmpty()) {
                        URL_INACTIVE.setValue("1");
                    }
                    if (Active.isEmpty()) {
                        Active.setValue("1");
                    }
                    if (Auth_Level.isEmpty()) {
                        Auth_Level.setValue("1");
                    }
                    if (NewTab.isEmpty()) {
                        NewTab.setValue("1");
                    }

                    databaseUtils.addNewIdAndName_Link(Linktext.getValue(), Link_Group_ID.getValue(), Sort.getValue(), Description.getValue(), URL_ACTIVE.getValue(), URL_INACTIVE.getValue(), Active.getValue(), Auth_Level.getValue(), NewTab.getValue());
                    UI.getCurrent().getPage().reload();
                });

                cancelButton.addClickListener(Click -> d3.close());
                closeButton.addClickListener(Click -> d1.close());
                editButton.addClickListener(Click -> d2.open());
                deleteButton.addClickListener(Click -> {
                    d2.open();
                    d2.add(info);
                    d2.getFooter().add(approveButton, cancelButton);
                    Link_grid.addItemClickListener(click -> {
                        d2.setHeaderTitle("Verzeichnis " + click.getItem().getId());
                    });
                });
                createButton.addClickListener(Click -> {
                    d3.open();
                    d3.add(dialogLayout);
                    d3.getFooter().add(saveButton, cancelButton);
                });
                Link_grid.addItemClickListener(Click -> {
                    Link_grid.setRowsDraggable(true);
                    final int l = Click.getItem().getId();
                    approveButton.addClickListener(click -> {
                        databaseUtils.deleteInfoLink(l);
                        UI.getCurrent().getPage().reload();

                    });
                });
                d1.add(heading, tools, Link_grid);
            });
            Link_grid.addItemClickListener(Click -> {
                int i = Integer.parseInt(String.valueOf(Click.getItem().getId()));
                System.out.println(i);
            });
                layout_grid.setMaxWidth(100, Unit.PERCENTAGE);
                layout_content.setAlignItems(FlexComponent.Alignment.CENTER);
                layout_content.add(menuBar);
                layout_grid.add(InfoBox_grid, Link_grid, Ldap_grid);
        }
        if (tab.equals(home)) {
                System.out.println("Home wurde Gedrückt");
            }
    }
}