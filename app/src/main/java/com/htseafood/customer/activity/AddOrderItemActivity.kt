package com.htseafood.customer.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htseafood.customer.R
import com.htseafood.customer.adpter.ItemListAdapter
import com.htseafood.customer.apis.ApiClient
import com.htseafood.customer.model.request.AddItemRequest
import com.htseafood.customer.model.request.SearchOrderRequest
import com.htseafood.customer.model.responses.OrderItemResponse
import com.htseafood.customer.utils.Constants
import com.htseafood.customer.utils.ProgressDialog
import com.htseafood.customer.utils.Utils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_add_order_item.et_barcode
import kotlinx.android.synthetic.main.activity_add_order_item.evQuantity
import kotlinx.android.synthetic.main.activity_add_order_item.iv_clean
import kotlinx.android.synthetic.main.activity_add_order_item.tvAdd
import kotlinx.android.synthetic.main.activity_add_order_item.tvDescription
import kotlinx.android.synthetic.main.activity_add_order_item.tvNo
import kotlinx.android.synthetic.main.activity_add_order_item.tvSearchNo
import kotlinx.android.synthetic.main.activity_add_order_item.tvSearchUPC
import kotlinx.android.synthetic.main.activity_add_order_item.tvUPC
import kotlinx.android.synthetic.main.activity_add_order_item.tvUnitPrice
import kotlinx.android.synthetic.main.activity_add_order_item.tvUnitofMeasure
import kotlinx.android.synthetic.main.activity_add_order_item.ivBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOrderItemActivity : AppCompatActivity() {
    private var itemArrayList = ArrayList<OrderItemResponse>()
    private val getArray: TypeToken<ArrayList<OrderItemResponse?>?> =
        object : TypeToken<ArrayList<OrderItemResponse?>?>() {}
    var orderNo = ""
    var selectedItemNo = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order_item)

        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        if (intent != null) {
            orderNo = intent.getStringExtra("orderNo").toString()
        }
        et_barcode.requestFocus()

        et_barcode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (text.isEmpty()) {
                    iv_clean.visibility = View.INVISIBLE
                } else {
                    iv_clean.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        tvSearchUPC.setOnClickListener {
            if (et_barcode.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter the UPC", Toast.LENGTH_SHORT).show()
            } else {
                searchUPCAPI()
            }
            Utils.hideSoftKeyboard(this, et_barcode)
        }

        tvSearchNo.setOnClickListener {
            if (et_barcode.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Item No", Toast.LENGTH_SHORT).show()
            } else {
                searchItemNoAPI()
            }
            Utils.hideSoftKeyboard(this, et_barcode)
        }

        tvAdd.setOnClickListener {
            if (evQuantity.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
            } else {
                addQuantity()
            }
            Utils.hideSoftKeyboard(this, et_barcode)
        }

    }

    private fun addQuantity() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.addItem(
                AddItemRequest(
                    orderNo, evQuantity.text.toString(),
                    selectedItemNo
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@AddOrderItemActivity,
                                    "API Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                emptyData()
                            } else {
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
                    emptyData()
                    Toast.makeText(
                        this@AddOrderItemActivity,
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

    private fun searchItemNoAPI() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.searchItemNo(
                SearchOrderRequest(
                    et_barcode.text.toString().trim()
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@AddOrderItemActivity,
                                    "API Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                emptyData()
                            } else {
                                itemArrayList =
                                    Gson().fromJson(
                                        response.body()!!.getAsJsonObject("data")
                                            .getAsJsonArray("value"),
                                        getArray.type
                                    )
                                when (itemArrayList.size) {
                                    0 -> {
                                        emptyData()
                                        Toast.makeText(
                                            this@AddOrderItemActivity,
                                            "Please enter correct Item No",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    1 -> {
                                        setData(itemArrayList[0])
                                    }

                                    else -> {
                                        openDialog(itemArrayList)
                                    }
                                }


                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    emptyData()
                    Toast.makeText(
                        this@AddOrderItemActivity,
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

    private fun searchUPCAPI() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.searchUPC(
                SearchOrderRequest(
                    et_barcode.text.toString().trim()
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@AddOrderItemActivity,
                                    "API Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                emptyData()
                            } else {
                                itemArrayList =
                                    Gson().fromJson(
                                        response.body()!!.getAsJsonObject("data")
                                            .getAsJsonArray("value"),
                                        getArray.type
                                    )
                                when (itemArrayList.size) {
                                    0 -> {
                                        emptyData()
                                        Toast.makeText(
                                            this@AddOrderItemActivity,
                                            "Please enter correct UPC",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    1 -> {
                                        setData(itemArrayList[0])
                                    }

                                    else -> {
                                        openDialog(itemArrayList)
                                    }
                                }


                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    emptyData()
                    Toast.makeText(
                        this@AddOrderItemActivity,
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

    private fun openDialog(list: ArrayList<OrderItemResponse>?) {
        val inflater = layoutInflater
        val alertLayout: View = inflater.inflate(R.layout.dialog_product_selection, null)

        val productSeletionsAdapter = ItemListAdapter(this, list)

        val lLayout = LinearLayoutManager(this)
        val rv_productList = alertLayout.findViewById<RecyclerView>(R.id.rv_productList)
        rv_productList.layoutManager = lLayout
        rv_productList.adapter = productSeletionsAdapter

        val tv_cancel = alertLayout.findViewById<TextView>(R.id.tv_cancel)
        val tv_confirm = alertLayout.findViewById<TextView>(R.id.tv_confirm)

        val alert = AlertDialog.Builder(this)
        alert.setView(alertLayout)
        alert.setCancelable(false)

        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        tv_cancel.setOnClickListener { view: View? -> dialog.dismiss() }
        tv_confirm.setOnClickListener { view: View? ->
            val productBean: OrderItemResponse = productSeletionsAdapter.getSelected()
            setData(productBean)
            dialog.dismiss()
        }

    }

    private fun setData(item: OrderItemResponse) {
        et_barcode.setText("")
        iv_clean.visibility = View.INVISIBLE
        tvUPC.text = item.uPC
        tvNo.text = item.itemNo2
        tvDescription.text = item.description
        tvUnitofMeasure.text = item.baseUnitOfMeasure
        tvUnitPrice.text = item.updatedUnitPrice()
        evQuantity.setText("")
        evQuantity.isEnabled = true
        tvAdd.visibility = View.VISIBLE
        evQuantity.requestFocus()
        selectedItemNo = item.no.toString()
    }

    private fun emptyData() {
        et_barcode.requestFocus()
        et_barcode.setText("")
        iv_clean.visibility = View.INVISIBLE
        tvUPC.text = ""
        tvNo.text = ""
        tvDescription.text = ""
        tvUnitofMeasure.text = ""
        tvUnitPrice.text = ""
        evQuantity.setText("")
        evQuantity.isEnabled = false
        tvAdd.visibility = View.GONE

    }
}