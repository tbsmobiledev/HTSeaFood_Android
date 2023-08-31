package com.example.htseafood.adpter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.htseafood.R
import com.example.htseafood.databinding.RowItemProductBinding
import com.example.htseafood.model.responses.OrderItemResponse


class ItemListAdapter(
    private val mActivity: Activity,
    private val list: MutableList<OrderItemResponse>?
) :
    RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {
    private var checkedPosition = 0

    private val layoutInflater: LayoutInflater = mActivity.layoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemProductBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.detail = list!![position]

        if (checkedPosition == position) {
            holder.binding.imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    mActivity,
                    R.drawable.checked
                )
            )
        } else {
            holder.binding.imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    mActivity,
                    R.drawable.unchecked
                )
            )

        }
        holder.binding.imageView.setOnClickListener { view: View? ->
            checkedPosition = position
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class ViewHolder(var binding: RowItemProductBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    fun getSelected(): OrderItemResponse {
        return list!![checkedPosition]
    }
}