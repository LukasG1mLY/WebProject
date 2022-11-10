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
import java.util.Set;

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
    public String doesIdExits_GetContent_Ldap(int id) {
        ResultSet rs;

        try {
            rs = onQuery("SELECT GRP_NAME FROM LDAP_GRP WHERE ID =?", id);
            rs.next();
               return rs.getString("GRP_NAME");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery by DoesIDExits");
        }
        return null;
    }
    public String doesIdExits_GetContent_Link(int id, String Id, String Linktext, String Link_group_ID, String Sort, String Description, String Url_Active, String Url_inActive, String Active, String Auth_Level, String NewTab) {
        ResultSet rs;
        List<Link> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT ID,LINKTEXT,LINK_GRP_ID,SORT,DESCRIPTION,URL_ACTIVE,URL_INACTIVE,ACTIVE,AUTH_LEVEL,NEWTAB FROM LINK WHERE ID =?", id);
            while (rs.next()) {
                list.add(new Link(
                        rs.getString(Id),
                        rs.getString(Linktext),
                        rs.getString(Link_group_ID),
                        rs.getString(Sort),
                        rs.getString(Description),
                        rs.getString(Url_Active),
                        rs.getString(Url_inActive),
                        rs.getString(Active),
                        rs.getString(Auth_Level),
                        rs.getString(NewTab)));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery by DoesIDExits");
        }
        return null;
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


    public void deleteInfoLink(Set<Ldap> id)  {
        try {
            onExecute("DELETE FROM LINK WHERE ID =?", id);
            System.out.println("Deleted ROW_" + (id));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (id));
        }


    }


    public void editInfoLDAP(int id, String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID =?", text, id);
            System.out.println("Changed Info LDAP_ID_" + id );
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_" + id);
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
            rs = onQuery("SELECT ID,LINKTEXT,LINK_GRP_ID,SORT,DESCRIPTION,URL_ACTIVE,URL_INACTIVE,ACTIVE,AUTH_LEVEL,NEWTAB FROM LINK ORDER BY ID");
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
    public void addNewIdAndName(String name) {
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

    public String doesIdExits_GetContent_Link(String placeholder, String id) {

        return placeholder;
    }

    public Object doesIdExits_GetContent_Link(int parseInt) {
        return parseInt;
    }

    public void deleteInfoLink(String toString) {
        try {
            onExecute("DELETE FROM LINK WHERE ID =?", toString);
            System.out.println("Deleted ROW_" + (toString));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (toString));
        }


    }
}
