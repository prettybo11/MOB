package com.example.collegeschedule.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun addFavorite(groupName: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.add(groupName)
        saveFavorites(favorites)
    }

    fun removeFavorite(groupName: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.remove(groupName)
        saveFavorites(favorites)
    }

    fun isFavorite(groupName: String): Boolean {
        return getFavorites().contains(groupName)
    }

    fun getFavorites(): Set<String> {
        val json = prefs.getString("favorites", "[]") ?: "[]"
        val type = object : TypeToken<Set<String>>() {}.type
        return gson.fromJson(json, type) ?: emptySet()
    }

    private fun saveFavorites(favorites: Set<String>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString("favorites", json).apply()
    }
}