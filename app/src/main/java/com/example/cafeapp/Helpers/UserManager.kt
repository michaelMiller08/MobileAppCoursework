package com.example.cafeapp.Helpers

import android.content.Context

object UserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_ROLE = "user_role"

    fun saveLoggedInUser(userId: Int, role: UserRole, context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_USER_ROLE, role.toString())
        editor.apply()
    }

    fun getLoggedInUser(context: Context): Pair<Int, UserRole> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userId = prefs.getInt(KEY_USER_ID, -1)
        val roleStr = prefs.getString(KEY_USER_ROLE, null)

        if (userId != -1 && roleStr != null) {
            val role = UserRole.valueOf(roleStr)
            return Pair(userId, role)
        }
        else{
            return Pair(-1, UserRole.Customer)
        }
    }


    fun getLoggedInUserId(context: Context) : Int{
        return getLoggedInUser(context).first
    }
    fun getLoggedInUserRole(context: Context) : UserRole{
        return getLoggedInUser(context).second
    }

    fun clearLoggedInUser(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}

enum class UserRole{
    Customer,
    Admin
}