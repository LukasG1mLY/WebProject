package com.example.webproject.Listen;

public class Link {
    String Id;
    String Linktext;
    String Link_grp_id;
    String Sort;
    String Description;
    String Url_active;
    String Url_inactive;
    String Active;
    String Auth_level;
    String Newtab;


    public Link(String pId,
                String pLinktext,
                String pLink_grp_id,
                String pSort,
                String pDescription,
                String pUrl_active,
                String pUrl_inactive,
                String pActive,
                String pAuth_level,
                String pNewtab) {

        Id = pId;
        Linktext = pLinktext;
        Link_grp_id = pLink_grp_id;
        Sort = pSort;
        Description = pDescription;
        Url_active = pUrl_active;
        Url_inactive= pUrl_inactive;
        Active = pActive;
        Auth_level = pAuth_level;
        Newtab = pNewtab;
    }

    public int getId() {return Integer.parseInt(Id);}

    public String getLinktext() {return Linktext;}
    public String getLink_grp_id() {return Link_grp_id;}

    public String getSort() {return Sort;}

    public String getDescription() {return Description;}
    public String getUrl_active() {return Url_active;}
    public String getUrl_inactive() {return Url_inactive;}

    public String getActive() {return Active;}
    public String getAuth_level() {return Auth_level;}
    public String getNewtab() {return Newtab;}
}


