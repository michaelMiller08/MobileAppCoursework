package com.example.cafeapp.Activites.Landing

import BasketViewModel
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.ViewModels.LandingViewModel
import com.example.cafeapp.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var viewModel: LandingViewModel
//    private lateinit var basketViewModel: BasketViewModel
private val basketViewModel: BasketViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[LandingViewModel::class.java]
//        basketViewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        )[BasketViewModel::class.java]



        val productID = intent.getIntExtra("productExtra", -1)
        val product = basketViewModel.productFromID(productID)
        if (product != null) {

            val bmp: Bitmap =
                BitmapFactory.decodeByteArray(product.image, 0, product.image!!.size)
            binding.productImg.setImageBitmap(bmp)
            binding.productTitle.text = product.name
            binding.productPrice.text = ("£${product.price}")

            binding.btnAddToBasket.setOnClickListener { addToBasketOnClick(productID) }
        }
    }

    private fun addToBasketOnClick(productId: Int) {
        basketViewModel.addToBasketById(productId)
        Toast.makeText(this, "Item added to basket successfully!", Toast.LENGTH_SHORT).show()
    }

}