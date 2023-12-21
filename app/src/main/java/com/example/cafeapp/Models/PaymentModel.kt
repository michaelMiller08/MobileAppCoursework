package com.example.cafeapp.Models

data class PaymentModel(
    val paymentId: Int,
    val orderId: Int,
    val paymentType: String,
    val paymentAmount: Float,
    val paymentDate: String
)

