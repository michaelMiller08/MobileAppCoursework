package com.example.cafeapp.Activites.Basket

import BasketViewModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.R
import java.text.NumberFormat
import java.util.Currency

class BasketAdapter(private var itemList: List<ProductModel>, private val basketViewModel: BasketViewModel) :
    RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views in the item layout
        val itemName: TextView = itemView.findViewById(R.id.productName)
        val itemPrice: TextView = itemView.findViewById(R.id.productPrice)
        val removeProduct: Button = itemView.findViewById(R.id.removeProduct)


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
        holder.removeProduct.setOnClickListener { basketViewModel. removeItemFromBasket(currentItem.id)}
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance("GBP")
        holder.itemPrice.text = currencyFormat.format(currentItem.price)

    }



    override fun getItemCount(): Int {
        // Return the number of items in the list
        return itemList.size
    }


}
