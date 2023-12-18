package com.example.cafeapp.Activites.Basket

import BasketViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R

class BasketActivity : AppCompatActivity() {
    private val basketViewModel: BasketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Observe changes in the basket LiveData
        basketViewModel.basket.observe(this) { newBasket ->
            // Initialize the adapter only when the LiveData is updated
            val basketAdapter = BasketAdapter(newBasket)
            recyclerView.adapter = basketAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

//    var newBasket = mutableListOf<ProductModel>()
//        var model = ProductModel(1, "sdsd", 22.2F, null, true)
//        newBasket.add(model)
//        val basketAdapter = BasketAdapter(newBasket)
//        recyclerView.adapter = basketAdapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
