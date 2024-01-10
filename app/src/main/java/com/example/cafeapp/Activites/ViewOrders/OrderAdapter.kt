package com.example.cafeapp.Activites.ViewOrders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Models.OrderModel
import com.example.cafeapp.R

class OrderAdapter(private val orders: List<OrderModel>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewOrderDate: TextView = itemView.findViewById(R.id.orderDate)
        val textViewOrderTime: TextView = itemView.findViewById(R.id.orderTime)
        val textViewOrderStatus: TextView = itemView.findViewById(R.id.orderStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.textViewOrderDate.text = "Date: ${order.orderDate}"
        holder.textViewOrderTime.text = "Time: ${order.orderTime}"
        holder.textViewOrderStatus.text = "Status: ${order.orderStatus}"
    }

    override fun getItemCount(): Int = orders.size
}
