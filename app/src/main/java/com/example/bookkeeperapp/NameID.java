package com.example.bookkeeperapp;

public class NameID {
    private String name;
    private String ImgUrl;


public  NameID(){

}

public NameID(String name){

    this.name = name;
    this.ImgUrl = ImgUrl;

}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
