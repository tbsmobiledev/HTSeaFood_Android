package com.example.htseafood.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.htseafood.R
import com.example.htseafood.apis.ApiClient
import com.example.htseafood.model.request.AddOrderRequest
import com.example.htseafood.model.request.OrderDetailRequest
import com.example.htseafood.model.responses.OrderDetailResponse
import com.example.htseafood.utils.Constants
import com.example.htseafood.utils.ProgressDialog
import com.example.htseafood.utils.SharedHelper
import com.example.htseafood.utils.Utils
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_order_detail.ivBack
import kotlinx.android.synthetic.main.activity_order_detail.llView
import kotlinx.android.synthetic.main.activity_order_detail.tvAddress
import kotlinx.android.synthetic.main.activity_order_detail.tvContactPerson
import kotlinx.android.synthetic.main.activity_order_detail.tvOrderdate
import kotlinx.android.synthetic.main.activity_order_detail.tvPostingdate
import kotlinx.android.synthetic.main.activity_order_detail.tvShipingdate
import kotlinx.android.synthetic.main.activity_order_detail.tvTitle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailActivity : AppCompatActivity() {
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (intent != null) {
            id = intent.getStringExtra("id").toString()
            if (id == "create") {
                createOrderAPI()
            } else {
                detailAPI()
            }
        }

//        rvList.addItemDecoration(
//            EqualSpacingItemDecoration(
//                resources.getDimension(com.intuit.sdp.R.dimen._10sdp).toInt(),
//                EqualSpacingItemDecoration.VERTICAL
//            )
//        )
//        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }

    private fun createOrderAPI() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL, ""
            )!!.webservices.createOrder(
                AddOrderRequest(
                    SharedHelper.getKey(this, Constants.CustmerNo)
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@OrderDetailActivity,
                                    "API Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                id = response.body()!!.getAsJsonObject("data").get("orderNo")
                                    .toString().replace('"', ' ').trim()
                                detailAPI()

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(
                        this@OrderDetailActivity,
                        getString(R.string.api_fail_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    ProgressDialog.dismiss()
                }
            })


        } else {
            Toast.makeText(
                this,
                getString(R.string.please_check_your_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun detailAPI() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL, ""
            )!!.webservices.orderDetail(
                OrderDetailRequest(
                    id
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@OrderDetailActivity,
                                    response.body()!!.get("msg").toString().replace('"', ' ')
                                        .trim(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val detailResponse: OrderDetailResponse =
                                    Gson().fromJson(
                                        response.body()!!.getAsJsonObject("data"),
                                        OrderDetailResponse::class.java
                                    )
                                llView.visibility = View.VISIBLE
                                tvTitle.text = "Order ID: #${detailResponse.no}"
                                tvContactPerson.text = detailResponse.selltoContact
                                tvAddress.text =
                                    detailResponse.sellToAddress + ", " + detailResponse.sellToCity
                                tvOrderdate.text = detailResponse.orderDate
                                tvPostingdate.text = detailResponse.postingDate
                                tvShipingdate.text = detailResponse.shipmentDate
//                                if (detailResponse.postedSalesInvoiceLines!!.isNotEmpty()) {
//                                    val invoiceItemListAdapter = InvoiceItemListAdapter(
//                                        this@InvoiceDetailActivity,
//                                        detailResponse.postedSalesInvoiceLines
//                                    )
//                                    rvList.adapter = invoiceItemListAdapter
//                                }

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(
                        this@OrderDetailActivity,
                        getString(R.string.api_fail_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    ProgressDialog.dismiss()
                }
            })


        } else {
            Toast.makeText(
                this,
                getString(R.string.please_check_your_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}