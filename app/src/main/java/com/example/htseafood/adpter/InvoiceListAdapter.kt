package com.example.htseafood.adpter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.htseafood.activity.InvoiceDetailActivity
import com.example.htseafood.databinding.ItemInvoiceBinding
import com.example.htseafood.model.responses.ValueItem


class InvoiceListAdapter(
    private val mActivity: Activity,
    private val invoiceList: MutableList<ValueItem>?
) :
    RecyclerView.Adapter<InvoiceListAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = mActivity.layoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInvoiceBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.invoice = invoiceList!![position]
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

        holder.binding.llInvoice.setOnClickListener {
            mActivity.startActivity(
                Intent(
                    mActivity,
                    InvoiceDetailActivity::class.java
                ).putExtra("id", invoiceList[position].no)
            )
        }

        holder.binding.tvInvoiceNo.text =
            "${position + 1}.  Invoice No : #" + invoiceList[position].no

    }

    override fun getItemCount(): Int {
        return invoiceList?.size ?: 0
    }

    class ViewHolder(var binding: ItemInvoiceBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}