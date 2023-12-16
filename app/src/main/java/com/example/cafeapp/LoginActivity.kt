package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.Activites.Landing.LandingActivity
import com.example.cafeapp.Activites.Register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(LoginViewModel::class.java)

        username = findViewById(R.id.editTextUsername)
        password = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.btnLogin)
        registerButton = findViewById(R.id.btnRegister)

        loginButton.setOnClickListener { handleLoginButtonOnClick() }
        registerButton.setOnClickListener { handleRegisterButtonOnClick() }

        //If user has already signed in before, skip sign in
        if(viewModel.checkUserLoggedIn())
        {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }

    }

        private fun handleLoginButtonOnClick() {

            val editTextList = listOf(username, password)

            for (editText in editTextList) {
                if (editText.text.isEmpty()) {
                    editText.error = "Field cannot be empty! "
                    return
                }
            }
            val loginAttempt = viewModel.login(username.text.toString(),password.text.toString())
            if(loginAttempt == LoginViewModel.LoginErrorCodes.Success){
                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this,loginAttempt.toString(), Toast.LENGTH_SHORT).show()

            }
        }

    private fun handleRegisterButtonOnClick() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

}