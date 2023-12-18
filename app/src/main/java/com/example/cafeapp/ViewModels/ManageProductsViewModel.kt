package com.example.cafeapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Models.ProductModel

class ManageProductsViewModel (application: Application) : AndroidViewModel(application) {
    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)

    fun getProducts() : List<ProductModel>{
        return db.getAllProducts()
    }


//    fun updateProduct(product: ProductModel){
//        db.editProduct(product)
//    }

    //Returns a boolean if failed returns false
    fun updateProduct(id: Int, name: String, price: Float): Boolean {
        val product = db.getProductById(id)

        if (product != null) {
            product.name = name
            product.price = price
            db.editProduct(product)
            return true
        } else {
            return false
        }
    }
}