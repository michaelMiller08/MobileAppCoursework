package com.example.cafeapp.Activites.ManageOrders

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Models.OrderModel
import com.example.cafeapp.R

class ManageOrdersAdapter(private val context: Context, private val orders: List<OrderModel>) :
    RecyclerView.Adapter<ManageOrdersAdapter.ManageOrdersViewHolder>() {

    class ManageOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewOrderDate: TextView = itemView.findViewById(R.id.orderDate)
        val textViewOrderTime: TextView = itemView.findViewById(R.id.orderTime)
        val textViewOrderStatus: TextView = itemView.findViewById(R.id.orderStatus)
        val btnViewOrder: Button = itemView.findViewById(R.id.btnViewOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageOrdersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manage_orders, parent, false)
        return ManageOrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: ManageOrdersViewHolder, position: Int) {
        val order = orders[position]
        holder.textViewOrderDate.text = "Date: ${order.orderDate}"
        holder.textViewOrderTime.text = "Time: ${order.orderTime}"
        holder.textViewOrderStatus.text = "Status: ${order.orderStatus}"

        holder.btnViewOrder.setOnClickListener {
            val intent = Intent(context, ViewOrderDetailsActivity::class.java)
            intent.putExtra("orderId", order.orderId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = orders.size
}
