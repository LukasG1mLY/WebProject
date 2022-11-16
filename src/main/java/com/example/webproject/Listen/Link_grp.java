package com.example.webproject.Listen;

public class Link_grp {

    String Id, Grp_Linktext, Icon_Id, Tile_Id, Sort, Description ;

    public Link_grp(String pId, String pGrp_Linktext, String pIcon_Id, String pTile_Id, String pSort, String pDescription) {
        Id = pId;
        Grp_Linktext = pGrp_Linktext;
        Icon_Id = pIcon_Id;
        Tile_Id = pTile_Id;
        Sort = pSort;
        Description = pDescription;

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getGrp_Linktext() {
        return Grp_Linktext;
    }

    public void setGrp_Linktext(String grp_Linktext) {
        Grp_Linktext = grp_Linktext;
    }

    public String getIcon_Id() {
        return Icon_Id;
    }

    public void setIcon_Id(String icon_Id) {
        Icon_Id = icon_Id;
    }

    public String getTile_Id() {
        return Tile_Id;
    }

    public void setTile_Id(String tile_Id) {
        Tile_Id = tile_Id;
    }

    public String getSort() {
        return Sort;
    }

    public void setSort(String sort) {
        Sort = sort;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
