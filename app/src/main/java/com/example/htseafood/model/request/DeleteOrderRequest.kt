package com.example.htseafood.model.request

import com.google.gson.annotations.SerializedName

data class DeleteOrderRequest(

    @SerializedName("orderNo")
    val orderNo: String? = null
)
