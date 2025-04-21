package com.example.bookshelfapp.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen(){
    Surface(){
        Column (modifier = Modifier.fillMaxSize() ){
            LazyVerticalGrid(columns = GridCells.Fixed(2) , modifier = Modifier.padding(4.dp)) {
                items() { item ->

                }
            }
        }
    }
}

//@Composable
//fun