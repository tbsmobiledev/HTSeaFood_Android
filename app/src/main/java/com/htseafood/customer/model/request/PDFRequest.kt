package com.htseafood.customer.model.request

import com.google.gson.annotations.SerializedName

data class PDFRequest(
    @SerializedName("orderNo") val orderNo: String? = null,
    @SerializedName("email") val email: String? = null,
)
