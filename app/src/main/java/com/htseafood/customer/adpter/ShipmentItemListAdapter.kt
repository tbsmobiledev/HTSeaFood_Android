package com.htseafood.customer.adpter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htseafood.customer.databinding.ItemShipmentItemBinding
import com.htseafood.customer.model.responses.PostedSalesShipmentLinesItem


class ShipmentItemListAdapter(
    private val mActivity: Activity,
    private val shipmentList: MutableList<PostedSalesShipmentLinesItem>?
) :
    RecyclerView.Adapter<ShipmentItemListAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = mActivity.layoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShipmentItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.detail = shipmentList!![position]
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

        holder.binding.tvNo.text = "No " + (position + 1) + "  : " + shipmentList[position].no

    }

    override fun getItemCount(): Int {
        return shipmentList?.size ?: 0
    }

    class ViewHolder(var binding: ItemShipmentItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}