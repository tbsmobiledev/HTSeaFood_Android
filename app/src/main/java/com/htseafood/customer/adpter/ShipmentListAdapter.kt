package com.htseafood.customer.adpter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htseafood.customer.activity.ShipmentDetailActivity
import com.htseafood.customer.databinding.ItemShipmentBinding
import com.htseafood.customer.model.responses.ValueItem


class ShipmentListAdapter(
    private val mActivity: Activity,
    private val shipmentList: MutableList<ValueItem>?
) :
    RecyclerView.Adapter<ShipmentListAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = mActivity.layoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShipmentBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.shipment = shipmentList!![position]
        holder.binding.ivArrow.setOnClickListener {
            if (holder.binding.ivArrow.tag == "0") {
                holder.binding.ivArrow.tag = "1"
                holder.binding.llExtraView.visibility = View.VISIBLE
                holder.binding.ivArrow.rotation = 180f
            } else {
                holder.binding.ivArrow.tag = "0"
                holder.binding.llExtraView.visibility = View.GONE
                holder.binding.ivArrow.rotation = 0f
            }
        }

        holder.binding.llShipment.setOnClickListener {
            mActivity.startActivity(
                Intent(
                    mActivity,
                    ShipmentDetailActivity::class.java
                ).putExtra("id", shipmentList[position].no)
            )
        }

        holder.binding.tvShipmentNo.text = "Shipment No : #" + shipmentList[position].no

    }

    override fun getItemCount(): Int {
        return shipmentList?.size ?: 0
    }

    class ViewHolder(var binding: ItemShipmentBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}