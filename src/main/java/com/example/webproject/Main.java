package com.example.webproject;

import com.example.webproject.DatabaseUtils.DatabaseUtils;
import com.example.webproject.Listen.InfoBox;
import com.example.webproject.Listen.Ldap;
import com.example.webproject.Listen.Link;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
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

        layout_content.removeAll();
        layout_grid.removeAll();
        layout_dialog.removeAll();

        if (tab.equals(edit)) {

            layout_content.setVisible(true);
            layout_grid.setVisible(true);
            layout_dialog.setVisible(true);

            layout_dialog.setSpacing(true);
            layout_dialog.setPadding(false);

            List<InfoBox> InfoBox = databaseUtils.getInfo_InfoBox();

            Grid<InfoBox> InfoBox_grid = new Grid<>();
            InfoBox_grid.addColumn(com.example.webproject.Listen.InfoBox::getRolle).setHeader("Rolle");
            InfoBox_grid.addColumn(com.example.webproject.Listen.InfoBox::getInfo).setHeader("Info");
            InfoBox_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
            InfoBox_grid.setSelectionMode(Grid.SelectionMode.MULTI);
            InfoBox_grid.setItems(InfoBox);
            InfoBox_grid.setVisible(false);

            List<Link> Link = databaseUtils.getInfo_Link();

            Grid<Link> Link_grid = new Grid<>(com.example.webproject.Listen.Link.class, false);
            Link_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
            Link_grid.setSelectionMode(Grid.SelectionMode.SINGLE);

            Link_grid.addColumn(com.example.webproject.Listen.Link::getId).setHeader("ID");
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

            Ldap_grid.addColumn(com.example.webproject.Listen.Ldap::getId).setHeader("ID").setWidth("75px").setFlexGrow(1);
            Ldap_grid.addColumn(com.example.webproject.Listen.Ldap::getContent).setHeader("Content").setAutoWidth(true);
            Ldap_grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
            Ldap_grid.setSelectionMode(Grid.SelectionMode.MULTI);
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

               H2 H2 = new H2("Verzeichnis-Liste: Link");
               H2.getStyle().set("margin", "0 auto 0 0");

               H2 H3 = new H2("");
               H2.getStyle().set("margin", "0 auto 0 0");

               Button b1 = new Button(VaadinIcon.PLUS.create());
               b1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

               Button b2 = new Button(VaadinIcon.CLOSE.create());
               b2.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

               Button b3 = new Button(VaadinIcon.EDIT.create());
               b3.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
               b3.setEnabled(false);

               Button b4 = new Button(VaadinIcon.TRASH.create());
               b4.setEnabled(false);
               b4.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
               b4.getStyle().set("margin", "0 auto 0 0 0 0");

               HorizontalLayout heading = new HorizontalLayout(H2, b2);
               heading.setAlignItems(FlexComponent.Alignment.CENTER);

               HorizontalLayout tools = new HorizontalLayout(H3, b1, b3, b4);
               heading.setAlignItems(FlexComponent.Alignment.CENTER);

               Ldap_grid.setAllRowsVisible(false);

               d1.open();
               d1.setCloseOnOutsideClick(false);
               d1.setWidthFull();
               d1.add(heading, tools);

               Ldap_grid.setVisible(d1.isOpened());
               d1.add(Ldap_grid);

               b2.addClickListener(Click -> d1.close());
           });
            Link_item.addClickListener(e -> {
                Dialog d1 = new Dialog();

                Dialog d2 = new Dialog();

                Button b5 = new Button("Bestätigen");
                b5.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

                Button b6 = new Button("Abbrechen");
                b6.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

                H2 H2 = new H2("Verzeichnis-Liste: Link");
                H2.getStyle().set("margin", "0 auto 0 0");

                H2 H3 = new H2("");
                H2.getStyle().set("margin", "0 auto 0 0");

                Button b1 = new Button(VaadinIcon.PLUS.create());
                b1.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);

                Button b2 = new Button(VaadinIcon.CLOSE.create());
                b2.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

                Button b3 = new Button(VaadinIcon.EDIT.create());
                b3.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                b3.setEnabled(false);

                Button b4 = new Button(VaadinIcon.TRASH.create());
                b4.setEnabled(false);
                b4.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
                b4.getStyle().set("margin", "0 auto 0 0 0 0");

                HorizontalLayout heading = new HorizontalLayout(H2, b2);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                HorizontalLayout tools = new HorizontalLayout(H3, b1, b3, b4);
                heading.setAlignItems(FlexComponent.Alignment.CENTER);

                d1.open();
                d1.setCloseOnOutsideClick(false);
                d1.setWidthFull();

                Link_grid.addSelectionListener(selection -> {
                    int size = selection.getAllSelectedItems().size();
                    boolean isSingleSelection = size == 1;
                    b3.setEnabled(isSingleSelection);

                    b4.setEnabled(size != 0);



                });




                b5.addClickListener(Click -> {
                });

                Link_grid.setVisible(d1.isOpened());

                d1.add(heading, tools, Link_grid);

                b2.addClickListener(Click -> d1.close());

                b4.addClickListener(Click -> {
                    d2.open();
                    d2.setHeaderTitle("Sind sie sicher, dass sie dieses Verzeichnis Unwiderruflich Löschen Wollen ?");
                    d2.getFooter().add(b5, b6);

                });

            });

            layout_grid.setMaxWidth(100, Unit.PERCENTAGE);
            layout_content.setAlignItems(FlexComponent.Alignment.CENTER);
            layout_content.add(menuBar);
            layout_grid.add(InfoBox_grid, Ldap_grid, Link_grid);
        }
        if (tab.equals(home)) {
            System.out.println("Home wurde Gedrückt");
        }
    }
}
