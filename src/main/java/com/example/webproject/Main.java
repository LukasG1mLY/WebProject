package com.example.webproject;

import com.example.webproject.DatabaseUtils.DatabaseUtils;
import com.example.webproject.Listen.*;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Route("")
public class Main extends Div {

    public DatabaseUtils databaseUtils;
    private final VerticalLayout layout_content;
    private final VerticalLayout layout_dialog;
    private final Tab edit;
    private final Tab home;

    public Main() throws IOException, SQLException {
        databaseUtils = new DatabaseUtils();
        layout_content = new VerticalLayout();
        layout_dialog = new VerticalLayout();

        edit = new Tab(VaadinIcon.EDIT.create(), new Span("Bearbeiten"));
        home = new Tab(VaadinIcon.EDIT.create(), new Span("Hauptmenü"));

        Tabs tabs = new Tabs(home, edit);
        tabs.addSelectedChangeListener(e -> {
            setContent(e.getSelectedTab());
            setContent(tabs.getSelectedTab());
        });
        add(tabs);
    }

    private void setContent(@NotNull Tab tab) {
        layout_content.setVisible(true);
        layout_content.removeAll();
        layout_content.setAlignItems(FlexComponent.Alignment.CENTER);

        layout_dialog.removeAll();
        layout_dialog.setVisible(true);
        layout_dialog.setSpacing(true);
        layout_dialog.setPadding(false);

        if (tab.equals(edit)) {

            Notification notification = new Notification();
            notification.setDuration(1000);
            notification.setPosition(Notification.Position.TOP_CENTER);

            MenuBar menuBar = new MenuBar();
            menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON, MenuBarVariant.MATERIAL_OUTLINED);

            MenuItem Infobox_item = menuBar.addItem("Infotext");
            MenuItem Ldap_item = menuBar.addItem("Ldap");
            MenuItem Link_item = menuBar.addItem("Link");
            MenuItem LdapRole_item = menuBar.addItem("Ldap Role");
            MenuItem Link_Grp_item = menuBar.addItem("Link Group");
            MenuItem Link_Tile_item = menuBar.addItem("Link Tile");

            Infobox_item.addThemeNames("InfoBox für Studenten und Mitarbeitende");
            Ldap_item.addThemeNames("Lightweight Directory Access Protocol");
            Link_item.addThemeNames("Links");
            LdapRole_item.addThemeNames("Lightweight Directory Access Protocol Rollen");
            Link_Grp_item.addThemeNames("Link Gruppen");
            Link_Tile_item.addThemeNames("Link Kacheln");

            Infobox_item.addClickListener(e -> {
                List<InfoBox> InfoBox = databaseUtils.getInfo_InfoBox();
                Grid<InfoBox> InfoBox_grid = new Grid<>();
                InfoBox_grid.addColumn(com.example.webproject.Listen.InfoBox::getRolle).setHeader("Rolle");
                InfoBox_grid.addColumn(com.example.webproject.Listen.InfoBox::getInfo).setHeader("Info");
                InfoBox_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                InfoBox_grid.setSelectionMode(Grid.SelectionMode.NONE);
                InfoBox_grid.setItems(InfoBox);

                Dialog d1 = new Dialog();
                Dialog d2 = new Dialog();
                while (d1.isOpened()) {
                    InfoBox_grid.getDataProvider().refreshItem(databaseUtils.getInfo_InfoBox().get(1));
                    InfoBox_grid.getDataProvider().refreshItem(databaseUtils.getInfo_InfoBox().get(2));
                }
                H2 H2 = new H2("Verzeichnis-Liste: Infobox");
                H2.getStyle().set("margin", "0 auto 0 0");

                Button cancelButton = new Button("Nein, Abbrechen");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button editButton = new Button("Bearbeiten");
                editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button saveButton = new Button("Speichern");
                saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

                TextField tf1 = new TextField("Mitabeiter");
                tf1.setWidthFull();
                TextField tf2 = new TextField("Studenten");
                tf2.setWidthFull();

                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2);
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);

                HorizontalLayout heading = new HorizontalLayout(H2, closeButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                d1.open();
                d1.setCloseOnOutsideClick(false);
                d1.setWidthFull();
                d1.getFooter().add();

                d2.setHeaderTitle("Bearbeiten");
                d2.getFooter().add(saveButton);
                d2.setModal(true);

                editButton.addClickListener(Click -> {
                    d2.open();
                    tf1.setValue(databaseUtils.getInfoStaff());
                    tf2.setValue(databaseUtils.getInfoStudent());
                });
                saveButton.addClickListener(Click -> {
                    if (tf1.isEmpty()) {
                        tf1.setValue(databaseUtils.getInfoStaff());
                    }
                    if (tf2.isEmpty()) {
                        tf2.setValue(databaseUtils.getInfoStudent());
                    }
                    databaseUtils.editInfoStaff(tf1.getValue());
                    databaseUtils.editInfoStudent(tf2.getValue());
                    d2.close();
                    d1.close();
                    Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                });
                closeButton.addClickListener(Click -> d1.close());

                d1.add(heading, InfoBox_grid);
                d1.getFooter().add(editButton);
                d2.add(tf1, tf2);
            });
            Ldap_item.addClickListener(e -> {
                List<Ldap> Ldap = databaseUtils.getInfo_Ldap();
                Grid<Ldap> Ldap_grid = new Grid<>();
                Ldap_grid.addColumn(com.example.webproject.Listen.Ldap::getId).setHeader("ID").setFlexGrow(0);
                Ldap_grid.addColumn(com.example.webproject.Listen.Ldap::getContent).setHeader("Content").setAutoWidth(true);
                Ldap_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                Ldap_grid.setSelectionMode(Grid.SelectionMode.SINGLE);
                Ldap_grid.setItems(Ldap);

                Dialog d1 = new Dialog();
                Dialog d2 = new Dialog();
                Dialog d3 = new Dialog();
                d3.setWidth(60, Unit.PERCENTAGE);

                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");
                info.getStyle().set("color", "red");

                H2 H2 = new H2("Verzeichnis-Liste: Ldap");
                H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");
                H2.getStyle().set("margin", "0 auto 0 0");

                Button sb1 = new Button("Speichern");
                Button db1 = new Button("Löschen");
                db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button cancelButton = new Button("Nein, Abbrechen");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");
                createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());
                maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());
                minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                TextField tf1 = new TextField("Linktext");
                tf1.setWidthFull();

                VerticalLayout dialogLayout = new VerticalLayout(tf1);
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);
                dialogLayout.setSizeFull();

                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                d1.open();
                d1.setCloseOnOutsideClick(false);
                d1.setWidthFull();
                d1.getFooter().add();

                Ldap_grid.setVisible(d1.isOpened());
                Ldap_grid.addComponentColumn(Tools -> {
                    Button deleteButton = new Button(VaadinIcon.TRASH.create());
                    Button editButton = new Button(VaadinIcon.EDIT.create());
                    Button sb2 = new Button("Speichern");

                    editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    editButton.addClickListener(Click -> {
                        tf1.setValue(Tools.getContent());

                        sb2.addClickListener(click -> {
                            final int i;
                            i = Integer.parseInt(Tools.getId());
                            if (tf1.isEmpty()) {
                                tf1.setValue("N/A");
                            }
                            databaseUtils.editInfoLDAP(i, tf1.getValue());
                            Ldap_grid.getDataProvider().refreshAll();
                            d1.close();
                            d2.close();
                            d3.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                        d3.open();
                        d3.add(dialogLayout);
                        d3.getFooter().add(sb2);
                    });

                    deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    deleteButton.addClickListener(Click -> {
                        final int i;
                        i = Integer.parseInt(Tools.getId());
                        d2.open();
                        d2.setHeaderTitle("Verzeichnis " + i);
                        d2.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");
                        d2.setCloseOnOutsideClick(false);
                        d2.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            databaseUtils.deleteInfoLDAP(Integer.parseInt(Tools.getId()));
                            Ldap_grid.getDataProvider().refreshAll();
                            d1.close();
                            d2.close();
                            d3.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER);

                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                });
                sb1.addClickListener(Click -> {
                    if (tf1.isEmpty()) {
                        tf1.setValue("N/A");
                    }
                    databaseUtils.addNewIdAndName_Ldap(tf1.getValue());
                    Ldap_grid.getDataProvider().refreshAll();
                    d1.close();
                    d2.close();
                    d3.close();
                    Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                });
                cancelButton.addClickListener(Click -> {
                    d3.close();
                    d2.close();
                });
                createButton.addClickListener(Click -> {
                    d3.open();
                    d3.add(dialogLayout);
                    d3.setCloseOnOutsideClick(false);
                    d3.getFooter().add(sb1, cancelButton);
                });
                closeButton.addClickListener(Click -> {
                    d1.close();
                    Ldap_grid.removeAllColumns();
                });
                maximizeButton.addClickListener(Click -> Ldap_grid.setAllRowsVisible(true));
                minimizeButton.addClickListener(Click -> Ldap_grid.setAllRowsVisible(false));
                d1.add(heading, tools, Ldap_grid);
            });
            Link_item.addClickListener(e -> {
                List<Link> Link = databaseUtils.getInfo_Link();
                Grid<Link> Link_grid = new Grid<>();
                Link_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                Link_grid.setAllRowsVisible(false);
                Link_grid.addColumn(com.example.webproject.Listen.Link::getId).setHeader("ID").setHeader("ID").setWidth("75px").setFlexGrow(0);
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

                Button sb1 = new Button("Speichern");
                Button db1 = new Button("Löschen");
                db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button cancelButton = new Button("Nein, Abbrechen");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");
                createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());
                maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());
                minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                TextField tf1 = new TextField("Linktext");
                tf1.setWidthFull();
                NumberField tf2 = new NumberField("Link Group ID");
                tf2.setWidthFull();
                NumberField tf3 = new NumberField("Sort");
                tf3.setWidthFull();
                TextField tf4 = new TextField("Description");
                tf4.setWidthFull();
                TextField tf5 = new TextField("URL Active");
                tf5.setWidthFull();
                NumberField tf6 = new NumberField("URL InActive");
                tf6.setWidthFull();
                NumberField tf7 = new NumberField("Active");
                tf7.setWidthFull();
                NumberField tf8 = new NumberField("Auth Level");
                tf8.setWidthFull();
                NumberField tf9 = new NumberField("NewTab");
                tf9.setWidthFull();

                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9);
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);
                dialogLayout.setSizeFull();

                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                d1.open();
                d1.setCloseOnOutsideClick(false);
                d1.setWidthFull();
                d1.getFooter().add();

                Link_grid.setVisible(d1.isOpened());
                Link_grid.addComponentColumn(Tools -> {
                    Button deleteButton = new Button(VaadinIcon.TRASH.create());
                    Button editButton = new Button(VaadinIcon.EDIT.create());
                    Button sb2 = new Button("Speichern");

                    editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    editButton.addClickListener(Click -> {
                        tf1.setValue(Tools.getLinktext());
                        tf2.setValue(Double.valueOf(Tools.getLink_grp_id()));
                        tf3.setValue(Double.valueOf(Tools.getSort()));
                        tf4.setValue(Tools.getDescription());
                        tf5.setValue(Tools.getUrl_active());
                        tf6.setValue(Double.valueOf(Tools.getUrl_inactive()));
                        tf7.setValue(Double.valueOf(Tools.getActive()));
                        tf8.setValue(Double.valueOf(Tools.getAuth_level()));
                        tf9.setValue(Double.valueOf(Tools.getNewtab()));

                        sb2.addClickListener(click -> {
                            final int i;
                            i = Integer.parseInt(Tools.getId());
                            if (tf1.isEmpty()) {
                                tf1.setValue("N/A");
                            }
                            if (tf2.isEmpty()) {
                                tf2.setValue(Double.valueOf(Tools.getLink_grp_id()));
                            }
                            if (tf3.isEmpty()) {
                                tf3.setValue(Double.valueOf(Tools.getSort()));
                            }
                            if (tf4.isEmpty()) {
                                tf4.setValue("N/A");
                            }
                            if (tf5.isEmpty()) {
                                tf5.setValue("N/A");
                            }
                            if (tf6.isEmpty()) {
                                tf6.setValue(Double.valueOf(Tools.getUrl_inactive()));
                            }
                            if (tf7.isEmpty()) {
                                tf7.setValue(Double.valueOf(Tools.getActive()));
                            }
                            if (tf8.isEmpty()) {
                                tf8.setValue(Double.valueOf(Tools.getAuth_level()));
                            }
                            if (tf9.isEmpty()) {
                                tf9.setValue(Double.valueOf(Tools.getNewtab()));
                            }

                            databaseUtils.editInfoLink(i, tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue(), tf5.getValue(), tf6.getValue(), tf7.getValue(), tf8.getValue(), tf9.getValue());
                            d1.close();
                            d2.close();
                            d3.close();
                            Link_grid.getDataProvider().refreshAll();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).add(new Button("Dialog Öffnen", klick -> d1.open()));
                        });

                        d3.open();
                        d3.add(dialogLayout);
                        d3.getFooter().add(sb2);
                    });

                    deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    deleteButton.addClickListener(Click -> {
                        final int i;
                        i = Integer.parseInt(Tools.getId());
                        d2.open();
                        d2.setHeaderTitle("Verzeichnis " + i);
                        d2.setCloseOnOutsideClick(false);
                        d2.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");
                        d2.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            databaseUtils.deleteInfoLink(Integer.parseInt(Tools.getId()));
                            Link_grid.getDataProvider().refreshAll();
                            d1.close();
                            d2.close();
                            d3.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                });
                sb1.addClickListener(Click -> {
                    if (tf1.isEmpty()) {
                        tf1.setValue("N/A");
                    }
                    if (tf2.isEmpty()) {
                        tf2.setValue(Double.valueOf("1"));
                    }
                    if (tf3.isEmpty()) {
                        tf3.setValue(Double.valueOf("1"));
                    }
                    if (tf4.isEmpty()) {
                        tf4.setValue("N/A");
                    }
                    if (tf5.isEmpty()) {
                        tf5.setValue("N/A");
                    }
                    if (tf6.isEmpty()) {
                        tf6.setValue(Double.valueOf("1"));
                    }
                    if (tf7.isEmpty()) {
                        tf7.setValue(Double.valueOf("1"));
                    }
                    if (tf8.isEmpty()) {
                        tf8.setValue(Double.valueOf("1"));
                    }
                    if (tf9.isEmpty()) {
                        tf9.setValue(Double.valueOf("1"));
                    }

                    databaseUtils.addNewIdAndName_Link(tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue(), tf5.getValue(), tf6.getValue(), tf7.getValue(), tf8.getValue(), tf9.getValue());
                    Link_grid.getDataProvider().refreshAll();
                    d1.close();
                    d2.close();
                    d3.close();
                    Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                });
                cancelButton.addClickListener(Click -> {
                    d3.close();
                    d2.close();
                });
                createButton.addClickListener(Click -> {
                    d3.open();
                    d3.add(dialogLayout);
                    d3.setCloseOnOutsideClick(false);
                    d3.getFooter().add(sb1, cancelButton);
                });
                closeButton.addClickListener(Click -> {
                    d1.close();
                    Link_grid.removeAllColumns();
                });
                maximizeButton.addClickListener(Click -> Link_grid.setAllRowsVisible(true));
                minimizeButton.addClickListener(Click -> Link_grid.setAllRowsVisible(false));
                d1.add(heading, tools, Link_grid);
            });
            LdapRole_item.addClickListener(e -> {
                List<LDAP_ROLE> LdapRole = databaseUtils.getAllInfos_LDAP_ROLE();
                Grid<LDAP_ROLE> LdapRole_grid = new Grid<>();
                LdapRole_grid.addColumn(LDAP_ROLE::getId).setHeader("ID").setWidth("75px").setFlexGrow(0);
                LdapRole_grid.addColumn(LDAP_ROLE::getContent).setHeader("Role Name");
                LdapRole_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                LdapRole_grid.setItems(LdapRole);
                LdapRole_grid.setVisible(false);
                LdapRole_grid.getId();

                Dialog d1 = new Dialog();
                Dialog d2 = new Dialog();
                d2.setWidth(60, Unit.PERCENTAGE);
                Dialog d3 = new Dialog();
                d3.setWidth(60, Unit.PERCENTAGE);

                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");
                info.getStyle().set("color", "red");

                H2 H2 = new H2("Verzeichnis-Liste: Ldap Role");
                H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");
                H2.getStyle().set("margin", "0 auto 0 0");

                Button sb1 = new Button("Speichern");
                Button db1 = new Button("Ja, Löschen");
                db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button cancelButton = new Button("Nein, Abbrechen");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");
                createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());
                maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());
                minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                TextField tf1 = new TextField("Role Name");
                tf1.setWidthFull();

                VerticalLayout dialogLayout = new VerticalLayout(tf1);
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);
                dialogLayout.setSizeFull();

                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                d1.open();
                d1.setCloseOnOutsideClick(false);
                d1.setWidthFull();
                d1.getFooter().add();

                LdapRole_grid.setVisible(d1.isOpened());
                LdapRole_grid.addComponentColumn(Tools -> {
                    Button deleteButton = new Button(VaadinIcon.TRASH.create());
                    Button editButton = new Button(VaadinIcon.EDIT.create());
                    Button sb2 = new Button("Speichern");

                    editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    editButton.addClickListener(Click -> {
                        tf1.setValue(Tools.getContent());
                        sb2.addClickListener(click -> {
                            final int i;
                            i = Integer.parseInt(Tools.getId());
                            if (tf1.isEmpty()) {
                                tf1.setValue("N/A");
                            }
                            databaseUtils.editInfoLDAP_ROLE(i, tf1.getValue());
                            LdapRole_grid.getDataProvider().refreshAll();
                            d1.close();
                            d2.close();
                            d3.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });

                        d3.open();
                        d3.add(dialogLayout);
                        d3.getFooter().add(sb2);
                    });

                    deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    deleteButton.addClickListener(Click -> {
                        final int i;
                        i = Integer.parseInt(Tools.getId());
                        d2.open();
                        d2.setHeaderTitle("Verzeichnis " + i);
                        d2.setCloseOnOutsideClick(false);
                        d2.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");
                        d2.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            databaseUtils.deleteInfoLDAP_ROLE(Integer.parseInt(Tools.getId()));
                            LdapRole_grid.getDataProvider().refreshAll();
                            d1.close();
                            d2.close();
                            d3.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                }).setWidth("125px").setFlexGrow(0);
                sb1.addClickListener(Click -> {
                    if (tf1.isEmpty()) {
                        tf1.setValue("N/A");
                    }
                    databaseUtils.addNewIdAndName_ROLE(tf1.getValue());
                    LdapRole_grid.getDataProvider().refreshAll();
                    d1.close();
                    d2.close();
                    d3.close();
                    Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                });
                cancelButton.addClickListener(Click -> {
                    d3.close();
                    d2.close();
                });
                createButton.addClickListener(Click -> {
                    d3.open();
                    d3.add(dialogLayout);
                    d3.setCloseOnOutsideClick(false);
                    d3.getFooter().add(sb1, cancelButton);
                });
                closeButton.addClickListener(Click -> {
                    d1.close();
                    LdapRole_grid.removeAllColumns();
                });
                maximizeButton.addClickListener(Click -> LdapRole_grid.setAllRowsVisible(true));
                minimizeButton.addClickListener(Click -> LdapRole_grid.setAllRowsVisible(false));
                d1.add(heading, tools, LdapRole_grid);
            });
            Link_Grp_item.addClickListener(e -> {
                List<Link_grp> LinkGrp = databaseUtils.getInfo_Link_Grp();
                Grid<Link_grp> LinkGrp_grid = new Grid<>();
                LinkGrp_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                LinkGrp_grid.addColumn(Link_grp::getId).setHeader("ID").setWidth("75px").setFlexGrow(0);
                LinkGrp_grid.addColumn(Link_grp::getGrp_Linktext).setHeader("Linktext_Grp");
                LinkGrp_grid.addColumn(Link_grp::getIcon_Id).setHeader("Icon_Id");
                LinkGrp_grid.addColumn(Link_grp::getTile_Id).setHeader("Tile_Id");
                LinkGrp_grid.addColumn(Link_grp::getSort).setHeader("Sort");
                LinkGrp_grid.addColumn(Link_grp::getDescription).setHeader("Description");
                LinkGrp_grid.setItems(LinkGrp);

                Dialog d1 = new Dialog();
                Dialog d2 = new Dialog();
                d2.setWidth(60, Unit.PERCENTAGE);
                Dialog d3 = new Dialog();
                d3.setWidth(60, Unit.PERCENTAGE);

                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");
                info.getStyle().set("color", "red");

                H2 H2 = new H2("Verzeichnis-Liste: Link Group");
                H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");
                H2.getStyle().set("margin", "0 auto 0 0");

                Button sb1 = new Button("Speichern");
                Button db1 = new Button("Ja, Löschen");
                db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button cancelButton = new Button("Nein, Abbrechen");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");
                createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());
                maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());
                minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                TextField tf1 = new TextField("Linktext Gruppe");
                tf1.setWidthFull();
                NumberField tf2 = new NumberField("Symbol Id");
                tf2.setWidthFull();
                NumberField tf3 = new NumberField("Kachel Id");
                tf3.setWidthFull();
                NumberField tf4 = new NumberField("Sortieren");
                tf4.setWidthFull();
                TextField tf5 = new TextField("Beschreibung");
                tf5.setWidthFull();

                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2, tf3, tf4, tf5);
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);
                dialogLayout.setSizeFull();

                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                d1.open();
                d1.setCloseOnOutsideClick(false);
                d1.setWidthFull();
                d1.getFooter().add();

                LinkGrp_grid.setVisible(d1.isOpened());
                LinkGrp_grid.addComponentColumn(Tools -> {
                    Button deleteButton = new Button(VaadinIcon.TRASH.create());
                    Button editButton = new Button(VaadinIcon.EDIT.create());
                    Button sb2 = new Button("Speichern");

                    editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    editButton.addClickListener(Click -> {
                        d3.open();
                        VerticalLayout vl = new VerticalLayout(tf1, tf2, tf3, tf4, tf5);
                        tf1.setValue(Tools.getGrp_Linktext());
                        tf2.setValue(Double.valueOf(Tools.getIcon_Id()));
                        tf3.setValue(Double.valueOf(Tools.getTile_Id()));
                        tf4.setValue(Double.valueOf(Tools.getSort()));
                        try {
                            tf5.setValue(Tools.getDescription());
                        } catch (NullPointerException npe) {
                            tf5.setPlaceholder("Momentan keine Beschreibung Vorhanden");
                        }
                        sb2.addClickListener(click -> {
                            final int i;
                            i = Integer.parseInt(Tools.getId());
                            if (tf1.isEmpty()) {
                                tf1.setValue("N/A");
                            }
                            if (tf2.isEmpty()) {
                                tf2.setValue(Double.valueOf("0"));
                            }
                            if (tf3.isEmpty()) {
                                tf3.setValue(Double.valueOf("0"));
                            }
                            if (tf4.isEmpty()) {
                                tf4.setValue(Double.valueOf("0"));
                            }
                            if (tf5.isEmpty()) {
                                tf5.setValue("N/A");
                            }
                            databaseUtils.editInfoLink_Grp(i, tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue(), tf5.getValue());
                            LinkGrp_grid.getDataProvider().refreshAll();
                            d1.close();
                            d2.close();
                            d3.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });

                        d3.add(vl);
                        d3.getFooter().add(sb2);
                    });

                    deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    deleteButton.addClickListener(Click -> {
                        final int i;
                        i = Integer.parseInt(Tools.getId());
                        d2.open();
                        d2.setHeaderTitle("Verzeichnis " + i);
                        d2.setCloseOnOutsideClick(false);
                        d2.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");
                        d2.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            databaseUtils.deleteInfoLink_Grp(Integer.parseInt(Tools.getId()));
                            LinkGrp_grid.getDataProvider().refreshAll();
                            d1.close();
                            d2.close();
                            d3.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).add(new Button("Dialog Öffnen", klick -> d1.open()));
                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                }).setWidth("125px").setFlexGrow(0);

                sb1.addClickListener(Click -> {
                    if (tf1.isEmpty()) {
                        tf1.setValue("N/A");
                    }
                    if (tf2.isEmpty()) {
                        tf2.setValue(Double.valueOf("1"));
                    }
                    if (tf3.isEmpty()) {
                        tf3.setValue(Double.valueOf("1"));
                    }
                    if (tf4.isEmpty()) {
                        tf4.setValue(Double.valueOf("1"));
                    }
                    if (tf5.isEmpty()) {
                        tf5.setValue("N/A");
                    }
                    databaseUtils.addNewIdAndName_Link_Grp(tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue(), tf5.getValue());
                    LinkGrp_grid.getDataProvider().refreshAll();
                    d1.close();
                    d2.close();
                    d3.close();
                    Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                });
                cancelButton.addClickListener(Click -> {
                    d3.close();
                    d2.close();
                });
                createButton.addClickListener(Click -> {
                    d3.open();
                    d3.add(dialogLayout);
                    d3.setCloseOnOutsideClick(false);
                    d3.getFooter().add(sb1, cancelButton);
                });
                closeButton.addClickListener(Click -> {
                    d1.close();
                    LinkGrp_grid.removeAllColumns();
                });
                maximizeButton.addClickListener(Click -> LinkGrp_grid.setAllRowsVisible(true));
                minimizeButton.addClickListener(Click -> LinkGrp_grid.setAllRowsVisible(false));
                d1.add(heading, tools, LinkGrp_grid);
            });
            Link_Tile_item.addClickListener(e -> {
                List<Link_Tile> LinkTile = databaseUtils.getInfo_Link_Tile();
                Grid<Link_Tile> LinkTile_grid = new Grid<>();
                LinkTile_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                LinkTile_grid.addColumn(Link_Tile::getId).setHeader("ID").setWidth("75px").setFlexGrow(0);
                LinkTile_grid.addColumn(Link_Tile::getName).setHeader("Name");
                LinkTile_grid.addColumn(Link_Tile::getDescription).setHeader("Description");
                LinkTile_grid.addColumn(Link_Tile::getSort).setHeader("Sort");
                LinkTile_grid.addColumn(Link_Tile::getTile_Column_Id).setHeader("Tile Column Id");
                LinkTile_grid.setItems(LinkTile);

                Dialog d1 = new Dialog();
                Dialog d2 = new Dialog();
                d2.setWidth(60, Unit.PERCENTAGE);
                Dialog d3 = new Dialog();
                d3.setWidth(60, Unit.PERCENTAGE);

                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");
                info.getStyle().set("color", "red");

                H2 H2 = new H2("Verzeichnis-Liste: Link Tile");
                H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");
                H2.getStyle().set("margin", "0 auto 0 0");

                Button sb1 = new Button("Speichern");
                Button db1 = new Button("Ja, Löschen");
                db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button cancelButton = new Button("Nein, Abbrechen");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");
                createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());
                maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());
                minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                TextField tf1 = new TextField("Name");
                tf1.setWidthFull();
                TextField tf2 = new TextField("Description");
                tf2.setWidthFull();
                NumberField tf3 = new NumberField("Sort");
                tf3.setWidthFull();
                NumberField tf4 = new NumberField("Tile Column Id");
                tf4.setWidthFull();

                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2, tf3, tf4);
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);
                dialogLayout.setSizeFull();

                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                d1.open();
                d1.setCloseOnOutsideClick(false);
                d1.setWidthFull();
                d1.getFooter().add();

                LinkTile_grid.setVisible(d1.isOpened());
                LinkTile_grid.addComponentColumn(Tools -> {
                    Button deleteButton = new Button(VaadinIcon.TRASH.create());
                    Button editButton = new Button(VaadinIcon.EDIT.create());
                    Button sb2 = new Button("Speichern");

                    editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    editButton.addClickListener(Click -> {
                        tf1.setValue(Tools.getName());
                        tf2.setValue(Tools.getDescription());
                        tf3.setValue(Double.valueOf(Tools.getSort()));
                        tf4.setValue(Double.valueOf(Tools.getTile_Column_Id()));
                        sb2.addClickListener(click -> {
                            final int i;
                            i = Integer.parseInt(Tools.getId());
                            if (tf1.isEmpty()) {
                                tf1.setValue(Tools.getName());
                            }
                            if (tf2.isEmpty()) {
                                tf2.setValue(Tools.getDescription());
                            }
                            if (tf3.isEmpty()) {
                                tf3.setValue(Double.valueOf(Tools.getSort()));
                            }
                            if (tf4.isEmpty()) {
                                tf4.setValue(Double.valueOf(Tools.getTile_Column_Id()));
                            }
                            databaseUtils.editInfoLink_Tile(i, tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue());
                            LinkTile_grid.getDataProvider().refreshAll();
                            d1.close();
                            d2.close();
                            d3.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });

                        d3.open();
                        d3.add(dialogLayout);
                        d3.getFooter().add(sb2);
                    });

                    deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    deleteButton.addClickListener(Click -> {
                        final int i;
                        i = Integer.parseInt(Tools.getId());
                        d2.open();
                        d2.setHeaderTitle("Verzeichnis " + i);
                        d2.setCloseOnOutsideClick(false);
                        d2.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");
                        d2.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            databaseUtils.deleteInfoLink_Tile(Integer.parseInt(Tools.getId()));
                            LinkTile_grid.getDataProvider().refreshAll();
                            d1.close();
                            d2.close();
                            d3.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                }).setWidth("125px").setFlexGrow(0);

                sb1.addClickListener(Click -> {
                    if (tf1.isEmpty()) {
                        tf1.setValue("N/A");
                    }
                    if (tf2.isEmpty()) {
                        tf2.setValue("N/A");
                    }
                    if (tf3.isEmpty()) {
                        tf3.setValue(Double.valueOf("1"));
                    }
                    if (tf4.isEmpty()) {
                        tf4.setValue(Double.valueOf("1"));
                    }
                    databaseUtils.addNewIdAndName_Link_Tile(tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue());
                    LinkTile_grid.getDataProvider().refreshAll();
                    d1.close();
                    d2.close();
                    d3.close();
                    Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                });
                cancelButton.addClickListener(Click -> {
                    d3.close();
                    d2.close();
                });
                createButton.addClickListener(Click -> {
                    d3.open();
                    d3.add(dialogLayout);
                    d3.setCloseOnOutsideClick(false);
                    d3.getFooter().add(sb1, cancelButton);
                });
                closeButton.addClickListener(Click -> {
                    d1.close();
                    LinkTile_grid.removeAllColumns();
                });
                maximizeButton.addClickListener(Click -> LinkTile_grid.setAllRowsVisible(true));
                minimizeButton.addClickListener(Click -> LinkTile_grid.setAllRowsVisible(false));
                d1.add(heading, tools, LinkTile_grid);
            });

            layout_content.add(menuBar);

        }
        if (tab.equals(home)) {
            H2 Title = new H2("Startseite WebClient");
            layout_content.add(Title);
        }
        add(layout_content, layout_dialog);
    }
}
//Copyright LukasG1mLY

//Git Repository

//Status: OpenSource