package com.example.cafeapp.Helpers

import android.content.Context

object UserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"

    fun saveLoggedInUser(userId: Int, context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(KEY_USER_ID, userId)
        editor.apply()
    }

    fun getLoggedInUserId(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun clearLoggedInUser(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}