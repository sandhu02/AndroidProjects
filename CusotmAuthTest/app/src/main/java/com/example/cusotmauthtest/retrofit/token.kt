package com.example.cusotmauthtest.retrofit

import android.content.Context

fun saveToken(context: Context, token: String) {
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("jwt_token", token)
        apply()
    }
}

fun getToken(context: Context): String? {
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return sharedPref.getString("jwt_token", null)
}
