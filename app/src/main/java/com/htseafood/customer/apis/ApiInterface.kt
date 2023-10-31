package com.htseafood.customer.apis

import com.google.gson.JsonObject
import com.htseafood.customer.model.request.AddItemRequest
import com.htseafood.customer.model.request.AddOrderRequest
import com.htseafood.customer.model.request.DeleteItemRequest
import com.htseafood.customer.model.request.DeleteOrderRequest
import com.htseafood.customer.model.request.InvoiceDetailRequest
import com.htseafood.customer.model.request.ListRequest
import com.htseafood.customer.model.request.LoginRequest
import com.htseafood.customer.model.request.OrderDetailRequest
import com.htseafood.customer.model.request.PDFRequest
import com.htseafood.customer.model.request.SearchOrderRequest
import com.htseafood.customer.model.request.ShipmentDetailRequest
import com.htseafood.customer.model.request.UpdateItemRequest
import com.htseafood.customer.utils.Constants
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

    @POST(Constants.ORDER_DETAIL)
    fun orderDetail(@Body orderDetailRequest: OrderDetailRequest): Call<JsonObject>

    @POST(Constants.CREATE_ORDER)
    fun createOrder(@Body addOrderRequest: AddOrderRequest): Call<JsonObject>

    @POST(Constants.DELETE_ORDER)
    fun deleteOrder(@Body deleteOrderRequest: DeleteOrderRequest): Call<JsonObject>

    @POST(Constants.DELETE_ITEM)
    fun deleteItem(@Body deleteItemRequest: DeleteItemRequest): Call<JsonObject>

    @POST(Constants.SEARCH_UPC)
    fun searchUPC(@Body searchOrderRequest: SearchOrderRequest): Call<JsonObject>

    @POST(Constants.SEARCH_ITEMNO)
    fun searchItemNo(@Body searchOrderRequest: SearchOrderRequest): Call<JsonObject>

    @POST(Constants.ADD_ITEM)
    fun addItem(@Body addItemRequest: AddItemRequest): Call<JsonObject>

    @POST(Constants.UPDATE_ITEM)
    fun updateItemQty(@Body updateItemRequest: UpdateItemRequest): Call<JsonObject>

    @POST(Constants.SEND_ORDER_PDF)
    fun sendPDFRequest(@Body pdfRequest: PDFRequest): Call<JsonObject>
}