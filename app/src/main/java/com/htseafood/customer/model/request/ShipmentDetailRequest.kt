package com.htseafood.customer.model.request

import com.google.gson.annotations.SerializedName

data class ShipmentDetailRequest(

    @SerializedName("shipment_no")
    val shipment_no: String? = null
)
