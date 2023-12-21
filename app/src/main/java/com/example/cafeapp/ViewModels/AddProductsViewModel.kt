package com.example.cafeapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Models.ProductModel
import kotlinx.coroutines.launch


class AddProductsViewModel(application: Application) : AndroidViewModel(application) {


    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)

    fun addProduct(product: ProductModel) {
        viewModelScope.launch {
            db.addProductAsync(product)
        }
    }
}