package com.example.webproject.Listen;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.sql.SQLException;

public class Icon {
    String Id;
    Image Icon;
    String ContentType;

    public Icon(String pId, Blob pBlob, String pContentType) {
        Id = pId;
        ContentType = pContentType;
        Icon = new Image(new StreamResource("", (InputStreamFactory) () -> {
            try {
                return pBlob.getBinaryStream();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }), "");
    }

    public String getId() {return Id;}

    public void setId(String pId) {Id = pId;}

    public Image getIcon() {return Icon;}

    public String getContentType() {return ContentType;}
}