package com.example.edtech.model

import android.net.Uri

data class Course(
    val title: String,
    val description: String,
    val author: String,
    val rating: Int,
    val imgUrl: String? = "",
    val vidUrl: String = ""
)
