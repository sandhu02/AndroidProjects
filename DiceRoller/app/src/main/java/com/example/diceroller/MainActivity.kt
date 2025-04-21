package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {

            }
        }
    }


    @Composable
    fun ArtSpaceApp(){
        Surface (color = MaterialTheme.colorScheme.background){
            var currentStep by remember { mutableIntStateOf(1) }
            when (currentStep){
                1 -> {
                    val modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)

                    Column (modifier = modifier , horizontalAlignment = Alignment.CenterHorizontally ){
                        Surface ( shadowElevation = 24.dp , modifier = Modifier.padding(16.dp)){
                            Image (painter = painterResource(id = R.drawable._0230314_144504), contentDescription = "PI Day" ,
                                modifier = Modifier
                                    .rotate(90f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Surface (modifier = Modifier.padding(16.dp)){
                            Column (modifier = Modifier
                                .background(color = Color(0xFFDEDEE3))
                                .padding(16.dp)
                                .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ){
                                Text(text = "Still life of Roses and other flowers" , fontSize = 30.sp , lineHeight = 28.sp)
                                Text(text = "Author (2021)" , fontWeight = FontWeight.Bold)
                            }
                        }


                        Spacer(modifier = Modifier.height(29.dp))
                        Row (horizontalArrangement = Arrangement.SpaceEvenly){
                            Button(onClick = {   } , colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111171))  ){
                                Text(text = "Previous")
                            }
                            Spacer(modifier = Modifier.width(100.dp))
                            Button(onClick = { currentStep = 2  } , colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111171))){
                                Text(text = "Next     ")
                            }

                        }

                    }
                }
                2 -> {
                    val modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)

                    Column (modifier = modifier , horizontalAlignment = Alignment.CenterHorizontally ){
                        Surface ( shadowElevation = 24.dp , modifier = Modifier.padding(16.dp)){
                            Image (painter = painterResource(id = R.drawable.testandroid), contentDescription = "Test Image" ,
                                modifier = Modifier

                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Surface (modifier = Modifier.padding(16.dp)){
                            Column (modifier = Modifier
                                .background(color = Color(0xFFDEDEE3))
                                .padding(16.dp)
                                .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ){
                                Text(text = "A country road to sky" , fontSize = 30.sp , lineHeight = 28.sp)
                                Text(text = "ME (2023)" , fontWeight = FontWeight.Bold)
                            }
                        }


                        Spacer(modifier = Modifier.height(29.dp))
                        Row (horizontalArrangement = Arrangement.SpaceEvenly){
                            Button(onClick = { currentStep  } , colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111171))  ){
                                Text(text = "Previous")
                            }
                            Spacer(modifier = Modifier.width(100.dp))
                            Button(onClick = { currentStep = 1  } , colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111171))){
                                Text(text = "Next     ")
                            }

                        }

                    }
                }
            }

        }
    }
    @Composable
    fun FirstPage(modifier: Modifier = Modifier){

    }

    @Composable
    fun SecondPage(modifier: Modifier = Modifier){

    }

    @Preview
    @Composable
    fun ArtSpaceAppPreview(){
        DiceRollerTheme {
            ArtSpaceApp()
        }
    }
}

