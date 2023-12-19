package com.example.cafeapp.Activites.Landing

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.databinding.CardCellBinding

class CardViewHolder(
    private val cardCellBinding: CardCellBinding,
    private val onClickListener: ProductOnClickListener
) : RecyclerView.ViewHolder(cardCellBinding.root) {
    fun bindProduct(productModel: ProductModel) {
        var bmp: Bitmap =
            BitmapFactory.decodeByteArray(productModel.image, 0, productModel.image!!.size)
        cardCellBinding.productImg.setImageBitmap(bmp)
        cardCellBinding.productTitle.text = productModel.name
        cardCellBinding.cardView.setOnClickListener {

            cardCellBinding.productPrice.text = ("£${productModel.price}")
            onClickListener.onClick(productModel)
        }
    }
}