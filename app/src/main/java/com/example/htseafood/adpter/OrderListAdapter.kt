package com.example.htseafood.adpter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.htseafood.databinding.ItemOrderBinding


class OrderListAdapter(
    private val mActivity: Activity,
    private val invoiceList: MutableList<String>?
) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = mActivity.layoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
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

    }

    override fun getItemCount(): Int {
        return invoiceList?.size ?: 0
    }

    class ViewHolder(var binding: ItemOrderBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}