package com.example.webproject.Listen;

public class LDAP_ROLE {

    String ID, Content;

    public LDAP_ROLE(String pId, String pContent) {
        ID = pId;
        Content = pContent;
    }

    public String getContent() {return Content;}
    public String getId() {return ID;}
}
