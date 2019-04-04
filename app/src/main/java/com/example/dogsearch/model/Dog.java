package com.example.dogsearch.model;

public class Dog {

    private String breed;
    private String subBreed;
    private byte[] img;
    private String imgLink;

    public Dog(){

    }

    public Dog(String breed, String subBreed, byte[] img, String imgLink) {
        this.breed = breed;
        this.subBreed = subBreed;
        this.img = img;
        this.imgLink = imgLink;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSubBreed() {
        return subBreed;
    }

    public void setSubBreed(String subBreed) {
        this.subBreed = subBreed;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
}


