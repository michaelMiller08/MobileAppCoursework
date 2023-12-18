package com.example.cafeapp.Activites.ManageProducts

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.ViewModels.ManageProductsViewModel

class ManageProductsActivity : AppCompatActivity() {
    private val viewModel: ManageProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)


        val adapter = ManageProductsAdapter(viewModel.getProducts())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


}