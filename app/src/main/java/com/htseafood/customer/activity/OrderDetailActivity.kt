package com.htseafood.customer.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.htseafood.customer.R
import com.htseafood.customer.adpter.OrderItemListAdapter
import com.htseafood.customer.apis.ApiClient
import com.htseafood.customer.custom.EqualSpacingItemDecoration
import com.htseafood.customer.interfaces.DeleteItemListener
import com.htseafood.customer.interfaces.EditListener
import com.htseafood.customer.model.request.AddOrderRequest
import com.htseafood.customer.model.request.DeleteItemRequest
import com.htseafood.customer.model.request.DeleteOrderRequest
import com.htseafood.customer.model.request.OrderDetailRequest
import com.htseafood.customer.model.request.PDFRequest
import com.htseafood.customer.model.request.UpdateItemRequest
import com.htseafood.customer.model.responses.OrderDetailResponse
import com.htseafood.customer.model.responses.SalesOrderLinesItem
import com.htseafood.customer.utils.Constants
import com.htseafood.customer.utils.ProgressDialog
import com.htseafood.customer.utils.SharedHelper
import com.htseafood.customer.utils.Utils
import kotlinx.android.synthetic.main.activity_order_detail.ivAdd
import kotlinx.android.synthetic.main.activity_order_detail.ivBack
import kotlinx.android.synthetic.main.activity_order_detail.ivDelete
import kotlinx.android.synthetic.main.activity_order_detail.llView
import kotlinx.android.synthetic.main.activity_order_detail.rvList
import kotlinx.android.synthetic.main.activity_order_detail.tvAddress
import kotlinx.android.synthetic.main.activity_order_detail.tvContactPerson
import kotlinx.android.synthetic.main.activity_order_detail.tvOrderdate
import kotlinx.android.synthetic.main.activity_order_detail.tvPDFSend
import kotlinx.android.synthetic.main.activity_order_detail.tvPostingdate
import kotlinx.android.synthetic.main.activity_order_detail.tvShipingdate
import kotlinx.android.synthetic.main.activity_order_detail.tvTitle
import kotlinx.android.synthetic.main.activity_order_detail.tvtotalAmount
import kotlinx.android.synthetic.main.activity_order_detail.tvtotalInVatAmount
import kotlinx.android.synthetic.main.activity_order_detail.tvtotalTaxAmount
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderDetailActivity : AppCompatActivity(), DeleteItemListener, EditListener {
    var id = ""
    var isrefresh = false
    var detailResponse: OrderDetailResponse? = null

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                detailAPI()
            }
        }

    override fun onBackPressed() {
        if (isrefresh) {
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        ivBack.setOnClickListener {
            onBackPressed()
        }

        if (intent != null) {
            id = intent.getStringExtra("id").toString()
            if (id == "create") {
                isrefresh = true
                createOrderAPI()
            } else {
                isrefresh = false
                detailAPI()
            }
        }

        ivDelete.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            val confirmationText = "Are you sure you want to delete this order?"
            val blackMessageSpannable = SpannableString(confirmationText)

            val message =
                "Note: Deleting an order here will permanently delete it from the source ERP as well. Please confirm the deletion."
            val redMessage = SpannableString(message)
            redMessage.setSpan(ForegroundColorSpan(Color.RED), 0, message.length, 0)

            builder.setTitle("Delete Order")
            builder.setMessage(TextUtils.concat(blackMessageSpannable, "\n\n", redMessage))


            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                builder.setCancelable(true)
                deleteOrderAPI()
            }

            //performing negative action
            builder.setNegativeButton("No") { dialogInterface, which ->
                builder.setCancelable(true)
            }

            builder.setCancelable(false)
            builder.show()
        }

        rvList.addItemDecoration(
            EqualSpacingItemDecoration(
                resources.getDimension(com.intuit.sdp.R.dimen._10sdp).toInt(),
                EqualSpacingItemDecoration.VERTICAL
            )
        )
        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        ivAdd.setOnClickListener {
            val intent = Intent(this, AddOrderItemActivity::class.java).putExtra("orderNo", id)
                .putExtra("orderData", Gson().toJson(detailResponse!!.salesOrderLines))
            resultLauncher.launch(intent)
        }

        tvPDFSend.setOnClickListener {
            if (detailResponse!!.amountIncludingVAT != 0.0 && SharedHelper.getKey(
                    this,
                    Constants.CustmerEmail
                ) != ""
            ) {
                if (Utils.isOnline(this)) {
                    ProgressDialog.start(this)
                    ApiClient.getRestClient(
                        Constants.BASE_URL
                    )!!.webservices.sendPDFRequest(
                        PDFRequest(
                            id, SharedHelper.getKey(this, Constants.CustmerEmail)
                        )
                    ).enqueue(object : Callback<JsonObject> {
                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
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
                                        Toast.makeText(
                                            this@OrderDetailActivity,
                                            "The order list PDF has been successfully sent.",
                                            Toast.LENGTH_SHORT
                                        ).show()

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


    }

    private fun deleteOrderAPI() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.deleteOrder(
                DeleteOrderRequest(
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
                                Toast.makeText(
                                    this@OrderDetailActivity,
                                    "The order has been successfully deleted.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val resultIntent = Intent()
                                setResult(RESULT_OK, resultIntent)
                                finish()

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

    private fun createOrderAPI() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
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
                Constants.BASE_URL
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
                                detailResponse =
                                    Gson().fromJson(
                                        response.body()!!.getAsJsonObject("data"),
                                        OrderDetailResponse::class.java
                                    )
                                llView.visibility = View.VISIBLE
                                tvTitle.text = "Order ID: #${detailResponse!!.no}"
                                tvContactPerson.text = detailResponse!!.selltoContact
                                tvAddress.text =
                                    detailResponse!!.sellToAddress + ", " + detailResponse!!.sellToCity
                                tvOrderdate.text = detailResponse!!.orderDate
                                tvPostingdate.text = detailResponse!!.postingDate
                                tvShipingdate.text = detailResponse!!.shipmentDate
                                if (detailResponse!!.salesOrderLines!!.isNotEmpty()) {
                                    val orderItemListAdapter = OrderItemListAdapter(
                                        this@OrderDetailActivity,
                                        detailResponse!!.salesOrderLines,
                                        this@OrderDetailActivity,
                                        this@OrderDetailActivity
                                    )
                                    rvList.adapter = orderItemListAdapter
                                    rvList.visibility = View.VISIBLE
                                } else {
                                    rvList.visibility = View.INVISIBLE
                                }

                                tvtotalAmount.text = detailResponse!!.updatedAmount()
                                tvtotalTaxAmount.text = detailResponse!!.updatedTaxAmount()
                                tvtotalInVatAmount.text =
                                    detailResponse!!.updatedAmountIncludingVAT()

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

    override fun deleteOrderItem(lineNo: String) {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.deleteItem(
                DeleteItemRequest(
                    id, lineNo
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
                                Toast.makeText(
                                    this@OrderDetailActivity,
                                    "The order item has been successfully deleted.",
                                    Toast.LENGTH_SHORT
                                ).show()
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

    override fun editQty(salesOrderLinesItem: SalesOrderLinesItem) {
        val inflater = layoutInflater
        val alertLayout: View = inflater.inflate(R.layout.dialog_update_item_qty, null)

        val tvCancel = alertLayout.findViewById<TextView>(R.id.tvCancel)
        val tvConfirm = alertLayout.findViewById<TextView>(R.id.tvUpdate)

        val tvUPC = alertLayout.findViewById<TextView>(R.id.tvUPC)
        val tvNo = alertLayout.findViewById<TextView>(R.id.tvNo)
        val tvDescription = alertLayout.findViewById<TextView>(R.id.tvDescription)
        val tvUnitofMeasure = alertLayout.findViewById<TextView>(R.id.tvUnitofMeasure)
        val tvUnitPrice = alertLayout.findViewById<TextView>(R.id.tvUnitPrice)
        val evQuantity = alertLayout.findViewById<EditText>(R.id.evQuantity)

        val alert = AlertDialog.Builder(this)
        alert.setView(alertLayout)
        alert.setCancelable(false)

        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        tvUPC.text = salesOrderLinesItem.uPC
        tvNo.text = salesOrderLinesItem.itemNo2
        tvDescription.text = salesOrderLinesItem.description
        tvUnitofMeasure.text = salesOrderLinesItem.unitOfMeasure
        tvUnitPrice.text = salesOrderLinesItem.updatedUnitPrice()
        evQuantity.setText(salesOrderLinesItem.quantity.toString())
        evQuantity.requestFocus()

        Handler(Looper.getMainLooper()).postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(evQuantity, InputMethodManager.SHOW_IMPLICIT)

        }, 500)


        tvCancel.setOnClickListener { view: View? -> dialog.dismiss() }
        tvConfirm.setOnClickListener { view: View? ->
            if (evQuantity.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
            } else {
                updateOrder(salesOrderLinesItem.lineNo, evQuantity.text.toString())
                dialog.dismiss()
            }


        }

    }

    private fun updateOrder(lineNo: Int?, qty: String) {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.updateItemQty(
                UpdateItemRequest(
                    id, lineNo.toString(), qty
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
}