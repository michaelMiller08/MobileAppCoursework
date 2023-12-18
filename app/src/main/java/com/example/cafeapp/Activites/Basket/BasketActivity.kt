package com.example.cafeapp.Activites.Basket

import BasketViewModel
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R

class BasketActivity : AppCompatActivity() {
    private val basketViewModel: BasketViewModel by viewModels()
    private lateinit var clearBasket: Button
    private lateinit var checkout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)


        val basketAdapter = BasketAdapter(basketViewModel.getItemsInBasket())
        recyclerView.adapter = basketAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        clearBasket = findViewById(R.id.btnClearBasket)
        checkout = findViewById(R.id.btnCheckout)

        clearBasket.setOnClickListener { clearBasket() }
    }

    private fun clearBasket(){
        basketViewModel.clearBasket()
    }
}
