package com.example.cafeapp.Activites.Register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.LoginActivity
import com.example.cafeapp.LoginViewModel
import com.example.cafeapp.R
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var phoneNo: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[LoginViewModel::class.java]

        fullName = findViewById(R.id.editTextFullName)
        email = findViewById(R.id.editTextEmail)
        phoneNo = findViewById(R.id.editTextPhoneNumber)
        fullName = findViewById(R.id.editTextFullName)
        username = findViewById(R.id.editTextUsername)
        password = findViewById(R.id.editTextPassword)
        registerButton = findViewById(R.id.btnRegister)

        registerButton.setOnClickListener { registerCustomer() }
    }

    private fun registerCustomer() {
        val editTextList = listOf(fullName, email, phoneNo, username, password)

        for (editText in editTextList) {
            if (editText.text.isEmpty()) {
                editText.error = editText.hint.toString() + " required! "
                return
            }
        }

        lifecycleScope.launch {
            val result = viewModel.createCustomerAsync(
                fullName.text.toString(),
                email.text.toString(),
                phoneNo.text.toString(),
                username.text.toString(),
                password.text.toString()
            )

            when (result) {
                LoginViewModel.LoginErrorCodes.InvalidEmail -> email.error = "Invalid Email!"
                LoginViewModel.LoginErrorCodes.PasswordNotLongEnough -> password.error =
                    "Invalid Password!"

                LoginViewModel.LoginErrorCodes.InvalidPhoneNumber -> phoneNo.error =
                    "Invalid Phone Number!"

                LoginViewModel.LoginErrorCodes.UsernameAlreadyExists -> Toast.makeText(
                    this@RegisterActivity,
                    "Username already exists!",
                    Toast.LENGTH_SHORT
                ).show()

                LoginViewModel.LoginErrorCodes.DatabaseError -> Toast.makeText(
                    this@RegisterActivity,
                    "Database issue!",
                    Toast.LENGTH_SHORT
                ).show()

                LoginViewModel.LoginErrorCodes.Success -> {
                    Toast.makeText(this@RegisterActivity, "You can now login!", Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                }

                else -> {

                }
            }
        }

//            val result = viewModel.createCustomer(
//                fullName.text.toString(),
//                email.text.toString(),
//                phoneNo.text.toString(),
//                username.text.toString(),
//                password.text.toString()
//            )
//            when (result) {
//               LoginViewModel.LoginErrorCodes.InvalidEmail -> email.error = "Invalid Email!"
//                LoginViewModel.LoginErrorCodes.PasswordNotLongEnough -> password.error = "Invalid Password!"
//                LoginViewModel.LoginErrorCodes.InvalidPhoneNumber -> phoneNo.error = "Invalid Phone Number!"
//                LoginViewModel.LoginErrorCodes.UsernameAlreadyExists -> Toast.makeText(
//                    this,
//                    "Username already exists!",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                LoginViewModel.LoginErrorCodes.DatabaseError -> Toast.makeText(
//                    this,
//                    "Database issue!",
//                    Toast.LENGTH_SHORT
//                ).show()
//                LoginViewModel.LoginErrorCodes.Success -> {
//                    Toast.makeText(this, "You can now login!", Toast.LENGTH_LONG).show()
//
//                    val intent = Intent(this, LoginActivity::class.java)
//                    startActivity(intent)
//                }
//                else -> {
//
//                }
    }
}
















//    } private fun registerCustomer() {
//        val editTextList = listOf(fullName, email, phoneNo, username, password)
//
//        for (editText in editTextList) {
//            if (editText.text.isEmpty()) {
//                editText.error = editText.hint.toString() + " required! "
//                return
//            }
//        }
//            val result = viewModel.createCustomer(
//                fullName.text.toString(),
//                email.text.toString(),
//                phoneNo.text.toString(),
//                username.text.toString(),
//                password.text.toString()
//            )
//            when (result) {
//               LoginViewModel.LoginErrorCodes.InvalidEmail -> email.error = "Invalid Email!"
//                LoginViewModel.LoginErrorCodes.PasswordNotLongEnough -> password.error = "Invalid Password!"
//                LoginViewModel.LoginErrorCodes.InvalidPhoneNumber -> phoneNo.error = "Invalid Phone Number!"
//                LoginViewModel.LoginErrorCodes.UsernameAlreadyExists -> Toast.makeText(
//                    this,
//                    "Username already exists!",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                LoginViewModel.LoginErrorCodes.DatabaseError -> Toast.makeText(
//                    this,
//                    "Database issue!",
//                    Toast.LENGTH_SHORT
//                ).show()
//                LoginViewModel.LoginErrorCodes.Success -> {
//                    Toast.makeText(this, "You can now login!", Toast.LENGTH_LONG).show()
//
//                    val intent = Intent(this, LoginActivity::class.java)
//                    startActivity(intent)
//                }
//                else -> {
//
//                }
//            }
//    }

//         * return  1 : the new use has been add to the database successfully
//     * return -1 : Error, adding new user
//     * return -2 : can not Open/Create database
//     * return -3 : user name is already exist
