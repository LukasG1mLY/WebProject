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
import java.util.List;


@Route("")
public class Main extends Div {
    public DatabaseUtils databaseUtils;
    private final VerticalLayout layout_content;
    private final VerticalLayout layout_dialog;
    private final Tab edit;
    private final Tab home;
    public Main() throws IOException {
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

            MenuBar menuBar = new MenuBar();menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON, MenuBarVariant.MATERIAL_OUTLINED);

            MenuItem Infobox_item = menuBar.addItem("Infotext");
            MenuItem Ldap_item = menuBar.addItem("Ldap");
            MenuItem Link_item = menuBar.addItem("Link");
            MenuItem Ldap_Role_item = menuBar.addItem("Ldap Role");
            MenuItem Link_Grp_item = menuBar.addItem("Link Group");
            MenuItem Link_Tile_item = menuBar.addItem("Link Tile");
            MenuItem Icon_item = menuBar.addItem("Icon");

            Infobox_item.addClickListener(e -> {
                List<InfoBox> InfoBox = databaseUtils.getInfo_InfoBox();
                Grid<InfoBox> InfoBox_grid = new Grid<>();
                H2 H2 = new H2("Verzeichnis-Liste: Infobox");H2.getStyle().set("margin", "0 auto 0 0");
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button editButton = new Button("Bearbeiten");editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button saveButton = new Button("Speichern");saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                TextField tf1 = new TextField("Mitabeiter");tf1.setWidthFull();
                TextField tf2 = new TextField("Studenten");tf2.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);
                HorizontalLayout heading = new HorizontalLayout(H2, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setWidthFull();gridDialog.setCloseOnOutsideClick(false);gridDialog.getFooter().add(editButton);
                Dialog editDialog = new Dialog();editDialog.setHeaderTitle("Bearbeiten");editDialog.setCloseOnOutsideClick(false);editDialog.getFooter().add(saveButton);

                InfoBox_grid.addColumn(com.example.webproject.Listen.InfoBox::getRolle).setHeader("Rolle");
                InfoBox_grid.addColumn(com.example.webproject.Listen.InfoBox::getInfo).setHeader("Info");
                InfoBox_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                InfoBox_grid.setSelectionMode(Grid.SelectionMode.NONE);
                InfoBox_grid.getDataProvider().refreshAll();
                InfoBox_grid.setItems(InfoBox);

                editButton.addClickListener(Click -> {
                    tf1.setValue(databaseUtils.getInfoStaff());
                    tf2.setValue(databaseUtils.getInfoStudent());

                    editDialog.open();
                    editDialog.add(tf1, tf2);
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
                    editDialog.close();
                    gridDialog.close();
                    Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();
                    editDialog.close();
                });

                gridDialog.add(heading, InfoBox_grid);
            });
            Ldap_item.addClickListener(e -> {
                List<Ldap> Ldap = databaseUtils.getInfo_Ldap();
                Grid<Ldap> Ldap_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Ldap");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button saveButton = new Button("Speichern");saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Löschen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> Ldap_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> Ldap_grid.setAllRowsVisible(false));
                TextField tf1 = new TextField("Linktext");tf1.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.add(info);deleteDialog.setCloseOnOutsideClick(false);
                Dialog editDialog = new Dialog();editDialog.setHeaderTitle("Verzeichnis Bearbeiten");editDialog.setCloseOnOutsideClick(false);editDialog.add(tf1);editDialog.getFooter().add(saveButton, cancelButton);editDialog.setWidth(60, Unit.PERCENTAGE);

                Ldap_grid.addColumn(com.example.webproject.Listen.Ldap::getId).setHeader("ID").setFlexGrow(0);
                Ldap_grid.addColumn(com.example.webproject.Listen.Ldap::getContent).setHeader("Content").setAutoWidth(true);
                Ldap_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                Ldap_grid.setItems(Ldap);

                Ldap_grid.addComponentColumn(Tools -> {int i = Integer.parseInt(Tools.getId());

                    Button deleteButton = new Button(VaadinIcon.TRASH.create()); deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    Button editButton = new Button(VaadinIcon.EDIT.create());editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                    editButton.addClickListener(Click -> {
                        tf1.setValue(Tools.getContent());
                        editDialog.open();
                        editDialog.add(saveButton, cancelButton);
                        saveButton.addClickListener(click -> {
                            if (tf1.isEmpty()) {
                                tf1.setValue("N/A");
                            }
                            databaseUtils.editInfoLDAP(i, tf1.getValue());tf1.setValue("Unsichtbar");
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Ldap_grid.getDataProvider().refreshAll();

                            Notification.show("Erfolgreich Geändert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    deleteButton.addClickListener(Click -> {
                        deleteDialog.open();
                        deleteDialog.getFooter().add(saveButton, cancelButton);
                        saveButton.addClickListener(click -> {
                            databaseUtils.deleteInfoLDAP(Integer.parseInt(Tools.getId()));
                            gridDialog.close();deleteDialog.close();editDialog.close();

                            Ldap_grid.getDataProvider().refreshAll();

                            Notification.show("Erfolgreich Gelöscht", 5000, Notification.Position.TOP_CENTER);
                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                }).setFlexGrow(0);
                cancelButton.addClickListener(Click -> {
                    gridDialog.close();
                    deleteDialog.close();
                    editDialog.close();
                });
                createButton.addClickListener(Click -> {editDialog.open();
                    saveButton.addClickListener(click -> {if (tf1.isEmpty()) {
                        tf1.setValue("N/A");
                    }

                        databaseUtils.addNewIdAndName_Ldap(tf1.getValue());
                        Ldap_grid.getDataProvider().refreshAll();
                        gridDialog.close();deleteDialog.close();editDialog.close();
                        Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                    });
                });
                closeButton.addClickListener(Click -> {gridDialog.close();
                    deleteDialog.close();
                    editDialog.close();});

                gridDialog.add(heading, tools, Ldap_grid);
            });
            Link_item.addClickListener(e -> {
                List<Link> Link = databaseUtils.getInfo_Link();
                Grid<Link> Link_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Link");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Löschen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> Link_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> Link_grid.setAllRowsVisible(false));
                TextField tf1 = new TextField("Linktext");tf1.setWidthFull();
                TextField tf4 = new TextField("Description");tf4.setWidthFull();
                TextField tf5 = new TextField("URL Active");tf5.setWidthFull();
                NumberField tf2 = new NumberField("Link Group ID");tf2.setWidthFull();
                NumberField tf3 = new NumberField("Sort");tf3.setWidthFull();
                NumberField tf6 = new NumberField("URL InActive");tf6.setWidthFull();
                NumberField tf7 = new NumberField("Active");tf7.setWidthFull();
                NumberField tf8 = new NumberField("Auth Level");tf8.setWidthFull();
                NumberField tf9 = new NumberField("NewTab");tf9.setWidthFull();
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.getFooter().add();
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.getFooter().add(sb1, cancelButton);
                Dialog editDialog = new Dialog(); editDialog.setCloseOnOutsideClick(false);editDialog.setHeaderTitle("Verzeichnis Bearbeiten");editDialog.add(tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9);editDialog.add(sb1, cancelButton);editDialog.setWidth(60, Unit.PERCENTAGE);
                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);

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
                Link_grid.setVisible(gridDialog.isOpened());

                Link_grid.addComponentColumn(Tools -> {int i = Integer.parseInt(Tools.getId());

                    Button deleteButton = new Button(VaadinIcon.TRASH.create());deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    Button editButton = new Button(VaadinIcon.EDIT.create());editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                    editButton.addClickListener(Click -> {editDialog.open();
                        try {
                            tf1.setValue(Tools.getLinktext());
                        } catch (NullPointerException npe) {
                            tf1.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf2.setValue(Double.valueOf(Tools.getLink_grp_id()));
                        } catch (NullPointerException npe) {
                            tf2.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf3.setValue(Double.valueOf(Tools.getSort()));
                        } catch (NullPointerException npe) {
                            tf3.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf4.setValue((Tools.getDescription()));
                        } catch (NullPointerException npe) {
                            tf4.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf5.setValue(Tools.getUrl_active());
                        } catch (NullPointerException npe) {
                            tf5.setPlaceholder("Momentan keine Beschreibung Vorhanden");
                        }
                        try {
                            tf6.setValue(Double.valueOf(Tools.getUrl_inactive()));
                        } catch (NullPointerException npe) {
                            tf2.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf7.setValue(Double.valueOf(Tools.getActive()));
                        } catch (NullPointerException npe) {
                            tf3.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf8.setValue(Double.valueOf(Tools.getAuth_level()));
                        } catch (NullPointerException npe) {
                            tf4.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf9.setValue(Double.valueOf(Tools.getNewtab()));
                        } catch (NullPointerException npe) {
                            tf5.setPlaceholder("Momentan ist nichts Vorhanden");
                        }

                        sb1.addClickListener(click -> {
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
                                tf4.setValue("N/A");
                            }
                            if (tf5.isEmpty()) {
                                tf5.setValue("N/A");
                            }
                            if (tf6.isEmpty()) {
                                tf6.setValue(Double.valueOf("0"));
                            }
                            if (tf7.isEmpty()) {
                                tf7.setValue(Double.valueOf("0"));
                            }
                            if (tf8.isEmpty()) {
                                tf8.setValue(Double.valueOf("0"));
                            }
                            if (tf9.isEmpty()) {
                                tf9.setValue(Double.valueOf("0"));
                            }
                            databaseUtils.editInfoLink(i, tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue(), tf5.getValue(), tf6.getValue(), tf7.getValue(), tf8.getValue(), tf9.getValue());
                            tf1.setValue("Unsichtbar");
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Link_grid.getDataProvider().refreshAll();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    deleteButton.addClickListener(Click -> {
                        deleteDialog.open();

                        sb1.addClickListener(click -> {databaseUtils.deleteInfoLink(Integer.parseInt(Tools.getId()));
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Link_grid.getDataProvider().refreshAll();

                            Notification.show("Erfolgreich Gelöscht", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });

                    return new HorizontalLayout(editButton, deleteButton);
                }).setFlexGrow(0);
                cancelButton.addClickListener(Click -> {
                    gridDialog.close();
                    deleteDialog.close();
                    editDialog.close();
                });
                createButton.addClickListener(Click -> {
                    editDialog.open();
                    sb1.addClickListener(click -> {
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
                            tf4.setValue("N/A");
                        }
                        if (tf5.isEmpty()) {
                            tf5.setValue("N/A");
                        }
                        if (tf6.isEmpty()) {
                            tf6.setValue(Double.valueOf("0"));
                        }
                        if (tf7.isEmpty()) {
                            tf7.setValue(Double.valueOf("0"));
                        }
                        if (tf8.isEmpty()) {
                            tf8.setValue(Double.valueOf("0"));
                        }
                        if (tf9.isEmpty()) {
                            tf9.setValue(Double.valueOf("0"));
                        }
                        databaseUtils.addNewIdAndName_Link(tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue(), tf5.getValue(), tf6.getValue(), tf7.getValue(), tf8.getValue(), tf9.getValue());
                        Link_grid.getDataProvider().refreshAll();
                        gridDialog.close();
                        deleteDialog.close();
                        editDialog.close();
                        Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                    });
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();
                    deleteDialog.close();
                    editDialog.close();
                });

                gridDialog.add(heading, tools, Link_grid);
            });
            Ldap_Role_item.addClickListener(e -> {
                List<LDAP_ROLE> LdapRole = databaseUtils.getAllInfos_LDAP_ROLE();
                Grid<LDAP_ROLE> LdapRole_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Ldap Role");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Ja, Löschen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> LdapRole_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);  minimizeButton.addClickListener(Click -> LdapRole_grid.setAllRowsVisible(false));
                TextField tf1 = new TextField("Role Name");tf1.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.getFooter().add();
                Dialog deleteDialog = new Dialog();deleteDialog.setWidth(60, Unit.PERCENTAGE);deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");
                Dialog editDialog = new Dialog();editDialog.setWidth(60, Unit.PERCENTAGE);editDialog.add(dialogLayout);editDialog.setCloseOnOutsideClick(false);editDialog.getFooter().add(sb1, cancelButton);

                LdapRole_grid.addColumn(LDAP_ROLE::getId).setHeader("ID").setWidth("75px");
                LdapRole_grid.addColumn(LDAP_ROLE::getContent).setHeader("Role Name").setFlexGrow(1);
                LdapRole_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                LdapRole_grid.setItems(LdapRole);
                LdapRole_grid.getId();

                LdapRole_grid.addComponentColumn(Tools -> {
                    Button deleteButton = new Button(VaadinIcon.TRASH.create());deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    Button editButton = new Button(VaadinIcon.EDIT.create());editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    Button sb2 = new Button("Speichern");

                    editButton.addClickListener(Click -> {
                        tf1.setValue(Tools.getContent());
                        sb2.addClickListener(click -> {
                            int i = Integer.parseInt(Tools.getId());
                            if (tf1.isEmpty()) {tf1.setValue("N/A");
                            }
                            databaseUtils.editInfoLDAP_ROLE(i, tf1.getValue());
                            LdapRole_grid.getDataProvider().refreshAll();
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                        editDialog.open();
                    });
                    deleteButton.addClickListener(Click -> {
                        deleteDialog.open();
                        deleteDialog.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            databaseUtils.deleteInfoLDAP_ROLE(Integer.parseInt(Tools.getId()));
                            LdapRole_grid.getDataProvider().refreshAll();
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    return new HorizontalLayout(editButton, deleteButton);
                }).setFlexGrow(0);
                cancelButton.addClickListener(Click -> {
                    editDialog.close();deleteDialog.close();gridDialog.close();
                });
                createButton.addClickListener(Click -> {editDialog.open();
                    sb1.addClickListener(click -> {if (tf1.isEmpty()) {tf1.setValue("N/A");
                    }
                        databaseUtils.addNewIdAndName_ROLE(tf1.getValue());
                        gridDialog.close();deleteDialog.close();editDialog.close();
                        Notification.show("Erfolgreich Gespeichert",5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        LdapRole_grid.getDataProvider().refreshAll();
                    });
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();deleteDialog.close();gridDialog.close();
                });

                gridDialog.add(heading, tools, LdapRole_grid);
            });
            Link_Grp_item.addClickListener(e -> {

                List<Link_grp> LinkGrp = databaseUtils.getInfo_Link_Grp();
                Grid<Link_grp> LinkGrp_grid = new Grid<>();

                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Link Group");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button db1 = new Button("Ja, Löschen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> LinkGrp_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY); minimizeButton.addClickListener(Click -> LinkGrp_grid.setAllRowsVisible(false));
                TextField tf1 = new TextField("Linktext Gruppe");tf1.setWidthFull();
                TextField tf5 = new TextField("Beschreibung");tf5.setWidthFull();
                NumberField tf2 = new NumberField("Symbol Id");tf2.setWidthFull();
                NumberField tf3 = new NumberField("Kachel Id");tf3.setWidthFull();
                NumberField tf4 = new NumberField("Sortieren");tf4.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2, tf3, tf4, tf5);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();
                Dialog deleteDialog = new Dialog();deleteDialog.setWidth(60, Unit.PERCENTAGE);
                Dialog editDialog = new Dialog();editDialog.setWidth(60, Unit.PERCENTAGE);

                LinkGrp_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                LinkGrp_grid.addColumn(Link_grp::getId).setHeader("ID").setWidth("75px").setFlexGrow(0);
                LinkGrp_grid.addColumn(Link_grp::getGrp_Linktext).setHeader("Linktext_Grp");
                LinkGrp_grid.addColumn(Link_grp::getIcon_Id).setHeader("Icon_Id");
                LinkGrp_grid.addColumn(Link_grp::getTile_Id).setHeader("Tile_Id");
                LinkGrp_grid.addColumn(Link_grp::getSort).setHeader("Sort");
                LinkGrp_grid.addColumn(Link_grp::getDescription).setHeader("Description");
                LinkGrp_grid.addComponentColumn(Tools -> {

                    Button deleteButton = new Button(VaadinIcon.TRASH.create());deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                    Button editButton = new Button(VaadinIcon.EDIT.create()); editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    Button sb2 = new Button("Speichern");
                    VerticalLayout vl = new VerticalLayout(tf1, tf2, tf3, tf4, tf5);
                    try {
                        tf1.setValue(Tools.getGrp_Linktext());
                    } catch (NullPointerException npe) {
                        tf1.setPlaceholder("Momentan ist nichts Vorhanden");
                    }
                    try {
                        tf2.setValue(Double.valueOf(Tools.getIcon_Id()));
                    } catch (NullPointerException npe) {
                        tf2.setPlaceholder("Momentan ist nichts Vorhanden");
                    }
                    try {
                        tf3.setValue(Double.valueOf(Tools.getTile_Id()));
                    } catch (NullPointerException npe) {
                        tf3.setPlaceholder("Momentan ist nichts Vorhanden");
                    }
                    try {
                        tf4.setValue(Double.valueOf(Tools.getSort()));
                    } catch (NullPointerException npe) {
                        tf4.setPlaceholder("Momentan ist nichts Vorhanden");
                    }
                    try {
                        tf5.setValue(Tools.getDescription());
                    } catch (NullPointerException npe) {
                        tf5.setPlaceholder("Momentan keine Beschreibung Vorhanden");
                    }

                    editButton.addClickListener(Click -> {
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
                            gridDialog.close();
                            deleteDialog.close();
                            editDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                        editDialog.open();
                        editDialog.add(vl);
                        editDialog.getFooter().add(sb2);
                    });
                    deleteButton.addClickListener(Click -> {
                        final int i;
                        i = Integer.parseInt(Tools.getId());
                        deleteDialog.open();
                        deleteDialog.setHeaderTitle("Verzeichnis " + i);
                        deleteDialog.setCloseOnOutsideClick(false);
                        deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");
                        deleteDialog.getFooter().add(db1, cancelButton);
                        db1.addClickListener(click -> {
                            databaseUtils.deleteInfoLink_Grp(Integer.parseInt(Tools.getId()));
                            LinkGrp_grid.getDataProvider().refreshAll();
                            gridDialog.close();
                            deleteDialog.close();
                            editDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).add(new Button("Dialog Öffnen", klick -> gridDialog.open()));
                        });
                    });

                    return new HorizontalLayout(editButton, deleteButton);
                }).setWidth("125px").setFlexGrow(0);
                LinkGrp_grid.setItems(LinkGrp);
                LinkGrp_grid.setVisible(gridDialog.isOpened());

                cancelButton.addClickListener(Click -> {
                    editDialog.close();
                    deleteDialog.close();
                });
                createButton.addClickListener(Click -> {
                    editDialog.open();
                    editDialog.add(dialogLayout);
                    editDialog.setCloseOnOutsideClick(false);
                    editDialog.getFooter().add(sb1, cancelButton);

                    sb1.addClickListener(click -> {
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
                        gridDialog.close();deleteDialog.close();editDialog.close();
                        Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                    });
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();
                    deleteDialog.close();
                    editDialog.close();
                });

                gridDialog.add(heading, tools, LinkGrp_grid);
            });
            Link_Tile_item.addClickListener(e -> {

                List<Link_Tile> LinkTile = databaseUtils.getInfo_Link_Tile();
                Grid<Link_Tile> LinkTile_grid = new Grid<>();
                Label info = new Label("WARNUNG Dieser Vorgang kann nicht rückgängig gemacht werden");info.getStyle().set("color", "red");
                H2 H2 = new H2("Verzeichnis-Liste: Link Tile");H2.getStyle().set("margin", "0 auto 0 0");
                H2 H3 = new H2("");H2.getStyle().set("margin", "0 auto 0 0");
                Button sb1 = new Button("Speichern");sb1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                Button db1 = new Button("Ja, Löschen");db1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button createButton = new Button("Hinzufügen");createButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> LinkTile_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> LinkTile_grid.setAllRowsVisible(false));
                TextField tf1 = new TextField("Name");tf1.setWidthFull();
                TextField tf2 = new TextField("Description");tf2.setWidthFull();
                TextField tf4 = new TextField("Tile Column Id");tf4.setWidthFull();
                NumberField tf3 = new NumberField("Sort");tf3.setWidthFull();
                VerticalLayout dialogLayout = new VerticalLayout(tf1, tf2, tf3, tf4);dialogLayout.setPadding(false);dialogLayout.setSpacing(false);dialogLayout.setSizeFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                HorizontalLayout tools = new HorizontalLayout(H3, createButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.getFooter().add();
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");deleteDialog.getFooter().add(db1, cancelButton);deleteDialog.setWidth(60, Unit.PERCENTAGE);
                Dialog editDialog = new Dialog();editDialog.add(dialogLayout);editDialog.setCloseOnOutsideClick(false);editDialog.getFooter().add(sb1, cancelButton);editDialog.setWidth(60, Unit.PERCENTAGE);

                LinkTile_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                LinkTile_grid.addColumn(Link_Tile::getId).setHeader("ID").setWidth("75px").setFlexGrow(0);
                LinkTile_grid.addColumn(Link_Tile::getName).setHeader("Name");
                LinkTile_grid.addColumn(Link_Tile::getDescription).setHeader("Description").setFlexGrow(0);
                LinkTile_grid.addColumn(Link_Tile::getSort).setHeader("Sort").setFlexGrow(0);
                LinkTile_grid.addColumn(Link_Tile::getTile_Column_Id).setHeader("Tile Column Id");
                LinkTile_grid.setItems(LinkTile);

                LinkTile_grid.addComponentColumn(Tools -> {
                    Button deleteButton = new Button(VaadinIcon.TRASH.create());
                    Button editButton = new Button(VaadinIcon.EDIT.create());

                    editButton.addClickListener(Click -> {
                        try {
                            tf1.setValue(Tools.getName());
                        } catch (NullPointerException npe) {
                            tf1.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf2.setValue(Tools.getDescription());
                        } catch (NullPointerException npe) {
                            tf2.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf3.setValue(Double.valueOf(Tools.getSort()));
                        } catch (NullPointerException npe) {
                            tf3.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        try {
                            tf4.setValue(Tools.getTile_Column_Id());
                        } catch (NullPointerException npe) {
                            tf4.setPlaceholder("Momentan ist nichts Vorhanden");
                        }
                        sb1.addClickListener(click -> {
                            final int i;
                            i = Integer.parseInt(Tools.getId());
                            databaseUtils.editInfoLink_Tile(i, tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue());
                            LinkTile_grid.getDataProvider().refreshAll();
                            gridDialog.close();
                            deleteDialog.close();
                            editDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                        editDialog.open();
                        editDialog.add(dialogLayout);
                        editDialog.getFooter().add(sb1);
                    });
                    editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
                    deleteButton.addClickListener(Click -> {
                        deleteDialog.open();
                        db1.addClickListener(click -> {
                            databaseUtils.deleteInfoLink_Tile(Integer.parseInt(Tools.getId()));
                            LinkTile_grid.getDataProvider().refreshAll();
                            gridDialog.close();deleteDialog.close();editDialog.close();
                            Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        });
                    });
                    deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_ICON);
                    return new HorizontalLayout(editButton, deleteButton);
                }).setFlexGrow(0);
                cancelButton.addClickListener(Click -> {
                    editDialog.close();
                    deleteDialog.close();
                });
                createButton.addClickListener(Click -> {
                    editDialog.open();
                    sb1.addClickListener(click -> {
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
                            tf4.setValue("N/A");
                        }
                        databaseUtils.addNewIdAndName_Link_Tile(tf1.getValue(), tf2.getValue(), tf3.getValue(), tf4.getValue());
                        gridDialog.close();
                        deleteDialog.close();
                        editDialog.close();
                        Notification.show("Erfolgreich Gespeichert", 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
                        LinkTile_grid.getDataProvider().refreshAll();
                    });
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();
                    deleteDialog.close();
                    editDialog.close();
                });
                gridDialog.add(heading, tools, LinkTile_grid);
            });
            Icon_item.addClickListener(e -> {
                for (int i = 1; i < 17; i++) {
                    databaseUtils.getInfoIcon(i);
                }
                Grid<dbIcon> Icon_grid = new Grid<>();
                List<dbIcon> dbIcon = databaseUtils.getIconImage();
                H2 H2 = new H2("Verzeichnis-Liste: Link Tile");H2.getStyle().set("margin", "0 auto 0 0");
                Button cancelButton = new Button("Nein, Abbrechen");cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button closeButton = new Button(VaadinIcon.CLOSE.create());closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                Button maximizeButton = new Button(VaadinIcon.VIEWPORT.create());maximizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);maximizeButton.addClickListener(Click -> Icon_grid.setAllRowsVisible(true));
                Button minimizeButton = new Button(VaadinIcon.RESIZE_H.create());minimizeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);minimizeButton.addClickListener(Click -> Icon_grid.setAllRowsVisible(false));
                NumberField tf3 = new NumberField("Sort");tf3.setWidthFull();
                HorizontalLayout heading = new HorizontalLayout(H2, minimizeButton, maximizeButton, closeButton);heading.setAlignItems(FlexComponent.Alignment.CENTER);
                Dialog gridDialog = new Dialog();gridDialog.open();gridDialog.setCloseOnOutsideClick(false);gridDialog.setWidthFull();gridDialog.getFooter().add();
                Dialog deleteDialog = new Dialog();deleteDialog.setHeaderTitle("Verzeichnis Löschen ?");deleteDialog.setCloseOnOutsideClick(false);deleteDialog.add("Dieser Vorgang kann nicht Rückgänig gemacht werden !");deleteDialog.getFooter().add(cancelButton);deleteDialog.setWidth(60, Unit.PERCENTAGE);
                Icon_grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
                Icon_grid.addColumn(com.example.webproject.Listen.dbIcon::getId).setHeader("Id");
                Icon_grid.addComponentColumn(i -> new Image());
                Icon_grid.addColumn(com.example.webproject.Listen.dbIcon::getContentType).setHeader("Contenttype");
                Icon_grid.setItems(dbIcon);
                cancelButton.addClickListener(Click -> {
                    deleteDialog.close();
                });
                closeButton.addClickListener(Click -> {
                    gridDialog.close();
                    deleteDialog.close();

                });
                gridDialog.add(heading, Icon_grid);
            });

            layout_content.add(menuBar);
        }
        if (tab.equals(home)) {
            System.out.println("");
        }
        add(layout_content, layout_dialog);
    }
}


//Copyright LukasG1mLY

//WebProject by LukasG1mLY