package com.example.htseafood.apis

import com.example.htseafood.model.request.InvoiceDetailRequest
import com.example.htseafood.model.request.ListRequest
import com.example.htseafood.model.request.LoginRequest
import com.example.htseafood.model.request.ShipmentDetailRequest
import com.example.htseafood.utils.Constants
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @POST(Constants.SIGN_IN)
    fun login(@Body loginRequest: LoginRequest): Call<JsonObject>

    @POST(Constants.INVOICE_LIST)
    fun invoiceList(@Body listRequest: ListRequest): Call<JsonObject>

    @POST(Constants.SHIPMENT_LIST)
    fun shipmentList(@Body listRequest: ListRequest): Call<JsonObject>

    @POST(Constants.ORDER_LIST)
    fun orderList(@Body listRequest: ListRequest): Call<JsonObject>

    @POST(Constants.INVOICE_DETAIL)
    fun invoiceDetail(@Body invoiceDetailRequest: InvoiceDetailRequest): Call<JsonObject>

    @POST(Constants.SHIPMENT_DETAIL)
    fun shipmentDetail(@Body shipmentDetailRequest: ShipmentDetailRequest): Call<JsonObject>
}