package com.example.webproject;

import java.util.ArrayList;

public class Items {

    public ArrayList<String> button;

    public Items() {
        button = new ArrayList<>();

        button.add("Übersicht");
        button.add("Hinzufügen");
        button.add("Bearbeiten");
    }
    public String getButtons(int j) {
        return button.get(j);
    }
}
