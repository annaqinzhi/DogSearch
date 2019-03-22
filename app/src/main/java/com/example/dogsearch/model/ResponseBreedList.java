package com.example.dogsearch.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class ResponseBreedList implements Serializable {
        @SerializedName("status")
        private String status;
        @SerializedName("message")
        private Object message;

    public ResponseBreedList(String status, Object message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
