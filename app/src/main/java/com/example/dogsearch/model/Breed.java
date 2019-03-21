package com.example.dogsearch.model;

import java.util.List;

public class Breed {
    private String breed;
    private List<String> subBreed;

    public Breed(String breed, List<String> subBreed) {
        this.breed = breed;
        this.subBreed = subBreed;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public List<String> getSubBreed() {
        return subBreed;
    }

    public void setSubBreed(List<String> subBreed) {
        this.subBreed = subBreed;
    }
}