package com.htseafood.customer.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.htseafood.customer.R
import com.htseafood.customer.adpter.ItemListAdapter
import com.htseafood.customer.apis.ApiClient
import com.htseafood.customer.model.request.AddItemRequest
import com.htseafood.customer.model.request.DeleteItemRequest
import com.htseafood.customer.model.request.SearchOrderRequest
import com.htseafood.customer.model.request.UpdateItemRequest
import com.htseafood.customer.model.responses.OrderItemResponse
import com.htseafood.customer.model.responses.SalesOrderLinesItem
import com.htseafood.customer.utils.Constants
import com.htseafood.customer.utils.ProgressDialog
import com.htseafood.customer.utils.Utils
import kotlinx.android.synthetic.main.activity_add_order_item.et_barcode
import kotlinx.android.synthetic.main.activity_add_order_item.evQuantity
import kotlinx.android.synthetic.main.activity_add_order_item.ivBack
import kotlinx.android.synthetic.main.activity_add_order_item.iv_clean
import kotlinx.android.synthetic.main.activity_add_order_item.llEditDelete
import kotlinx.android.synthetic.main.activity_add_order_item.tvAdd
import kotlinx.android.synthetic.main.activity_add_order_item.tvDelete
import kotlinx.android.synthetic.main.activity_add_order_item.tvDescription
import kotlinx.android.synthetic.main.activity_add_order_item.tvEdit
import kotlinx.android.synthetic.main.activity_add_order_item.tvNo
import kotlinx.android.synthetic.main.activity_add_order_item.tvSearchNo
import kotlinx.android.synthetic.main.activity_add_order_item.tvUPC
import kotlinx.android.synthetic.main.activity_add_order_item.tvUnitPrice
import kotlinx.android.synthetic.main.activity_add_order_item.tvUnitofMeasure
import kotlinx.android.synthetic.main.activity_home.ivLogout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class AddOrderItemActivity : AppCompatActivity() {
    private var itemArrayList = ArrayList<OrderItemResponse>()
    private val getArray: TypeToken<ArrayList<OrderItemResponse?>?> =
        object : TypeToken<ArrayList<OrderItemResponse?>?>() {}
    private val getOrder: TypeToken<ArrayList<SalesOrderLinesItem?>?> =
        object : TypeToken<ArrayList<SalesOrderLinesItem?>?>() {}
    private var orderArrayList = ArrayList<SalesOrderLinesItem>()
    var foundItem: SalesOrderLinesItem? = null
    var orderNo = ""
    var selectedItemNo = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order_item)

        ivBack.setOnClickListener {
            onBackPressed()
        }
        if (intent != null) {
            orderNo = intent.getStringExtra("orderNo").toString()
            orderArrayList =
                Gson().fromJson(intent.getStringExtra("orderData").toString(), getOrder.type)
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

            override fun afterTextChanged(s: Editable) {

            }
        })

        et_barcode.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                // Code to be executed when the Enter key is pressed
                searchItemNoAPI()
                Utils.hideSoftKeyboard(this@AddOrderItemActivity, et_barcode)
                return@setOnEditorActionListener true
            }
            false
        }

        iv_clean.setOnClickListener {
            et_barcode.setText("")
        }

        tvSearchNo.setOnClickListener {

            if (et_barcode.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter UPC or Item No", Toast.LENGTH_SHORT).show()
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

        tvEdit.setOnClickListener {
            if (evQuantity.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
            } else {
                updateOrder(foundItem!!.lineNo, evQuantity.text.toString())
            }

        }

        tvDelete.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Delete Order Item")
            builder.setMessage("Are you sure you want to delete this order item?")

            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                builder.setCancelable(true)
                deleteOrderItem(foundItem!!.lineNo.toString())
            }

            //performing negative action
            builder.setNegativeButton("No") { dialogInterface, which ->
                builder.setCancelable(true)
            }

            builder.setCancelable(false)
            builder.show()
        }

    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val resultIntent = Intent()
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    fun deleteOrderItem(lineNo: String) {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.deleteItem(
                DeleteItemRequest(
                    orderNo, lineNo
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@AddOrderItemActivity,
                                    response.body()!!.get("msg").toString().replace('"', ' ')
                                        .trim(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@AddOrderItemActivity,
                                    "The order item has been successfully deleted.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                emptyData()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
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
                    Log.d("Kaivan", response.body().toString())
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
                                emptyData()
                                Toast.makeText(
                                    this@AddOrderItemActivity,
                                    "Item Added",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val salesOrderLinesItem: SalesOrderLinesItem = Gson().fromJson(
                                    response.body()!!.getAsJsonObject("data"),
                                    SalesOrderLinesItem::class.java
                                )
                                orderArrayList.add(salesOrderLinesItem)
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

    private fun updateOrder(lineNo: Int?, qty: String) {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.updateItemQty(
                UpdateItemRequest(
                    orderNo, lineNo.toString(), qty
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@AddOrderItemActivity,
                                    response.body()!!.get("msg").toString().replace('"', ' ')
                                        .trim(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@AddOrderItemActivity,
                                    "Item Updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val salesOrderLinesItem: SalesOrderLinesItem = Gson().fromJson(
                                    response.body()!!.getAsJsonObject("data"),
                                    SalesOrderLinesItem::class.java
                                )
                                foundItem = orderArrayList.find { it.lineNo == lineNo }
                                foundItem!!.quantity = salesOrderLinesItem.qty
                                emptyData()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
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
        Log.d("TAG", "searchItemNoAPI: " + et_barcode.text.toString().trim())
        foundItem =
            orderArrayList.find { it.uPC == et_barcode.text.toString() || it.itemNo2 == et_barcode.text.toString() }
        if (foundItem != null) {
            findData(foundItem!!)
        } else {
            if (Utils.isOnline(this)) {
                ProgressDialog.start(this)
                ApiClient.getRestClient(
                    Constants.BASE_URL
                )!!.webservices.searchItemNo(
                    SearchOrderRequest(
                        et_barcode.text.toString().trim()
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


    }

    private fun findData(item: SalesOrderLinesItem) {
        et_barcode.setText("")
        iv_clean.visibility = View.INVISIBLE
        tvUPC.text = item.uPC
        tvNo.text = item.itemNo2
        tvDescription.text = item.description
        tvUnitofMeasure.text = item.unitOfMeasure
        tvUnitPrice.text = item.updatedUnitPrice()
        evQuantity.setText(item.quantity.toString())
        evQuantity.isEnabled = true
        tvAdd.visibility = View.GONE
        llEditDelete.visibility = View.VISIBLE
        evQuantity.requestFocus()
        selectedItemNo = item.no.toString()

        Handler(Looper.getMainLooper()).postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(evQuantity, InputMethodManager.SHOW_IMPLICIT)

        }, 500)

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
        llEditDelete.visibility = View.GONE
        evQuantity.requestFocus()
        selectedItemNo = item.no.toString()

        Handler(Looper.getMainLooper()).postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(evQuantity, InputMethodManager.SHOW_IMPLICIT)

        }, 500)

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
        llEditDelete.visibility = View.GONE

    }
}