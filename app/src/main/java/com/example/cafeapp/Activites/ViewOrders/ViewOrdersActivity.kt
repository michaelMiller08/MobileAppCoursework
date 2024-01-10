package com.example.cafeapp.Activites.ViewOrders

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.ViewModels.ViewOrdersViewModel

class ViewOrdersActivity : AppCompatActivity() {

    private val viewModel: ViewOrdersViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)


        recyclerView = findViewById(R.id.ordersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = OrderAdapter(viewModel.getAllOrders())
    }

}