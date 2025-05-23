package com.example.amphibians.data

import com.google.gson.annotations.SerializedName

data class Amphibian(
    @SerializedName("name") val name:String,
    @SerializedName("type") val type:String,
    @SerializedName("description") val description: String,
    @SerializedName("img_src") val imgSrc:String
)
