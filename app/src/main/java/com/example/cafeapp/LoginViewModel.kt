package com.example.cafeapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Helpers.UserManager
import com.example.cafeapp.Models.CustomerModel

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val customerListLive = MutableLiveData<List<CustomerModel>>()
    private val passwordLengthValidation: Int = 6
    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)


    fun checkUserLoggedIn(): Boolean {
        return UserManager.getLoggedInUserId(getApplication()) != -1
    }


    fun getCurrentCustomer(): CustomerModel? {
        return db.getCustomerById(UserManager.getLoggedInUserId(getApplication()))
    }

    fun login(username: String, password: String): LoginErrorCodes {
        val model = CustomerModel(0, "", "", "", username, password, true)
        return when (db.getCustomer(model)) {
            -1 -> {
                LoginErrorCodes.Error
            }

            else -> {
                UserManager.saveLoggedInUser(db.getCustomer(model), getApplication())

                LoginErrorCodes.Success

            }
        }

    }

    suspend fun createCustomerAsync(
        fullName: String,
        email: String,
        phoneNo: String,
        username: String,
        password: String
    ): LoginErrorCodes {
        //**Input Validation**//
        if (!isEmailValid(email)) {
            return LoginErrorCodes.InvalidEmail
        }
        if (phoneNo.length != 11 || !phoneNo.matches(Regex("[0-9]+"))
        ) {
            return LoginErrorCodes.InvalidPhoneNumber
        }
        if (password.length < passwordLengthValidation) {
            return LoginErrorCodes.PasswordNotLongEnough
        }

        //*********//


        //**Database validation**//
        val newCustomer = CustomerModel(-1, fullName, email, phoneNo, username, password, true)


        return try {
            when (db.addCustomerAsync(newCustomer)) {
                -1 -> LoginErrorCodes.Error
                -2 -> LoginErrorCodes.DatabaseError
                -3 -> LoginErrorCodes.UsernameAlreadyExists
                else -> LoginErrorCodes.Success
            }
        } catch (e: Exception) {
            LoginErrorCodes.Exception
        }
        //********//

    }

    fun createCustomer(
        fullName: String,
        email: String,
        phoneNo: String,
        username: String,
        password: String
    ): LoginErrorCodes {
        //**Input Validation**//
        if (!isEmailValid(email)) {
            return LoginErrorCodes.InvalidEmail
        }
        if (phoneNo.length != 11 || !phoneNo.matches(Regex("[0-9]+"))
        ) {
            return LoginErrorCodes.InvalidPhoneNumber
        }
        if (password.length < passwordLengthValidation) {
            return LoginErrorCodes.PasswordNotLongEnough
        }

        //*********//


        //**Database validation**//
        val newCustomer = CustomerModel(-1, fullName, email, phoneNo, username, password, true)


        var returnResult = LoginErrorCodes.Error
        val addCustomerResult: Int
        try {
            addCustomerResult = db.addCustomer(newCustomer)
        } catch (e: Exception) {
            return LoginErrorCodes.Exception
        }
        when (addCustomerResult) {
            1 -> {
                returnResult = LoginErrorCodes.Success

            }

            -1 -> {
                returnResult = LoginErrorCodes.Error
            }

            -2 -> {
                returnResult = LoginErrorCodes.DatabaseError
            }

            -3 -> {
                returnResult = LoginErrorCodes.UsernameAlreadyExists
            }

        }

        return returnResult
        //********//

    }


    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

    enum class LoginErrorCodes {
        InvalidEmail,
        InvalidPhoneNumber,
        PasswordNotLongEnough,
        Success,
        Exception,
        Error,
        DatabaseError,
        UsernameAlreadyExists,

    }
}