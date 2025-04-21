package com.example.retrofit_intro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface UserRequest{
    @GET("/api/users/2") fun getUser() : Call<UserData>
}

class RetrofitCalls{
    val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://reqres.in/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val requestUser:UserRequest = retrofit.create(UserRequest::class.java)

    var fetchedData by mutableStateOf<UserData?>(null)

    var responseSuccess by mutableStateOf("not yet initialized")

    fun fetchData(){
        val retrofitData = requestUser.getUser()
        retrofitData.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: retrofit2.Response<UserData>) {
                if (response.isSuccessful) {
                    fetchedData = response.body()
                    responseSuccess = "response successfull"

                } else {
                    responseSuccess = "Error: ${response.errorBody()}"
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                println("Failed to fetch user data: ${t.message}")
            }
        })
    }

}