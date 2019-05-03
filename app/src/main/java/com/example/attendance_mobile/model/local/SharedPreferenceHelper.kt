package com.example.attendance_mobile.model.local

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(context: Context) {
    private val PREF_FILE = "user_config"
    private var sharedPreferences : SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREF_FILE,0)
    }

    fun setSharedPreferenceString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setSharedPreferenceInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getSharedPreferenceString(key: String, defValue: String): String? {
        return sharedPreferences.getString(key, defValue)
    }

    fun getSharedPreferenceInt(key: String, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }
}