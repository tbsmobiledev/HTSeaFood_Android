package com.htseafood.customer.adpter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.htseafood.customer.databinding.ItemOrderBinding
import com.htseafood.customer.interfaces.OrderListener
import com.htseafood.customer.interfaces.PDFListener
import com.htseafood.customer.model.responses.ValueItem
import com.htseafood.customer.utils.Constants
import com.htseafood.customer.utils.SharedHelper


class OrderListAdapter(
    private val mActivity: Activity,
    private val orderList: MutableList<ValueItem>?,
    val orderListener: OrderListener,
    val pdfListener: PDFListener
) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = mActivity.layoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.order = orderList!![position]
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

        holder.binding.llOrder.setOnClickListener {
            orderListener.openOrder(orderList[position].no.toString())
        }

        holder.binding.tvPDFSend.setOnClickListener {
            if (orderList[position].amountIncludingVAT != 0.0 && SharedHelper.getKey(
                    mActivity,
                    Constants.CustmerEmail
                ) != ""
            ) {
                pdfListener.sendPDF(orderList[position].no.toString())
            }
        }

        holder.binding.tvOrderNo.text = "Order No : #" + orderList[position].no
    }

    override fun getItemCount(): Int {
        return orderList?.size ?: 0
    }

    class ViewHolder(var binding: ItemOrderBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}