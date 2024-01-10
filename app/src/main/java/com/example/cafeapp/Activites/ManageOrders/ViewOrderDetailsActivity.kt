package com.example.cafeapp.Activites.ManageOrders

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.R
import com.example.cafeapp.ViewModels.ManageOrdersViewModel

class ViewOrderDetailsActivity : AppCompatActivity() {
    private val viewModel: ManageOrdersViewModel by viewModels()
    private lateinit var productList: TextView
    private lateinit var btnMarkAsCollect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order_details)
        productList = findViewById(R.id.productTitle)
        btnMarkAsCollect = findViewById(R.id.btnMarkAsCollected)


        val orderId = intent.getIntExtra("orderId", -1)


        for (productModel in viewModel.getAllProductsFromOrder(orderId)) {
            productList.text = "${productList.text} ${productModel.name}"
        }

        viewModel.markAsCollect(orderId)

    }
}