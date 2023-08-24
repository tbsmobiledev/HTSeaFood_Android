package com.example.htseafood.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.htseafood.R
import com.example.htseafood.adpter.InvoiceItemListAdapter
import com.example.htseafood.apis.ApiClient
import com.example.htseafood.custom.EqualSpacingItemDecoration
import com.example.htseafood.model.request.InvoiceDetailRequest
import com.example.htseafood.model.responses.InvoiceDetailResponse
import com.example.htseafood.utils.Constants
import com.example.htseafood.utils.ProgressDialog
import com.example.htseafood.utils.Utils
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_invoice_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvoiceDetailActivity : AppCompatActivity() {
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_detail)

        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (intent != null) {
            id = intent.getStringExtra("id").toString()
        }

        rvList.addItemDecoration(
            EqualSpacingItemDecoration(
                resources.getDimension(com.intuit.sdp.R.dimen._10sdp).toInt(),
                EqualSpacingItemDecoration.VERTICAL
            )
        )
        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        detailAPI()
    }


    private fun detailAPI() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL, ""
            )!!.webservices.invoiceDetail(
                InvoiceDetailRequest(
                    id
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@InvoiceDetailActivity,
                                    response.body()!!.get("msg").toString().replace('"', ' ')
                                        .trim(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val detailResponse: InvoiceDetailResponse =
                                    Gson().fromJson(
                                        response.body()!!.getAsJsonObject("data"),
                                        InvoiceDetailResponse::class.java
                                    )
                                llView.visibility= View.VISIBLE
                                tvTitle.text = "Invoice ID: #${detailResponse.no}"
                                tvAddress.text =
                                    detailResponse.sellToAddress + ", " + detailResponse.sellToCity
                                tvOrderdate.text = detailResponse.orderDate
                                tvPostingdate.text = detailResponse.postingDate
                                if (detailResponse.postedSalesInvoiceLines!!.isNotEmpty()) {
                                    val invoiceItemListAdapter = InvoiceItemListAdapter(
                                        this@InvoiceDetailActivity,
                                        detailResponse.postedSalesInvoiceLines
                                    )
                                    rvList.adapter = invoiceItemListAdapter
                                }
                                tvtotalAmount.text = detailResponse.updatedAmount()
                                tvtotalTaxAmount.text = detailResponse.updatedTaxAmount()
                                tvtotalInVatAmount.text = detailResponse.updatedAmountIncludingVAT()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(
                        this@InvoiceDetailActivity,
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