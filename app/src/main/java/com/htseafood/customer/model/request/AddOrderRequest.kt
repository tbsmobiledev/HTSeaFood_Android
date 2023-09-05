package com.htseafood.customer.model.request

import com.google.gson.annotations.SerializedName

data class AddOrderRequest(

    @SerializedName("customerNo")
    val customerNo: String? = null
)
