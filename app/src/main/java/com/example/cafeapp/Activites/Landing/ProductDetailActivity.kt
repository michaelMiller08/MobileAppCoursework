package com.example.cafeapp.Activites.Landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.ViewModels.LandingViewModel
import com.example.cafeapp.databinding.ActivityProductDetailBinding
import java.text.NumberFormat
import java.util.Currency

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var viewModel: LandingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[LandingViewModel::class.java]

        val productID = intent.getIntExtra("productExtra", -1)
        val product = productFromID(productID)
        if (product != null) {
            binding.productTitle.text = product.name
            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency = Currency.getInstance("GBP")
            binding.productPrice.text = currencyFormat.format(product.price)
        }
    }

    private fun productFromID(productID: Int): ProductModel? {
        for (product in viewModel.getProducts()) {
            if (product.id == productID)
                return product

        }
        return null
    }
}