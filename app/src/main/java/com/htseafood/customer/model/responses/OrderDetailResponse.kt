package com.htseafood.customer.model.responses

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class OrderDetailResponse(

    @SerializedName("no")
    val no: String? = null,

    @SerializedName("selltoCountryRegionCode")
    val selltoCountryRegionCode: String? = null,

    @SerializedName("documentType")
    val documentType: String? = null,

    @SerializedName("sellToCustomerName")
    val sellToCustomerName: String? = null,

    @SerializedName("@odata.etag")
    val odataEtag: String? = null,

    @SerializedName("sellToCity")
    val sellToCity: String? = null,

    @SerializedName("postingDate")
    val postingDate: String? = null,

    @SerializedName("selltoContact")
    val selltoContact: String? = null,

    @SerializedName("selltoCounty")
    val selltoCounty: String? = null,

    @SerializedName("totalamount")
    val totalamount: Double? = null,

    @SerializedName("sellToAddress")
    val sellToAddress: String? = null,

    @SerializedName("salesOrderLines")
    val salesOrderLines: ArrayList<SalesOrderLinesItem>? = null,

    @SerializedName("sellToCustomerNo")
    val sellToCustomerNo: String? = null,

    @SerializedName("shipmentDate")
    val shipmentDate: String? = null,

    @SerializedName("taxAmount")
    val taxAmount: Double? = null,

    @SerializedName("orderDate")
    val orderDate: String? = null,

    @SerializedName("amountIncludingVAT")
    val amountIncludingVAT: Double? = null
){

    fun updatedAmount(): String {
        return "$" + DecimalFormat("0.00").format(totalamount)
    }

    fun updatedTaxAmount(): String {
        return "$" + DecimalFormat("0.00").format(taxAmount)
    }

    fun updatedAmountIncludingVAT(): String {
        return "$" + DecimalFormat("0.00").format(amountIncludingVAT)
    }
}


data class SalesOrderLinesItem(

    @SerializedName("unitPrice")
    val unitPrice: Double? = null,

    @SerializedName("no")
    val no: String? = null,

    @SerializedName("quantity")
    var quantity: Int? = null,

    @SerializedName("qty")
    val qty: Int? = null,

    @SerializedName("unitOfMeasure")
    val unitOfMeasure: String? = null,

    @SerializedName("documentType")
    val documentType: String? = null,

    @SerializedName("@odata.etag")
    val odataEtag: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("uPC")
    val uPC: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("documentNo")
    val documentNo: String? = null,

    @SerializedName("lineNo")
    val lineNo: Int? = null,

    @SerializedName("totalamount")
    val totalamount: Double? = null,

    @SerializedName("itemNo2")
    val itemNo2: String? = null,

    @SerializedName("taxAmount")
    val taxAmount: Double? = null,

    @SerializedName("amountIncludingVAT")
    val amountIncludingVAT: Double? = null
) {

    fun updatedAmount(): String {
        return "$" + DecimalFormat("0.00").format(totalamount)
    }

    fun updatedTaxAmount(): String {
        return "$" + DecimalFormat("0.00").format(taxAmount)
    }

    fun updatedAmountIncludingVAT(): String {
        return "$" + DecimalFormat("0.00").format(amountIncludingVAT)
    }

    fun updatedUnitPrice(): String {
        return "$" + DecimalFormat("0.00").format(unitPrice)
    }
}
