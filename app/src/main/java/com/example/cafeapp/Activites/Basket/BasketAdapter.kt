package com.example.cafeapp.Activites.Basket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.R

class BasketAdapter(private var itemList: List<ProductModel>) :
    RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views in the item layout
        val itemName: TextView = itemView.findViewById(R.id.productName)
        val itemPrice: Button = itemView.findViewById(R.id.removeProduct)
        // Add more views as needed
    }

    fun updateDataSet(newItemList: List<ProductModel>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout and create a ViewHolder
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.basket_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to the views
        val currentItem = itemList[position]
        holder.itemName.text = currentItem.name
//        holder.itemPrice.text = currentItem.price.toString()
        // Bind more data as needed
    }

    override fun getItemCount(): Int {
        // Return the number of items in the list
        return itemList.size
    }


}
