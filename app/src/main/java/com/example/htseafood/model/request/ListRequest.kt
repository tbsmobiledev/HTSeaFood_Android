package com.example.htseafood.model.request

import com.google.gson.annotations.SerializedName

data class ListRequest(

	@SerializedName("per_page")
	val perPage: Int? = null,

	@SerializedName("customer_no")
	val customerNo: String? = null,

	@SerializedName("page_no")
	val pageNo: Int? = null
)
