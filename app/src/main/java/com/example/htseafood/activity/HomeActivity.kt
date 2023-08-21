package com.example.htseafood.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.htseafood.R
import com.example.htseafood.adpter.InvoiceListAdapter
import com.example.htseafood.adpter.OrderListAdapter
import com.example.htseafood.adpter.ShipmentListAdapter
import com.example.htseafood.custom.EqualSpacingItemDecoration
import com.example.htseafood.utils.Constants
import com.example.htseafood.utils.SharedHelper
import com.example.htseafood.utils.Utils
import kotlinx.android.synthetic.main.activity_home.ivInvoice
import kotlinx.android.synthetic.main.activity_home.ivLogout
import kotlinx.android.synthetic.main.activity_home.ivOrder
import kotlinx.android.synthetic.main.activity_home.ivShipment
import kotlinx.android.synthetic.main.activity_home.llInvoice
import kotlinx.android.synthetic.main.activity_home.llOrder
import kotlinx.android.synthetic.main.activity_home.llShipment
import kotlinx.android.synthetic.main.activity_home.rvList
import kotlinx.android.synthetic.main.activity_home.tvInvoice
import kotlinx.android.synthetic.main.activity_home.tvNumber
import kotlinx.android.synthetic.main.activity_home.tvOrder
import kotlinx.android.synthetic.main.activity_home.tvShipment
import kotlinx.android.synthetic.main.activity_home.tvTitle

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        tvNumber.text = SharedHelper.getKey(this, Constants.CustmerNo)

        val invoiceListAdapter = InvoiceListAdapter(this, Utils.getDummyArrayList(8))
        rvList.adapter = invoiceListAdapter

        llInvoice.setOnClickListener {
            ivInvoice.setColorFilter(
                ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN
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
            tvShipment.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")

            llInvoice.background = ContextCompat.getDrawable(this, R.drawable.gredient_select)
            llOrder.background = null
            llShipment.background = null

            tvTitle.text = getString(R.string.invoice)


            val invoiceListAdapter = InvoiceListAdapter(this, Utils.getDummyArrayList(8))
            rvList.adapter = invoiceListAdapter
        }

        llOrder.setOnClickListener {
            ivInvoice.setColorFilter(
                ContextCompat.getColor(this, R.color.dark_text),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            ivOrder.setColorFilter(
                ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN
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
            tvShipment.typeface = Typeface.createFromAsset(this.assets, "fonts/sora_regular.ttf")

            llInvoice.background = null
            llOrder.background = ContextCompat.getDrawable(this, R.drawable.gredient_select)
            llShipment.background = null

            tvTitle.text = getString(R.string.order)

            val orderListAdapter = OrderListAdapter(this, Utils.getDummyArrayList(8))
            rvList.adapter = orderListAdapter

        }

        llShipment.setOnClickListener {
            ivInvoice.setColorFilter(
                ContextCompat.getColor(this, R.color.dark_text),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            ivOrder.setColorFilter(
                ContextCompat.getColor(this, R.color.dark_text),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            ivShipment.setColorFilter(
                ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN
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

            tvTitle.text = getString(R.string.shipment)

            val shipmentListAdapter = ShipmentListAdapter(this, Utils.getDummyArrayList(8))
            rvList.adapter = shipmentListAdapter

        }

        rvList.addItemDecoration(
            EqualSpacingItemDecoration(
                resources.getDimension(com.intuit.sdp.R.dimen._12sdp).toInt(),
                EqualSpacingItemDecoration.VERTICAL
            )
        )
        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

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
}