package com.example.htseafood.model.request

import com.google.gson.annotations.SerializedName

data class InvoiceRequest(

	@SerializedName("per_page")
	val perPage: Int? = null,

	@SerializedName("customer_no")
	val customerNo: String? = null,

	@SerializedName("page_no")
	val pageNo: Int? = null
)
