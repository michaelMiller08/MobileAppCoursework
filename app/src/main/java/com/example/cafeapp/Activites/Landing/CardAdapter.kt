package com.example.cafeapp.Activites.Landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.databinding.CardCellBinding

class CardAdapter(private val products: List<ProductModel>,
                  private val onClickListener: ProductOnClickListener
) :
    RecyclerView.Adapter<CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardCellBinding.inflate(from, parent, false)
        return CardViewHolder(binding, onClickListener)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindProduct(products[position])
    }
}