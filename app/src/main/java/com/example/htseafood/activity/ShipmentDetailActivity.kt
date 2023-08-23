package com.example.htseafood.activity

import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.htseafood.R
import com.example.htseafood.adpter.ShipmentItemListAdapter
import com.example.htseafood.custom.EqualSpacingItemDecoration
import com.example.htseafood.utils.Utils
import kotlinx.android.synthetic.main.activity_shipment_detail.ivBack
import kotlinx.android.synthetic.main.activity_shipment_detail.rvList


class ShipmentDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipment_detail)
        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        rvList.addItemDecoration(
            EqualSpacingItemDecoration(
                resources.getDimension(com.intuit.sdp.R.dimen._10sdp).toInt(),
                EqualSpacingItemDecoration.VERTICAL
            )
        )
        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val shipmentItemListAdapter = ShipmentItemListAdapter(this, Utils.getDummyArrayList(8))
        rvList.adapter = shipmentItemListAdapter
    }


}