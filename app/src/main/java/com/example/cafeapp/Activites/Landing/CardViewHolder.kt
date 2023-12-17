package com.example.cafeapp.Activites.Landing

import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.databinding.CardCellBinding
import java.text.NumberFormat
import java.util.Currency

class CardViewHolder(
    private val cardCellBinding: CardCellBinding,
    private val onClickListener: ProductOnClickListener
) : RecyclerView.ViewHolder(cardCellBinding.root) {
    fun bindProduct(productModel: ProductModel) {
//        Glide.with(cardCellBinding.productImg.context)
//            .load(productModel.image)
//            .into(cardCellBinding.productImg)
//        var bmp: Bitmap =
//            BitmapFactory.decodeByteArray(productModel.image, 0, productModel.image!!.size)
//        cardCellBinding.productImg.setImageBitmap(bmp)
        cardCellBinding.productTitle.text = productModel.name
        cardCellBinding.cardView.setOnClickListener {
            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency = Currency.getInstance("GBP")
            cardCellBinding.productPrice.text = currencyFormat.format(productModel.price)
            onClickListener.onClick(productModel)
        }
    }
}