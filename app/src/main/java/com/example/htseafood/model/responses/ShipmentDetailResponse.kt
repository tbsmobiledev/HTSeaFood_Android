package com.example.htseafood.model.responses

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class ShipmentDetailResponse(

    @SerializedName("no")
    val no: String? = null,

    @SerializedName("sellToCustomerName")
    val sellToCustomerName: String? = null,

    @SerializedName("postedSalesShipmentLines")
    val postedSalesShipmentLines: ArrayList<PostedSalesShipmentLinesItem>? = null,

    @SerializedName("sellToAddress")
    val sellToAddress: String? = null,

    @SerializedName("@odata.etag")
    val odataEtag: String? = null,

    @SerializedName("sellToCity")
    val sellToCity: String? = null,

    @SerializedName("sellToCustomerNo")
    val sellToCustomerNo: String? = null,

    @SerializedName("postingDate")
    val postingDate: String? = null,

    @SerializedName("orderDate")
    val orderDate: String? = null
)

data class PostedSalesShipmentLinesItem(

    @SerializedName("unitPrice")
    val unitPrice: Double? = null,

    @SerializedName("documentNo")
    val documentNo: String? = null,

    @SerializedName("no")
    val no: String? = null,

    @SerializedName("lineNo")
    val lineNo: Int? = null,

    @SerializedName("quantity")
    val quantity: Int? = null,

    @SerializedName("unitOfMeasure")
    val unitOfMeasure: String? = null,

    @SerializedName("@odata.etag")
    val odataEtag: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("locationCode")
    val locationCode: String? = null
) {
    fun updatedUnitPrice(): String {
        return "$" + DecimalFormat("0.00").format(unitPrice)
    }
}
