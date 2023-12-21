package com.example.cafeapp.Activites.ManageProducts

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
        val itemImage: ImageView = itemView.findViewById(R.id.productImage)
        val itemName: TextView = itemView.findViewById(R.id.productName)
        val itemPrice: TextView = itemView.findViewById(R.id.productPrice)
        val removeProduct: Button = itemView.findViewById(R.id.removeProduct)
        val replaceImage: Button = itemView.findViewById(R.id.replaceImage)
        val editProduct: Button = itemView.findViewById(R.id.editProduct)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout and create a ViewHolder
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.manage_products_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        val bmp: Bitmap =
            BitmapFactory.decodeByteArray(currentItem.image, 0, currentItem.image!!.size)
        holder.itemImage.setImageBitmap(bmp)
        holder.itemName.text = currentItem.name
        holder.removeProduct.setOnClickListener {handleRemoveProductOnClick(currentItem.id)}
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

    private fun handleRemoveProductOnClick(id: Int){
        //ToDo: Needs implementing
    }
    private fun handleEditProductOnClick(holder: ViewHolder) {
        val productId = itemList[holder.adapterPosition].id
        val fragmentManager = holder.itemView.context as AppCompatActivity
        val dialog = CustomEditProductDialog(productId)
        dialog.show(fragmentManager.supportFragmentManager, "editProductDialog")
    }
    override fun getItemCount(): Int {
        // Return the number of items in the list
        return itemList.size
    }


}
