package com.example.myapphelloworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapphelloworld.ui.theme.MyAppHelloWorldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppHelloWorldTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    businessCard()
                }
            }
        }
    }
}

@Composable
fun businessCard(){
    Column (verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(color=Color(0xFF3ddc84))){


        Box (modifier = Modifier
            .padding(top = 150.dp, start = 70.dp, end = 70.dp)
            .background(color = Color(0xFF000d5F))){

            Image(painter = painterResource(id = R.drawable.android_logo), contentDescription = null)
        }

        Text(text = "MUHAMMAD AWAIS", fontWeight = FontWeight.Bold, fontSize = 30.sp, modifier = Modifier.padding(1.dp))

        Text(text = "Assembly Engineer", modifier = Modifier.padding(bottom = 100.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Box (contentAlignment = Alignment.TopCenter){
            Row (verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(id = R.drawable.phone_24dp) ,contentDescription = "phone", modifier = Modifier.scale(0.5f))
                Text(text = "+923038746502", fontSize = 10.sp)
            }
        }
        Box{
            Row (verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(id = R.drawable.share_24dp),contentDescription = null,modifier = Modifier.scale(0.5f))
                Text(text="instagram@codename", fontSize = 10.sp)
            }

        }
        Box (modifier = Modifier.padding(bottom=20.dp)){
            Row (verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(id = R.drawable.email_24dp),contentDescription = null,modifier = Modifier.scale(0.5f))
                Text(text="awaissandhu1026r@gmail.com", fontSize = 10.sp)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyAppHelloWorldTheme {
        businessCard()
    }
}