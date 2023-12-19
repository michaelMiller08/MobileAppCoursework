package com.example.cafeapp.Models

data class ProductModel(
    val id: Int,
    var name: String,
    val price: Float,
    val image: ByteArray?,
    val available: Boolean
)

