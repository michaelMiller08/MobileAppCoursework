package com.example.cafeapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Models.OrderModel
import com.example.cafeapp.Models.ProductModel

class ManageOrdersViewModel(application: Application) : AndroidViewModel(application) {


        private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)

        fun getAllPreparingOrders() : List<OrderModel>{
           return db.getAllPreparingOrders()
        }

    fun getAllProductsFromOrder(orderId: Int): List<ProductModel> {
        val productIds = db.getProductIdsByOrderId(orderId)
        val products = mutableListOf<ProductModel>()

        for (productId in productIds) {
            val product = db.getProductById(productId)
            if (product != null) {
                products.add(product)
            }
        }

        return products
    }

    fun markAsCollect(orderId: Int) {
        db.markOrderForCollection(orderId)
    }

}