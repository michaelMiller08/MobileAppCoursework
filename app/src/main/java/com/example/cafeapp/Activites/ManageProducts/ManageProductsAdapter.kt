package com.example.cafeapp.Activites.ManageProducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.R
import java.text.NumberFormat
import java.util.Currency


class ManageProductsAdapter(private var itemList: List<ProductModel>, private val clickListener: ReplaceImageClickListener) :
    RecyclerView.Adapter<ManageProductsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views in the item layout
        val itemName: TextView = itemView.findViewById(R.id.productName)
        val itemPrice: TextView = itemView.findViewById(R.id.productPrice)
        val removeProduct: Button = itemView.findViewById(R.id.removeProduct)
        val replaceImage: Button = itemView.findViewById(R.id.replaceImage)
        val editProduct: Button = itemView.findViewById(R.id.editProduct)
        // Add more views as needed
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout and create a ViewHolder
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.manage_products_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to the views
        val currentItem = itemList[position]
        holder.itemName.text = currentItem.name
        holder.removeProduct.setOnClickListener {}
        holder.editProduct.setOnClickListener {handleEditProductOnClick(holder)
        }
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance("GBP")
        holder.itemPrice.text = currencyFormat.format(currentItem.price)

        holder.replaceImage.setOnClickListener {
            // Trigger the interface method when the image is clicked
            clickListener.onReplaceImageClick(currentItem.id)
        }
    }
    private fun handleEditProductOnClick(holder: ViewHolder) {
        val productId = itemList[holder.adapterPosition].id // Assuming there is an 'id' property in your ProductModel
        val fragmentManager = holder.itemView.context as AppCompatActivity
        val dialog = CustomEditProductDialog(productId)
        dialog.show(fragmentManager.supportFragmentManager, "editProductDialog")
    }
    override fun getItemCount(): Int {
        // Return the number of items in the list
        return itemList.size
    }


}
