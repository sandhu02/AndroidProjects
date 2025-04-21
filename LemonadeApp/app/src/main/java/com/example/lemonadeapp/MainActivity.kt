package com.example.lemonadeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonadeapp.ui.theme.LemonadeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonadeAppTheme {
                lemonadeApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun lemonadeApp(){
    var currentStep by remember { mutableStateOf(1) }
    var modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)

    Scaffold (topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Lemonade", fontWeight = FontWeight.Bold)}, colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) })
        { innerPadding ->
        Surface (color = MaterialTheme.colorScheme.background,modifier = modifier.padding(innerPadding)){
            when (currentStep) {
                1 -> {
                    Column (modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally){

                        Button(onClick = { currentStep = 2 } , shape = RoundedCornerShape(size = 64.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8ABF98)) ) {
                            Image(painter = painterResource(id = R.drawable.lemon_tree), contentDescription = stringResource( id = R.string.lemon_tree ) )
                        }
                        Spacer(modifier = Modifier.padding(16.dp))
                        Text(text = stringResource(id = R.string.tap_lemon_tree), fontSize = 18.sp)
                    }
                }

                2 -> {
                    Column (modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally){
                        var clickcount = 0
                        var ceiling = (2..4).random()

                        Button(onClick = { clickcount++
                            if (clickcount == ceiling){ currentStep = 3 }} ,
                            shape = RoundedCornerShape(size = 64.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8ABF98)) ) {

                            Image(painter = painterResource(id = R.drawable.lemon_squeeze), contentDescription = stringResource( id = R.string.lemon ) )
                        }
                        Spacer(modifier = Modifier.padding(16.dp))
                        Text(text = stringResource(id = R.string.squeeze_lemon), fontSize = 18.sp)
                    }
                }

                3 -> {
                    Column (modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally){

                        Button(onClick = { currentStep = 4 } , shape = RoundedCornerShape(size = 64.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8ABF98)) ) {
                            Image(painter = painterResource(id = R.drawable.lemon_drink), contentDescription = stringResource( id = R.string.lemonade_glass) )
                        }
                        Spacer(modifier = Modifier.padding(16.dp))
                        Text(text = stringResource(id = R.string.tap_lemonade_glass), fontSize = 18.sp)
                    }
                }

                4 -> {
                    Column (modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally){

                        Button(onClick = { currentStep = 1 } , shape = RoundedCornerShape(size = 64.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8ABF98)) ) {
                            Image(painter = painterResource(id = R.drawable.lemon_restart), contentDescription = stringResource( id = R.string.empty_glass ) )
                        }
                        Spacer(modifier = Modifier.padding(16.dp))
                        Text(text = stringResource(id = R.string.tap_empty_glass), fontSize = 18.sp)
                    }
                }
            }

        }
    }


}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LemonadeAppTheme {
        lemonadeApp()
    }
}