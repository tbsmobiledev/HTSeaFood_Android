package com.example.htseafood.activity

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
import com.example.htseafood.R
import com.example.htseafood.adpter.ItemListAdapter
import com.example.htseafood.apis.ApiClient
import com.example.htseafood.model.request.SearchOrderRequest
import com.example.htseafood.model.responses.OrderItemResponse
import com.example.htseafood.utils.Constants
import com.example.htseafood.utils.ProgressDialog
import com.example.htseafood.utils.Utils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_add_order_item.et_barcode
import kotlinx.android.synthetic.main.activity_add_order_item.evQuantity
import kotlinx.android.synthetic.main.activity_add_order_item.iv_clean
import kotlinx.android.synthetic.main.activity_add_order_item.tvAdd
import kotlinx.android.synthetic.main.activity_add_order_item.tvDescription
import kotlinx.android.synthetic.main.activity_add_order_item.tvNo
import kotlinx.android.synthetic.main.activity_add_order_item.tvSearch
import kotlinx.android.synthetic.main.activity_add_order_item.tvUPC
import kotlinx.android.synthetic.main.activity_add_order_item.tvUnitPrice
import kotlinx.android.synthetic.main.activity_add_order_item.tvUnitofMeasure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOrderItemActivity : AppCompatActivity() {
    private var itemArrayList = ArrayList<OrderItemResponse>()
    private val getArray: TypeToken<ArrayList<OrderItemResponse?>?> =
        object : TypeToken<ArrayList<OrderItemResponse?>?>() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order_item)

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

        et_barcode.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (et_barcode.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter the UPC or Item No", Toast.LENGTH_SHORT).show()
            } else {
                searchAPI()
            }
            Utils.hideSoftKeyboard(this, et_barcode)
            true
        }

        tvSearch.setOnClickListener {
            if (et_barcode.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter the UPC or Item No", Toast.LENGTH_SHORT).show()
            } else {
                searchAPI()
            }
            Utils.hideSoftKeyboard(this, et_barcode)
        }

    }

    private fun searchAPI() {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL, ""
            )!!.webservices.searchItem(
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
                                            "Please enter correct UPC or Item No",
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