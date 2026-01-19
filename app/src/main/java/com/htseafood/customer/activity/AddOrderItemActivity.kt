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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.htseafood.customer.R
import com.htseafood.customer.adpter.ItemListAdapter
import com.htseafood.customer.apis.ApiClient
import com.htseafood.customer.custom.BaseActivity
import com.htseafood.customer.databinding.ActivityAddOrderItemBinding
import com.htseafood.customer.model.request.AddItemRequest
import com.htseafood.customer.model.request.DeleteItemRequest
import com.htseafood.customer.model.request.SearchOrderRequest
import com.htseafood.customer.model.request.UpdateItemRequest
import com.htseafood.customer.model.responses.OrderItemResponse
import com.htseafood.customer.model.responses.SalesOrderLinesItem
import com.htseafood.customer.utils.Constants
import com.htseafood.customer.utils.ProgressDialog
import com.htseafood.customer.utils.SharedHelper
import com.htseafood.customer.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOrderItemActivity : BaseActivity<ActivityAddOrderItemBinding>() {
    private var itemArrayList = ArrayList<OrderItemResponse>()
    private val getArray: TypeToken<ArrayList<OrderItemResponse?>?> =
        object : TypeToken<ArrayList<OrderItemResponse?>?>() {}
    private val getOrder: TypeToken<ArrayList<SalesOrderLinesItem?>?> =
        object : TypeToken<ArrayList<SalesOrderLinesItem?>?>() {}
    private var orderArrayList = ArrayList<SalesOrderLinesItem>()
    var foundItem: SalesOrderLinesItem? = null
    var orderNo = ""
    var selectedItemNo = ""
    override fun inflateBinding(): ActivityAddOrderItemBinding {
        return ActivityAddOrderItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivBack.setOnClickListener {
            handleBackPressed()
        }
        if (intent != null) {
            orderNo = intent.getStringExtra("orderNo").toString()
            orderArrayList =
                Gson().fromJson(intent.getStringExtra("orderData").toString(), getOrder.type)
        }
        binding.etBarcode.requestFocus()

        binding.etBarcode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (text.isEmpty()) {
                    binding.ivClean.visibility = View.INVISIBLE
                } else {
                    binding.ivClean.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding.etBarcode.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                // Code to be executed when the Enter key is pressed
                searchItemNoAPI()
                Utils.hideSoftKeyboard(this@AddOrderItemActivity, binding.etBarcode)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.ivClean.setOnClickListener {
            binding.etBarcode.setText("")
        }

        binding.tvSearchNo.setOnClickListener {

            if (binding.etBarcode.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter UPC or Item No", Toast.LENGTH_SHORT).show()
            } else {
                searchItemNoAPI()
            }
            Utils.hideSoftKeyboard(this, binding.etBarcode)
        }

        binding.tvAdd.setOnClickListener {
            if (binding.evQuantity.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
            } else {
                addQuantity()
            }
            Utils.hideSoftKeyboard(this, binding.etBarcode)
        }

        binding.tvEdit.setOnClickListener {
            if (binding.evQuantity.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
            } else {
                updateOrder(foundItem!!.lineNo, binding.evQuantity.text.toString())
            }

        }

        binding.tvDelete.setOnClickListener {
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

    override fun handleBackPressed(callback: OnBackPressedCallback?) {        //super.onBackPressed()
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
                    orderNo, binding.evQuantity.text.toString(),
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
        Log.d("TAG", "searchItemNoAPI: " + binding.etBarcode.text.toString().trim())
        foundItem =
            orderArrayList.find { it.uPC == binding.etBarcode.text.toString() || it.itemNo2 == binding.etBarcode.text.toString() }
        if (foundItem != null) {
            findData(foundItem!!)
        } else {
            if (Utils.isOnline(this)) {
                ProgressDialog.start(this)
                ApiClient.getRestClient(
                    Constants.BASE_URL
                )!!.webservices.searchItemNo(
                    SearchOrderRequest(
                        binding.etBarcode.text.toString().trim(),
                        SharedHelper.getKey(this, Constants.CustmerPriceGroup)
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
        binding.etBarcode.setText("")
        binding.ivClean.visibility = View.INVISIBLE
        binding.tvUPC.text = item.uPC
        binding.tvNo.text = item.itemNo2
        binding.tvDescription.text = item.description
        binding.tvUnitofMeasure.text = item.unitOfMeasure
        binding.tvUnitPrice.text = item.updatedUnitPrice()
        binding.tvQtyOnHand.text = item.qtyOnHand.toString()
        binding.tvCasePack.text = item.casePack
        binding.evQuantity.setText(item.quantity.toString())
        binding.evQuantity.isEnabled = true
        binding.tvAdd.visibility = View.GONE
        binding.llEditDelete.visibility = View.VISIBLE
        binding.evQuantity.requestFocus()
        selectedItemNo = item.no.toString()

        Handler(Looper.getMainLooper()).postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.evQuantity, InputMethodManager.SHOW_IMPLICIT)

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
        binding.etBarcode.setText("")
        binding.ivClean.visibility = View.INVISIBLE
        binding.tvUPC.text = item.uPC
        binding.tvNo.text = item.itemNo2
        binding.tvDescription.text = item.description
        binding.tvUnitofMeasure.text = item.baseUnitOfMeasure
        binding.tvCasePack.text = item.casePack
        binding.tvQtyOnHand.text = item.qtyOnHand.toString()
        binding.tvUnitPrice.text = item.updatedUnitPrice()
        binding.evQuantity.setText("")
        binding.evQuantity.isEnabled = true
        binding.tvAdd.visibility = View.VISIBLE
        binding.llEditDelete.visibility = View.GONE
        binding.evQuantity.requestFocus()
        selectedItemNo = item.no.toString()

        Handler(Looper.getMainLooper()).postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.evQuantity, InputMethodManager.SHOW_IMPLICIT)

        }, 500)

    }

    private fun emptyData() {
        binding.etBarcode.requestFocus()
        binding.etBarcode.setText("")
        binding.ivClean.visibility = View.INVISIBLE
        binding.tvUPC.text = ""
        binding.tvNo.text = ""
        binding.tvDescription.text = ""
        binding.tvUnitofMeasure.text = ""
        binding.tvUnitPrice.text = ""
        binding.evQuantity.setText("")
        binding.evQuantity.isEnabled = false
        binding.tvAdd.visibility = View.GONE
        binding.llEditDelete.visibility = View.GONE

    }
}