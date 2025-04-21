package com.example.gridapp.model
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


data class Topic (@StringRes val fieldName:Int, val count:Int, @DrawableRes val fieldPhoto:Int)