package com.example.htseafood.model.request

import com.google.gson.annotations.SerializedName

data class OrderDetailRequest(

    @SerializedName("order_no")
    val order_no: String? = null
)
