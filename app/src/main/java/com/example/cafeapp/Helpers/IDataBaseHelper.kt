package com.example.cafeapp.Helpers

import com.example.cafeapp.Models.AdminModel
import com.example.cafeapp.Models.CustomerModel
import com.example.cafeapp.Models.OrderDetailsModel
import com.example.cafeapp.Models.OrderModel
import com.example.cafeapp.Models.PaymentModel
import com.example.cafeapp.Models.ProductModel


interface IDataBaseHelper {
    suspend fun addCustomerAsync(customer: CustomerModel): Int
    suspend fun addAdminAsync(admin: AdminModel): Int
    suspend fun addProductAsync(product: ProductModel)
    suspend fun addOrderAsync(order: OrderModel): Long
    suspend fun addPaymentAsync(paymentModel: PaymentModel)
    suspend fun addOrderDetailsAsync(orderDetailsModel: OrderDetailsModel): Long

    fun getCustomer(customer: CustomerModel): Int
    fun getAdmin(admin: AdminModel): Int

    fun getAllProducts(): ArrayList<ProductModel>
    fun getProductById(productId: Int): ProductModel?

    fun editProduct(product: ProductModel)
    fun editProductImageById(productId: Int, image: ByteArray)
     fun checkAdminUsernameExists(admin: AdminModel): Int

     fun checkUserName(customer: CustomerModel): Int


    }