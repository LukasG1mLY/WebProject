package com.example.webproject.DatabaseUtils;

import com.example.webproject.Listen.*;
import org.ini4j.Wini;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
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
    public void editInfoLDAP(int id, String text) {
        try {
            onExecute("UPDATE LDAP_GRP SET GRP_NAME =? WHERE ID =?", text, id);
            System.out.println("Changed Info LDAP_ID_" + id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ID_" + id);
        }
    }
    public void editInfoLDAP_ROLE(int ID, String Content) {
        try {
            onExecute("UPDATE LDAP_ROLE SET ROLE_NAME =? WHERE ID =?",Content, ID);
            System.out.println("Changed Info LDAP_ROLE " + ID);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LDAP_ROLE " + ID);
        }
    }
    public void editInfoLink(int Id, String Linktext, Double Link_group_ID, Double Sort, String Description, String Url_Active, Double Url_inActive, Double Active, Double Auth_Level, Double NewTab) {

        try {
            onExecute("UPDATE LINK SET LINKTEXT =?,LINK_GRP_ID =?,SORT =?,DESCRIPTION =?,URL_ACTIVE =?,URL_INACTIVE =?,ACTIVE =?,AUTH_LEVEL =?,NEWTAB =? WHERE ID =?",Linktext, Link_group_ID, Sort, Description, Url_Active, Url_inActive, Active, Auth_Level, NewTab, Id);
            System.out.println("Changed Info LINK_" + (Id));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by LINK_" + Id);
        }

    }
    public void editInfoLink_Grp(int id, String pGrp_Linktext, Double pIcon_Id, Double pTile_Id, Double pSort, String pDescription) {
        try {
            onExecute("UPDATE LINK_GRP SET GRP_LINKTEXT =?, ICON_ID =?, TILE_ID =?, SORT =?, DESCRIPTION =? WHERE ID =?", pGrp_Linktext, pIcon_Id, pTile_Id, pSort, pDescription, id);
            System.out.println("Changed Info Link group:" + id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by Link group:" + id);
        }
    }
    public void editInfoLink_Tile(int id, String pName, String pDescription, Double pSort, String pTile_Column_Id) {
        try {
            onExecute("UPDATE LINK_TILE SET NAME =?, DESCRIPTION =?, SORT =?, TILE_COLUMN_ID =? WHERE ID =?", pName, pDescription, pSort, pTile_Column_Id, id);
            System.out.println("Changed Info Link Tile:" + id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onExecute by Link Tile:" + id);
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
    public void deleteInfoLDAP_ROLE(int ID) {
        try {
            onExecute("DELETE FROM LDAP_ROLE WHERE ID =?", ID);
            System.out.println("Deleted ROW_" + (ID));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (ID));
        }
    }
    public void deleteInfoLink_Grp(int id)  {
        try {
            onExecute("DELETE FROM LINK_GRP WHERE ID =?", id);
            System.out.println("Deleted ROW_" + (id));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (id));
        }


    }
    public void deleteInfoLink_Tile(int id)  {
        try {
            onExecute("DELETE FROM LINK_TILE WHERE ID =?", id);
            System.out.println("Deleted ROW_" + (id));

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete ROW_ " + (id));
        }


    }
    public void addNewIdAndName_Link(String Linktext, Double Link_group_ID, Double Sort, String Description, String Url_Active, Double Url_inActive, Double Active, Double Auth_Level, Double NewTab) {
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
    public void addNewIdAndName_ROLE(String Content) {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LDAP_ROLE ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LDAP_ROLE VALUES(?,?)", newId, Content);
            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addNewIdAndName_Link_Grp(String pGrp_Linktext, Double pIcon_Id, Double pTile_Id, Double pSort, String pDescription) {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LINK_GRP ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LINK_GRP VALUES(?,?,?,?,?,?)", newId, pGrp_Linktext, pIcon_Id, pTile_Id, pSort, pDescription);
            System.out.println("Die ID: " + newId + " wurde zum verzeichnis Hinzugefügt.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addNewIdAndName_Link_Tile(String pName, String pDescription, Double pSort, String pTile_Column_Id) {
        try
        {
            ResultSet rs = onQuery("SELECT MAX(ID) FROM LINK_TILE ORDER BY ID");
            rs.next();
            int newId = rs.getInt("MAX(ID)") + 1;
            onExecute("INSERT INTO LINK_TILE VALUES(?,?,?,?,?)", newId, pName, pDescription, pSort, pTile_Column_Id);
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
    public List<LDAP_ROLE> getAllInfos_LDAP_ROLE() {
        ResultSet rs;
        List<LDAP_ROLE> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT ID,ROLE_NAME FROM LDAP_ROLE ORDER BY ID");
            while (rs.next()) {
                list.add(new LDAP_ROLE(rs.getString("ID"), rs.getString("ROLE_NAME")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Link_grp> getInfo_Link_Grp() {
        ResultSet rs;
        List<Link_grp> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT * FROM LINK_GRP ORDER BY ID");
            while (rs.next()) {
                list.add(new Link_grp(
                        rs.getString("ID"),
                        rs.getString("GRP_LINKTEXT"),
                        rs.getString("ICON_ID"),
                        rs.getString("TILE_ID"),
                        rs.getString("SORT"),
                        rs.getString("DESCRIPTION")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Link_Tile> getInfo_Link_Tile() {
        ResultSet rs;
        List<Link_Tile> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT * FROM LINK_TILE ORDER BY ID");
            while (rs.next()) {
                list.add(new Link_Tile(
                        rs.getString("ID"),
                        rs.getString("NAME"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("SORT"),
                        rs.getString("TILE_COLUMN_ID")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void getInfoIcon(int i) {
        ResultSet rs;
        try {
            rs = onQuery("SELECT ICON FROM ICON WHERE ID =?", i);
            while (rs.next()) {
                Blob aBlob = rs.getBlob("ICON");
                InputStream is = aBlob.getBinaryStream(1, aBlob.length());
                BufferedImage bufferedImage = ImageIO.read(is);
                File outputfile = new File("src/main/resources/images","Icon"+i+".png");
                System.out.println(outputfile.getName());;
                ImageIO.write(bufferedImage, "png", outputfile);
                onExecute("UPDATE ICON SET URL =? WHERE ID =?", outputfile.getPath(), i);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<dbIcon> getIconImage() {
        ResultSet rs;
        List<dbIcon> list = new ArrayList<>();
        try {
            rs = onQuery("SELECT ID,CONTENTTYPE FROM ICON ORDER BY ID");
            while (rs.next()) {
                list.add(new dbIcon(
                        rs.getString("ID"),
                        rs.getString("CONTENTTYPE")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public String getLinkImage() {

        ResultSet rs;

        try {
            rs = onQuery("SELECT URL FROM ICON ORDER BY ID");
            rs.next();
            return rs.getString("URL");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed onQuery");
            return "";
        }
    }
    public String deleteIcons() {



        return null;
    }

}