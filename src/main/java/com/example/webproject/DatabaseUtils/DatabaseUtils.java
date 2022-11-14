package com.example.webproject.DatabaseUtils;

import com.example.webproject.Listen.InfoBox;
import com.example.webproject.Listen.Ldap;
import com.example.webproject.Listen.Link;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils extends SQLUtils {

    public DatabaseUtils() throws IOException {
        Wini ini = new Wini(new File("src/main/resources/application.properties"));

        String pDatabaseUrl = ini.get("pDatabaseUrl", "spring.datasource.url");
        String pUser = ini.get("pUser", "spring.datasource.username");
        String pPassword = ini.get("pPassword", "spring.datasource.password");

        try {
            this.setDatabase(pDatabaseUrl, pUser, pPassword);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public String getInfoStaff() {

        ResultSet rs;

        try {
            rs = onQuery("SELECT INFO FROM INFOBOX WHERE ROLLE = 'staff'");
            rs.next();
            return rs.getString("INFO");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery");
            return "";
        }
    }
    public String getInfoStudent() {

        ResultSet rs;

        try {
            rs = onQuery("SELECT INFO FROM INFOBOX WHERE ROLLE = 'student'");
            rs.next();
            return rs.getString("INFO");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery");
            return "";
        }
    }
    public void editInfoStaff(String text) {
        try {
            onExecute("UPDATE INFOBOX SET INFO = ? WHERE ROLLE = 'staff'", text);
            System.out.println("Changed Info Text for staff");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute");

        }
    }
    public void editInfoStudent(String text) {
        try {
            onExecute("UPDATE INFOBOX SET INFO = ? WHERE ROLLE = 'student'", text);
            System.out.println("Changed Info Text for student");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute");
        }
    }
    public void deleteInfoLDAP(int id)  {
        try {
            onExecute("DELETE FROM LDAP_GRP WHERE ID =?", id);
            System.out.println("Deleted ROW_ " + (id));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (id));
        }


    }
    public void deleteInfoLink(int id)  {
        try {
            onExecute("DELETE FROM LINK WHERE ID =?", id);
            System.out.println("Deleted ROW_" + (id));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (id));
        }


    }
    public void addNewIdAndName_Link(String Linktext, String Link_group_ID, String Sort, String Description, String Url_Active, String Url_inActive, String Active, String Auth_Level, String NewTab) {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LINK ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LINK VALUES(?,?,?,?,?,?,?,?,?,?)", newId, Linktext, Link_group_ID, Sort, Description, Url_Active, Url_inActive, Active, Auth_Level, NewTab);
            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addNewIdAndName_Ldap(String name) {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LDAP_GRP ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LDAP_GRP VALUES(?,?)", newId, name);
            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<InfoBox> getInfo_InfoBox() {
        ResultSet rs;
        List<InfoBox> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT ID,ROLLE,INFO FROM INFOBOX ORDER BY ID");
            while (rs.next()) {
                list.add(new InfoBox(rs.getString("ROLLE"), rs.getString("INFO")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery by Infobox");
        }
        return list;
    }
    public List<Ldap> getInfo_Ldap() {
        ResultSet rs;
        List<Ldap> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT ID,GRP_NAME FROM LDAP_GRP ORDER BY ID");
            while (rs.next()) {
                list.add(new Ldap(rs.getString("ID"), rs.getString("GRP_NAME")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Link> getInfo_Link() {
        ResultSet rs;
        List<Link> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT * FROM LINK ORDER BY ID");
            while (rs.next()) {
                list.add(new Link(
                        rs.getString("ID"),
                        rs.getString("LINKTEXT"),
                        rs.getString("LINK_GRP_ID"),
                        rs.getString("SORT"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("URL_ACTIVE"),
                        rs.getString("URL_INACTIVE"),
                        rs.getString("ACTIVE"),
                        rs.getString("AUTH_LEVEL"),
                        rs.getString("NEWTAB")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void editInfoLink(int Id, String Linktext, String Link_group_ID, String Sort, String Description, String Url_Active, String Url_inActive, String Active, String Auth_Level, String NewTab) {


        try {
            onExecute("UPDATE LINK SET LINKTEXT =?,LINK_GRP_ID =?,SORT =?,DESCRIPTION =?,URL_ACTIVE =?,URL_INACTIVE =?,ACTIVE =?,AUTH_LEVEL =?,NEWTAB =? WHERE ID =?",Linktext, Link_group_ID, Sort, Description, Url_Active, Url_inActive, Active, Auth_Level, NewTab, Id + 2);
            System.out.println("Changed Info LINK_" + (Id + 2));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LINK_" + Id);
        }

    }
}