package com.example.cafeapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Helpers.UserManager
import com.example.cafeapp.Helpers.UserRole
import com.example.cafeapp.Models.OrderModel
import com.example.cafeapp.Models.PaymentModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val userManager: UserManager = UserManager

    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val defaultOrderStatus: String = "Preparing"


    fun createOrder(paymentType: String, paymentAmount: Float){
        val user = userManager.getLoggedInUser(getApplication())

        val order = OrderModel(0, user.first, LocalDate.now().toString(), LocalTime.now().format(timeFormatter), defaultOrderStatus)
        val payment = PaymentModel(0, order.orderId, paymentType , paymentAmount, LocalDate.now().toString())
        //var orderDetails = OrderDetailsModel(0, order.orderId, )
    }

    fun getCurrentUserRole(): UserRole{
        return userManager.getLoggedInUser(getApplication()).second
    }
}
