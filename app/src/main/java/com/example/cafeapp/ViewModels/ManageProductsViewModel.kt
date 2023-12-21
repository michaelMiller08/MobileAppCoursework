package com.example.cafeapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Models.ProductModel

class ManageProductsViewModel(application: Application) : AndroidViewModel(application) {
    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)

    fun getProducts(): List<ProductModel> {
        return db.getAllProducts()
    }


//    fun updateProduct(product: ProductModel){
//        db.editProduct(product)
//    }

    //Returns a boolean if failed returns false
    fun updateProduct(id: Int, name: String, price: Float): Boolean {
        val product = db.getProductById(id)
        var newName = name
        var newPrice = price
        if (name == "") {
            if (product != null) {
                newName = product.name

            }
        }

        if (price == 0F) {
            if (product != null) {
                newPrice = product.price
            }
        }


        val newProduct = product?.let {
            ProductModel(
                it.id,
                newName,
                newPrice,
                null,
                product.available
            )
        }

        return if (newProduct != null) {
            db.editProduct(newProduct)
            true
        } else {
            false
        }
    }

    fun replaceProductImage(productId: Int, img: ByteArray) : Boolean{
        return try{
            db.editProductImageById(productId, img)
            true;
        } catch(e: Exception) {
            false
        }

    }
}