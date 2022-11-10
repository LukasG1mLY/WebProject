package com.example.webproject.Listen;

public class Ldap {

    String id, content;

    public Ldap(String pId, String pContent) {
        id = pId;
        content = pContent;
    }

    public String getContent() {return content;}
    public String getId() {return id;}
}
