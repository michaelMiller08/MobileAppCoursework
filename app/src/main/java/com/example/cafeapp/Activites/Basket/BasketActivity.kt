package com.example.cafeapp.Activites.Basket

import BasketViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.Activites.ManageProducts.CustomCheckoutDialog
import com.example.cafeapp.R

class BasketActivity : AppCompatActivity() {
    private val basketViewModel: BasketViewModel by viewModels()
    private lateinit var totalPrice: TextView
    private lateinit var clearBasket: Button
    private lateinit var checkout: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)


        val basketAdapter = BasketAdapter(basketViewModel.getItemsInBasket(), basketViewModel)
        recyclerView.adapter = basketAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        totalPrice = findViewById(R.id.totalPrice);
        clearBasket = findViewById(R.id.btnClearBasket)
        checkout = findViewById(R.id.btnCheckout)

        clearBasket.setOnClickListener { clearBasket() }
        checkout.setOnClickListener { handleCheckoutOnClick() }

        // Observe changes in the basket LiveData
        basketViewModel.basket.observe(this) { newBasket ->
            // Update the adapter with the new dataset
            basketAdapter.updateDataSet(newBasket)

            if (newBasket.count() <= 1){
                clearBasket.visibility = View.GONE
                checkout.visibility = View.GONE
            }
            else{
                clearBasket.visibility = View.VISIBLE
                checkout.visibility = View.VISIBLE
            }
        }
        basketViewModel.totalPrice.observe(this, Observer { total ->
            val formattedTotal = String.format("£%.2f", total)
            totalPrice.text = "Total Price: $formattedTotal"
        })
    }

    private fun handleCheckoutOnClick() {
        val fragmentManager = this@BasketActivity.supportFragmentManager

        basketViewModel.totalPrice.observe(this@BasketActivity, Observer { totalPrice ->
            // Now totalPrice is the Float value inside LiveData
            val dialog = CustomCheckoutDialog(totalPrice)
            dialog.show(fragmentManager, "checkoutDialog")
        })
    }

    private fun clearBasket(){
        basketViewModel.clearBasket()
    }
}
