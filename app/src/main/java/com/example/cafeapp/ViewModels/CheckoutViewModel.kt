package com.example.cafeapp.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Helpers.UserManager
import com.example.cafeapp.Helpers.UserRole
import com.example.cafeapp.Models.OrderDetailsModel
import com.example.cafeapp.Models.OrderModel
import com.example.cafeapp.Models.PaymentModel
import com.example.cafeapp.Models.ProductModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val userManager: UserManager = UserManager

    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val defaultOrderStatus: String = "Preparing"


    fun createOrder(paymentType: String, paymentAmount: Float, products: List<ProductModel>) {
        val user = userManager.getLoggedInUser(getApplication())

        val order = OrderModel(0, user.first, LocalDate.now().toString(), LocalTime.now().format(timeFormatter), defaultOrderStatus)

        // Initialize orderId as -1
        var orderId = -1

        viewModelScope.launch {
            // Insert the order into the database and get the generated orderId
            orderId = db.addOrderAsync(order).toInt()

            // Update the payment entry with the generated orderId
            val payment = PaymentModel(0, orderId, paymentType, paymentAmount, LocalDate.now().toString())
            db.addPaymentAsync(payment)

            // Insert an OrderDetails entry for each product
            for (product in products) {
                val orderDetails = OrderDetailsModel(0, orderId, product.id)
                db.addOrderDetailsAsync(orderDetails)
            }
        }
    }



    fun getCurrentUserRole(): UserRole{
        return userManager.getLoggedInUser(getApplication()).second
    }
}
