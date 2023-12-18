package com.example.cafeapp.Helpers


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class  BasketManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("BasketPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveBasketIds(ids: List<Int>) {
        val json = gson.toJson(ids)
        prefs.edit().putString("basketIds", json).apply()
    }

    fun getBasketIds(): List<Int> {
        val json = prefs.getString("basketIds", "")
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun addIdToBasket(newId: Int) {
        val existingIds = getBasketIds().toMutableList()
        existingIds.add(newId)
        saveBasketIds(existingIds)
    }

    fun removeIdFromBasket(id: Int){
        val existingIds = getBasketIds().toMutableList()
        existingIds.remove(id)
        saveBasketIds(existingIds)
    }

    fun clearBasket(){
        prefs.edit().clear().apply()
    }
}
