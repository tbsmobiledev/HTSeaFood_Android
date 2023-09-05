package com.htseafood.customer.model.responses

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class InvoiceDetailResponse(

    @SerializedName("no")
    val no: String? = null,

    @SerializedName("amount")
    val amount: Double? = null,

    @SerializedName("orderNo")
    val orderNo: String? = null,

    @SerializedName("sellToCustomerName")
    val sellToCustomerName: String? = null,

    @SerializedName("@odata.etag")
    val odataEtag: String? = null,

    @SerializedName("sellToCity")
    val sellToCity: String? = null,

    @SerializedName("postingDate")
    val postingDate: String? = null,

    @SerializedName("sellToAddress")
    val sellToAddress: String? = null,

    @SerializedName("postedSalesInvoiceLines")
    val postedSalesInvoiceLines: ArrayList<PostedSalesInvoiceLinesItem>? = null,

    @SerializedName("sellToCustomerNo")
    val sellToCustomerNo: String? = null,

    @SerializedName("taxAmount")
    val taxAmount: Double? = null,

    @SerializedName("orderDate")
    val orderDate: String? = null,

    @SerializedName("amountIncludingVAT")
    val amountIncludingVAT: Double? = null
) {

    fun updatedAmount(): String {
        return "$" + DecimalFormat("0.00").format(amount)
    }

    fun updatedTaxAmount(): String {
        return "$" + DecimalFormat("0.00").format(taxAmount)
    }

    fun updatedAmountIncludingVAT(): String {
        return "$" + DecimalFormat("0.00").format(amountIncludingVAT)
    }
}

data class PostedSalesInvoiceLinesItem(

    @SerializedName("unitPrice")
    val unitPrice: Double? = null,

    @SerializedName("no")
    val no: String? = null,

    @SerializedName("amount")
    val amount: Double? = null,

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

    @SerializedName("documentNo")
    val documentNo: String? = null,

    @SerializedName("lineNo")
    val lineNo: Int? = null,

    @SerializedName("locationCode")
    val locationCode: String? = null,

    @SerializedName("taxAmount")
    val taxAmount: Double? = null,

    @SerializedName("amountIncludingVAT")
    val amountIncludingVAT: Double? = null
) {

    fun updatedAmount(): String {
        return "$" + DecimalFormat("0.00").format(amount)
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
