package com.example.cafeapp.Models

data class OrderModel(
    val orderId: Int,
    val customerId: Int,
    val orderDate: String,
    val orderTime: String,
    val orderStatus: String
)
