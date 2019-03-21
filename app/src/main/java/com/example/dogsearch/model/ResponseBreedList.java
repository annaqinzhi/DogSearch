package com.example.dogsearch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseBreedList implements Serializable {
        @SerializedName("status")
        private String status;
        @SerializedName("message")
        private List<Breed> message;

    public ResponseBreedList(String status, List<Breed> message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Breed> getMessage() {
        return message;
    }

    public void setMessage(List<Breed> message) {
        this.message = message;
    }
}
