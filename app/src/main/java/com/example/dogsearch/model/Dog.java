package com.example.dogsearch.model;

public class Dog {

    //private variables
    int id;
    String breed;
    String subBreed;
    byte[] img;

    // Empty constructor
    public Dog(){

    }

    // constructor
    public Dog(int id, String breed, String subBreed, byte[] img) {
        this.id = id;
        this.breed = breed;
        this.subBreed = subBreed;
        this.img = img;
    }

    // constructor
    public Dog(String breed, byte[] img){

        this.breed = breed;
        this.img = img;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}

