package com.example.retrofit_intro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.retrofit_intro.ui.theme.Retrofit_introTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Retrofit_introTheme {
                Surface(modifier = Modifier.fillMaxSize()){
                    RetrofitApp()
                }
            }
        }
    }
}



@Composable
fun RetrofitApp(){
    val retrofitcall = remember { RetrofitCalls() }
    val fetchedData = retrofitcall.fetchedData
    LaunchedEffect(Unit) {
        retrofitcall.fetchData()
    }
    var screenControl by remember{mutableStateOf(1)}
    when (screenControl){
        1 -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { screenControl = 2 }) {
                    Text(text = "Fetch Data")
                }
            }
        }
        2 -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (fetchedData == null) {
                    Text(text = "Data is null")
                } else {
                    AsyncImage(
                        model = fetchedData.data?.avatar,
                        contentDescription = null
                    )
                    Text(text = "ID: ${fetchedData.data?.id}")
                    Text(text = "Email: ${fetchedData.data?.email}")
                    Text(text = "First Name: ${fetchedData.data?.firstName}")
                    Text(text = "Last Name: ${fetchedData.data?.lastName}")
                    Text(text = "url: ${fetchedData.support?.url}")
                    Text(text = "text: ${fetchedData.support?.text}")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Retrofit_introTheme{
        Surface (modifier = Modifier.fillMaxSize()){
            RetrofitApp()
        }
    }
}