package com.example.retrofit_intro

import com.google.gson.annotations.SerializedName

class UserData (
    @SerializedName("data") val data: Data?,
    @SerializedName("support") val support: Support?
)


class Data(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("avatar") val avatar: String
)


class Support(
    @SerializedName("url") val url: String,
    @SerializedName("text") val text: String
)
