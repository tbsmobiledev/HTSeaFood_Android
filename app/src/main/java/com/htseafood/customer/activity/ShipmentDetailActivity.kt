package com.htseafood.customer.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.htseafood.customer.R
import com.htseafood.customer.adpter.ShipmentItemListAdapter
import com.htseafood.customer.apis.ApiClient
import com.htseafood.customer.custom.BaseActivity
import com.htseafood.customer.custom.EqualSpacingItemDecoration
import com.htseafood.customer.databinding.ActivityShipmentDetailBinding
import com.htseafood.customer.model.request.ShipmentDetailRequest
import com.htseafood.customer.model.responses.ShipmentDetailResponse
import com.htseafood.customer.utils.Constants
import com.htseafood.customer.utils.ProgressDialog
import com.htseafood.customer.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShipmentDetailActivity : BaseActivity<ActivityShipmentDetailBinding>() {
    var id = ""
    override fun inflateBinding(): ActivityShipmentDetailBinding {
        return ActivityShipmentDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (intent != null) {
            id = intent.getStringExtra("id").toString()
        }


        binding.rvList.addItemDecoration(
            EqualSpacingItemDecoration(
                resources.getDimension(com.intuit.sdp.R.dimen._10sdp).toInt(),
                EqualSpacingItemDecoration.VERTICAL
            )
        )
        binding.rvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        detailAPI()


    }

    private fun detailAPI() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.shipmentDetail(
                ShipmentDetailRequest(
                    id
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@ShipmentDetailActivity,
                                    response.body()!!.get("msg").toString().replace('"', ' ')
                                        .trim(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val detailResponse: ShipmentDetailResponse =
                                    Gson().fromJson(
                                        response.body()!!.getAsJsonObject("data"),
                                        ShipmentDetailResponse::class.java
                                    )
                                binding.llView.visibility = View.VISIBLE
                                binding.tvTitle.text = "Shipment ID: #${detailResponse.no}"
                                binding.tvAddress.text =
                                    detailResponse.sellToAddress + ", " + detailResponse.sellToCity
                                binding.tvOrderdate.text = detailResponse.orderDate
                                binding.tvPostingdate.text = detailResponse.postingDate
                                if (detailResponse.postedSalesShipmentLines!!.isNotEmpty()) {
                                    val shipmentItemListAdapter = ShipmentItemListAdapter(
                                        this@ShipmentDetailActivity,
                                        detailResponse.postedSalesShipmentLines!!
                                    )
                                    binding.rvList.adapter = shipmentItemListAdapter
                                }

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(
                        this@ShipmentDetailActivity,
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