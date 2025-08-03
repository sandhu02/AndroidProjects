package com.example.cusotmauthtest.retrofit

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient

val cookieJar = object : CookieJar{
    private val cookieStore: HashMap<HttpUrl, List<Cookie>> = HashMap()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isEmpty()) {
            Log.d("Cookie", "No cookies received from $url")
        } else {
            cookieStore[url] = cookies
            for (cookie in cookies) {
                Log.d("Cookie", "Cookie from $url: ${cookie.name()} = ${cookie.value()}")
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url] ?: emptyList()
    }
}

val client = OkHttpClient.Builder()
    .cookieJar(cookieJar)
    .build()