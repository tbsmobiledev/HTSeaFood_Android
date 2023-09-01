package com.example.htseafood.model.request

import com.google.gson.annotations.SerializedName

data class AddItemRequest(

    @SerializedName("orderNo")
    val orderNo: String? = null,

    @SerializedName("qty")
    val qty: String? = null,

    @SerializedName("itemNo")
    val itemNo: String? = null
)
