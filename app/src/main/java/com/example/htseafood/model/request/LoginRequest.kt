package com.example.htseafood.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @SerializedName("password") val password: String? = null,

    @SerializedName("user_name") val userName: String? = null
)
