package com.htseafood.customer.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @SerializedName("password") val password: String? = null,

    @SerializedName("user_name") val userName: String? = null
)
