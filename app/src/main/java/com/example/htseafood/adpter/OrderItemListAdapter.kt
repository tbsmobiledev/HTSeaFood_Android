package com.example.htseafood.adpter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.htseafood.databinding.ItemOrderItemBinding
import com.example.htseafood.interfaces.DeleteItemListener
import com.example.htseafood.interfaces.EditListener
import com.example.htseafood.model.responses.SalesOrderLinesItem


class OrderItemListAdapter(
    private val mActivity: Activity,
    private val orderList: MutableList<SalesOrderLinesItem>?,
    val deleteItemListener: DeleteItemListener,
    val editListener: EditListener
) :
    RecyclerView.Adapter<OrderItemListAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = mActivity.layoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.detail = orderList!![position]
        holder.binding.ivArrow.setOnClickListener {
            if (holder.binding.ivArrow.tag == "0") {
                holder.binding.ivArrow.tag = "1"
                holder.binding.llExtraView.visibility = View.VISIBLE
                holder.binding.rlButtons.visibility = View.VISIBLE
                holder.binding.ivArrow.rotation = 180f
            } else {
                holder.binding.ivArrow.tag = "0"
                holder.binding.llExtraView.visibility = View.GONE
                holder.binding.rlButtons.visibility = View.GONE
                holder.binding.ivArrow.rotation = 0f
            }
        }

        holder.binding.tvNo.text = "No : " + orderList[position].itemNo2
        holder.binding.tvUPCNo.text = "UPC : " + orderList[position].uPC

        holder.binding.tvEdit.setOnClickListener {
            editListener.editQty(orderList[position])
        }

        holder.binding.tvDelete.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(mActivity)
            builder.setTitle("Delete Order Item")
            builder.setMessage("Are you sure you want to delete this order item?")

            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                builder.setCancelable(true)
                deleteItemListener.deleteOrderItem(orderList[position].lineNo.toString())
            }

            //performing negative action
            builder.setNegativeButton("No") { dialogInterface, which ->
                builder.setCancelable(true)
            }

            builder.setCancelable(false)
            builder.show()
        }

    }

    override fun getItemCount(): Int {
        return orderList?.size ?: 0
    }

    class ViewHolder(var binding: ItemOrderItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}