package com.example.dogsearch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseBreed implements Serializable {
        @SerializedName("status")
        private String status;
        @SerializedName("message")
        private List<String> message;

    public ResponseBreed(String status, List<String> message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
