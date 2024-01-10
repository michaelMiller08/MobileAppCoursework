package com.example.cafeapp.Activites.ManageOrders

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.ViewModels.ManageOrdersViewModel

class ManageOrdersActivity : AppCompatActivity() {
    private val viewModel: ManageOrdersViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_orders)

        recyclerView = findViewById(R.id.manageOrdersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ManageOrdersAdapter(this, viewModel.getAllPreparingOrders())
    }
}