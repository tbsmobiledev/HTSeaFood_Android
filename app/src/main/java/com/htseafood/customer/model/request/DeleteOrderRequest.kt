package com.htseafood.customer.model.request

import com.google.gson.annotations.SerializedName

data class DeleteOrderRequest(

    @SerializedName("orderNo")
    val orderNo: String? = null
)
