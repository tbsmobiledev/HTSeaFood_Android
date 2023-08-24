package com.example.htseafood.model.request

import com.google.gson.annotations.SerializedName

data class InvoiceDetailRequest(

    @SerializedName("invoice_no")
    val invoiceNo: String? = null
)
