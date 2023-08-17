package com.example.htseafood.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("emailaddress")
    val emailaddress: String? = null,
    @SerializedName("password")
    val password: String? = null,
)
