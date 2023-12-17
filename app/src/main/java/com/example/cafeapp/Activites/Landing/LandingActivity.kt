package com.example.cafeapp.Activites.Landing

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.LoginActivity
import com.example.cafeapp.LoginViewModel
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.R
import com.example.cafeapp.ViewModels.LandingViewModel
import com.example.cafeapp.databinding.ActivityLandingBinding


class LandingActivity : AppCompatActivity(), ProductOnClickListener {
    private lateinit var binding:  ActivityLandingBinding
    private lateinit var toggle:  ActionBarDrawerToggle
    private lateinit var viewModel: LandingViewModel
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[LandingViewModel::class.java]
        loginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[LoginViewModel::class.java]



        binding.recyclerView.apply{
            layoutManager = GridLayoutManager(applicationContext , 3)
            var x = viewModel.getProducts()?.toList();
           adapter = x?.let { CardAdapter(it, this@LandingActivity) };
        }




        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@LandingActivity,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            supportActionBar?.title = "Cafe App"

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toggle.isDrawerIndicatorEnabled = true

            navView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.firstItem -> {
                        Toast.makeText(this@LandingActivity, "first item clicked", Toast.LENGTH_SHORT).show()
                    }
                    R.id.secondItem -> {
                        Toast.makeText(this@LandingActivity, "second item clicked", Toast.LENGTH_SHORT).show()
                    }
                    R.id.btnLogout ->{
                        viewModel.logout()
                        val intent = Intent(this@LandingActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            }



        }



        toggle.syncState()


//        var cust = loginViewModel.getCurrentCustomer()
//        val textView: TextView = findViewById<TextView>(R.id.LandingText)
//        if (cust != null) {
//            textView.text = cust.username.toString()
//        } else {
//            textView.text = "NULL"
//        }
    }

    override fun onClick(productModel: ProductModel) {
        val intent = Intent(applicationContext, ProductDetailActivity::class.java)
        intent.putExtra("productExtra", productModel.id)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }


}