package com.example.webproject.Listen;

import java.sql.Blob;

public class dbIcon {
    String Id, Icon, ContentType;

    public dbIcon(String pId, String pIcon, String pContentType) {
        Id = pId;
        Icon = pIcon;
        ContentType = pContentType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
    public String getIcon() {
        return Icon;
    }
    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }


}
