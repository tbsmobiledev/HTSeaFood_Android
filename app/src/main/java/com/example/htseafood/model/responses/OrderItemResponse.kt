package com.example.htseafood.model.responses

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class OrderItemResponse(

	@SerializedName("unitPrice")
	val unitPrice: Double? = null,

	@SerializedName("no")
	val no: String? = null,

	@SerializedName("baseUnitOfMeasure")
	val baseUnitOfMeasure: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("uPC")
	val uPC: String? = null,

	@SerializedName("itemNo2")
	val itemNo2: String? = null,

	@SerializedName("searchDescription")
	val searchDescription: String? = null
)
{
	fun updatedUnitPrice(): String {
		return "$" + DecimalFormat("0.00").format(unitPrice)
	}
}