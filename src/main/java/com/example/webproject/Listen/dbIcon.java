package com.example.webproject.Listen;

import java.sql.Blob;

public class dbIcon {
    Blob icon;

    public dbIcon(Blob pIcon) {
        icon = pIcon;
    }

    public Blob getIcon() {
        return icon;
    }
}
