package com.example.dogsearch.model

import com.google.gson.annotations.SerializedName

import org.json.JSONObject

import java.io.Serializable

class ResponseBreedList(@field:SerializedName("status")
                        var status: String, @field:SerializedName("message")
                        var message: Any) : Serializable
