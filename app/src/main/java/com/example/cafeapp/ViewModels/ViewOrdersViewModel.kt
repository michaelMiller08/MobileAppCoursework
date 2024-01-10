package com.example.cafeapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Helpers.UserManager
import com.example.cafeapp.Models.OrderModel

class ViewOrdersViewModel(application: Application) : AndroidViewModel(application) {


    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)

    fun getAllOrders(): List<OrderModel>{

       return db.getAllOrdersByCustomerId(UserManager.getLoggedInUserId(getApplication()))
    }
}