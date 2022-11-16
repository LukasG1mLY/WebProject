package com.example.webproject.Listen;

public class Link_Tile {

    String Id, Name, Tile_Column_Id, Sort, Description ;

    public Link_Tile(String pId, String pName, String pTile_Column_Id, String pSort, String pDescription) {
        Id = pId;
        Name = pName;
        Description = pDescription;
        Sort = pSort;
        Tile_Column_Id = pTile_Column_Id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTile_Column_Id() {
        return Tile_Column_Id;
    }

    public void setTile_Column_Id(String tile_Column_Id) {
        Tile_Column_Id = tile_Column_Id;
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
