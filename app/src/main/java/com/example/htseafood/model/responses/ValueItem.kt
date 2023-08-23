package com.example.htseafood.model.responses

import android.R.attr.value
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat


data class ValueItem(

    @SerializedName("no")
    val no: String? = null,

    @SerializedName("amount")
    val amount: Double? = null,

    @SerializedName("orderNo")
    val orderNo: String? = null,

    @SerializedName("sellToCustomerName")
    val sellToCustomerName: String? = null,

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
