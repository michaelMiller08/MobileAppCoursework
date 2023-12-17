package com.example.cafeapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Helpers.UserManager
import com.example.cafeapp.Models.ProductModel

class LandingViewModel(application: Application) : AndroidViewModel(application) {

    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)


    fun logout() {
        UserManager.clearLoggedInUser(getApplication())
    }

    fun getProducts() : ArrayList<ProductModel> {
        return db.getAllProducts()
    }

}