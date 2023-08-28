package com.example.htseafood.model.request

import com.google.gson.annotations.SerializedName

data class DeleteItemRequest(

    @SerializedName("orderNo")
    val orderNo: String? = null,
    @SerializedName("lineNo")
    val lineNo: String? = null
)
