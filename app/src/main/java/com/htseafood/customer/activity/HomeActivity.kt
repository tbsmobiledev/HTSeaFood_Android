package com.htseafood.customer.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.htseafood.customer.R
import com.htseafood.customer.adpter.InvoiceListAdapter
import com.htseafood.customer.adpter.OrderListAdapter
import com.htseafood.customer.adpter.ShipmentListAdapter
import com.htseafood.customer.apis.ApiClient
import com.htseafood.customer.custom.EqualSpacingItemDecoration
import com.htseafood.customer.interfaces.OrderListener
import com.htseafood.customer.interfaces.PDFListener
import com.htseafood.customer.model.request.ListRequest
import com.htseafood.customer.model.request.PDFRequest
import com.htseafood.customer.model.responses.ValueItem
import com.htseafood.customer.utils.Constants
import com.htseafood.customer.utils.ProgressDialog
import com.htseafood.customer.utils.SharedHelper
import com.htseafood.customer.utils.Utils
import kotlinx.android.synthetic.main.activity_home.ivAdd
import kotlinx.android.synthetic.main.activity_home.ivInvoice
import kotlinx.android.synthetic.main.activity_home.ivLogout
import kotlinx.android.synthetic.main.activity_home.ivOrder
import kotlinx.android.synthetic.main.activity_home.ivShipment
import kotlinx.android.synthetic.main.activity_home.llInvoice
import kotlinx.android.synthetic.main.activity_home.llOrder
import kotlinx.android.synthetic.main.activity_home.llShipment
import kotlinx.android.synthetic.main.activity_home.rvList
import kotlinx.android.synthetic.main.activity_home.tvCustEmail
import kotlinx.android.synthetic.main.activity_home.tvCustName
import kotlinx.android.synthetic.main.activity_home.tvInvoice
import kotlinx.android.synthetic.main.activity_home.tvNoDataFound
import kotlinx.android.synthetic.main.activity_home.tvNumber
import kotlinx.android.synthetic.main.activity_home.tvOrder
import kotlinx.android.synthetic.main.activity_home.tvShipment
import kotlinx.android.synthetic.main.activity_home.tvTitle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity(), OrderListener, PDFListener {
    private var totalItemCount = 10
    private var invoiceListAdapter: InvoiceListAdapter? = null
    private var orderListAdapter: OrderListAdapter? = null
    private var shipmentListAdapter: ShipmentListAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var itemArrayList = ArrayList<ValueItem>()
    var openScreen = "I"
    private val getArray: TypeToken<ArrayList<ValueItem?>?> =
        object : TypeToken<ArrayList<ValueItem?>?>() {}

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                totalItemCount = 10
                orderAPI(totalItemCount / 10)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        tvNumber.text = SharedHelper.getKey(this, Constants.CustmerNo)
        tvCustName.text = SharedHelper.getKey(this, Constants.CustmerName)
        tvCustEmail.text = SharedHelper.getKey(this, Constants.CustmerEmail)


        rvList.addItemDecoration(
            EqualSpacingItemDecoration(
                resources.getDimension(com.intuit.sdp.R.dimen._10sdp).toInt(),
                EqualSpacingItemDecoration.VERTICAL
            )
        )
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvList.layoutManager = linearLayoutManager

        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = linearLayoutManager!!.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    totalItemCount += 10
                    when (openScreen) {
                        "I" -> {
                            invoiceAPI(totalItemCount / 10)
                        }

                        "O" -> {
                            orderAPI(totalItemCount / 10)
                        }

                        else -> {
                            shipmentAPI(totalItemCount / 10)
                        }
                    }

                }
            }
        })

        invoiceAPI(totalItemCount / 10)

        ivAdd.setOnClickListener {
            val intent = Intent(this, OrderDetailActivity::class.java).putExtra("id", "create")
            resultLauncher.launch(intent)
        }

        llInvoice.setOnClickListener {
            if (openScreen != "I") {
                openScreen = "I"
                totalItemCount = 10
                invoiceAPI(totalItemCount / 10)
                ivInvoice.setColorFilter(
                    ContextCompat.getColor(this, R.color.blue),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                ivOrder.setColorFilter(
                    ContextCompat.getColor(this, R.color.dark_text),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                ivShipment.setColorFilter(
                    ContextCompat.getColor(this, R.color.dark_text),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                tvInvoice.setTextColor(ContextCompat.getColor(this, R.color.blue))
                tvOrder.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
                tvShipment.setTextColor(ContextCompat.getColor(this, R.color.dark_text))

                tvInvoice.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_bold.ttf")
                tvOrder.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")
                tvShipment.typeface =
                    Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")

                llInvoice.background = ContextCompat.getDrawable(this, R.drawable.gredient_select)
                llOrder.background = null
                llShipment.background = null

                ivAdd.visibility = View.GONE

                tvTitle.text = getString(R.string.invoice)


            }
        }

        llOrder.setOnClickListener {
            if (openScreen != "O") {
                openScreen = "O"
                totalItemCount = 10
                orderAPI(totalItemCount / 10)
                ivInvoice.setColorFilter(
                    ContextCompat.getColor(this, R.color.dark_text),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                ivOrder.setColorFilter(
                    ContextCompat.getColor(this, R.color.blue),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                ivShipment.setColorFilter(
                    ContextCompat.getColor(this, R.color.dark_text),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                tvInvoice.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
                tvOrder.setTextColor(ContextCompat.getColor(this, R.color.blue))
                tvShipment.setTextColor(ContextCompat.getColor(this, R.color.dark_text))

                tvInvoice.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")
                tvOrder.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_bold.ttf")
                tvShipment.typeface =
                    Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")

                llInvoice.background = null
                llOrder.background = ContextCompat.getDrawable(this, R.drawable.gredient_select)
                llShipment.background = null
                ivAdd.visibility = View.VISIBLE

                tvTitle.text = getString(R.string.order)


            }

        }

        llShipment.setOnClickListener {
            if (openScreen != "S") {
                openScreen = "S"
                totalItemCount = 10
                shipmentAPI(totalItemCount / 10)
                ivInvoice.setColorFilter(
                    ContextCompat.getColor(this, R.color.dark_text),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                ivOrder.setColorFilter(
                    ContextCompat.getColor(this, R.color.dark_text),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                ivShipment.setColorFilter(
                    ContextCompat.getColor(this, R.color.blue),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                tvInvoice.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
                tvOrder.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
                tvShipment.setTextColor(ContextCompat.getColor(this, R.color.blue))

                tvInvoice.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")
                tvOrder.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")
                tvShipment.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_bold.ttf")

                llInvoice.background = null
                llOrder.background = null
                llShipment.background = ContextCompat.getDrawable(this, R.drawable.gredient_select)

                ivAdd.visibility = View.GONE

                tvTitle.text = getString(R.string.shipment)
            }

        }



        ivLogout.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Logout")
            builder.setMessage("Are you sure logout?")

            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                builder.setCancelable(true)
                SharedHelper.putKey(this, Constants.IS_LOGIN, false)
                SharedHelper.clearSharedPreferences(this)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }

            //performing negative action
            builder.setNegativeButton("No") { dialogInterface, which ->
                builder.setCancelable(true)
            }

            builder.setCancelable(false)
            builder.show()
        }


    }

    private fun invoiceAPI(page: Int) {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.invoiceList(
                ListRequest(
                    10, SharedHelper.getKey(this, Constants.CustmerNo), page
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@HomeActivity,
                                    "API Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val list: ArrayList<ValueItem> =
                                    Gson().fromJson(
                                        response.body()!!.getAsJsonObject("data")
                                            .getAsJsonArray("value"),
                                        getArray.type
                                    )

                                invoiceBindData(list)


                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(
                        this@HomeActivity, getString(R.string.api_fail_message), Toast.LENGTH_SHORT
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

    private fun orderAPI(page: Int) {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.orderList(
                ListRequest(
                    10, SharedHelper.getKey(this, Constants.CustmerNo), page
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@HomeActivity,
                                    "API Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val list: ArrayList<ValueItem> =
                                    Gson().fromJson(
                                        response.body()!!.getAsJsonObject("data")
                                            .getAsJsonArray("value"),
                                        getArray.type
                                    )

                                orderBindData(list)


                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(
                        this@HomeActivity, getString(R.string.api_fail_message), Toast.LENGTH_SHORT
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


    private fun shipmentAPI(page: Int) {
        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.shipmentList(
                ListRequest(
                    10, SharedHelper.getKey(this, Constants.CustmerNo), page
                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@HomeActivity,
                                    "API Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val list: ArrayList<ValueItem> =
                                    Gson().fromJson(
                                        response.body()!!.getAsJsonObject("data")
                                            .getAsJsonArray("value"),
                                        getArray.type
                                    )

                                shipmentBindData(list)


                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(
                        this@HomeActivity, getString(R.string.api_fail_message), Toast.LENGTH_SHORT
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


    private fun invoiceBindData(arrayList: ArrayList<ValueItem>) {

        if (totalItemCount == 10) {
            itemArrayList.clear()
        }
        if (arrayList.size != 0) {
            itemArrayList.addAll(arrayList)
            rvList!!.visibility = View.VISIBLE
            tvNoDataFound!!.visibility = View.GONE
            if (totalItemCount == 10) {
                invoiceListAdapter = InvoiceListAdapter(this, itemArrayList)
                rvList!!.adapter = invoiceListAdapter

            } else {
                invoiceListAdapter!!.notifyDataSetChanged()
            }
        } else {
            if (totalItemCount == 10) {
                rvList!!.visibility = View.GONE
                tvNoDataFound!!.visibility = View.VISIBLE
            }
        }
    }

    private fun orderBindData(arrayList: ArrayList<ValueItem>) {

        if (totalItemCount == 10) {
            itemArrayList.clear()
        }
        if (arrayList.size != 0) {
            itemArrayList.addAll(arrayList)
            rvList!!.visibility = View.VISIBLE
            tvNoDataFound!!.visibility = View.GONE
            if (totalItemCount == 10) {
                orderListAdapter = OrderListAdapter(this, itemArrayList, this, this)
                rvList!!.adapter = orderListAdapter

            } else {
                orderListAdapter!!.notifyDataSetChanged()
            }
        } else {
            if (totalItemCount == 10) {
                rvList!!.visibility = View.GONE
                tvNoDataFound!!.visibility = View.VISIBLE
            }
        }
    }

    private fun shipmentBindData(arrayList: ArrayList<ValueItem>) {

        if (totalItemCount == 10) {
            itemArrayList.clear()
        }
        if (arrayList.size != 0) {
            itemArrayList.addAll(arrayList)
            rvList!!.visibility = View.VISIBLE
            tvNoDataFound!!.visibility = View.GONE
            if (totalItemCount == 10) {
                shipmentListAdapter = ShipmentListAdapter(this, itemArrayList)
                rvList!!.adapter = shipmentListAdapter

            } else {
                shipmentListAdapter!!.notifyDataSetChanged()
            }
        } else {
            if (totalItemCount == 10) {
                rvList!!.visibility = View.GONE
                tvNoDataFound!!.visibility = View.VISIBLE
            }
        }
    }

    override fun openOrder(orderId: String) {
        val intent = Intent(this, OrderDetailActivity::class.java).putExtra("id", "create")
            .putExtra("id", orderId)
        resultLauncher.launch(intent)
    }

    override fun sendPDF(orderId: String) {

        if (Utils.isOnline(this)) {
            ProgressDialog.start(this)
            ApiClient.getRestClient(
                Constants.BASE_URL
            )!!.webservices.sendPDFRequest(
                PDFRequest(
                    orderId, SharedHelper.getKey(this, Constants.CustmerEmail)

                )
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    ProgressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            if (!response.body()!!.get("status").asBoolean) {
                                Toast.makeText(
                                    this@HomeActivity,
                                    "API Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@HomeActivity,
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
                        this@HomeActivity, getString(R.string.api_fail_message), Toast.LENGTH_SHORT
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