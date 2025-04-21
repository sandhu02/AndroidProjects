package com.example.amphibians.network

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.amphibians.data.Amphibian
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitApiInterface{
    @GET("amphibians")
    fun getAmphibians() : Call<List<Amphibian>>
}

val baseUrl = "https://android-kotlin-fun-mars-server.appspot.com/"

class RetrofitCall {
    val retrofit:Retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val requestUser:RetrofitApiInterface = retrofit.create(RetrofitApiInterface::class.java)
    var fetchedData by mutableStateOf<List<Amphibian>?>(null)

    var responseSuccess by mutableStateOf("not yet initialized")

    fun fetchData(){
        val retrofitData = requestUser.getAmphibians()
        retrofitData.enqueue(object : Callback<List<Amphibian>> {
            override fun onResponse(call:Call<List<Amphibian>>, response:retrofit2.Response<List<Amphibian>>) {
                if (response.isSuccessful){
                    fetchedData = response.body()
                }
                else {
                    fetchedData = null
                    responseSuccess = "Response Failure"
                }
            }

            override fun onFailure(call: Call<List<Amphibian>>, t: Throwable) {
                fetchedData = null
                responseSuccess = "Failed to fetch data"
            }
        })
    }
}