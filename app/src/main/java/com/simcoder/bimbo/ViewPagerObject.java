package com.simcoder.bimbo;


public class ViewPagerObject {
    private String title;
    private String image;
    private String desc;


    public ViewPagerObject(String title, String image, String desc){
        this.title = title;
        this.image = image;
        this.desc = desc;
    }

    public String getTitle(){return title;}
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage(){return image;}
    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc(){return desc;}
    public void setDesc(String desc) {
        this.desc = image;
    }
}
