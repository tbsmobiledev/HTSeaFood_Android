package com.htseafood.customer.model.request

import com.google.gson.annotations.SerializedName

data class SearchOrderRequest(

    @SerializedName("description")
    val description: String? = null
)
