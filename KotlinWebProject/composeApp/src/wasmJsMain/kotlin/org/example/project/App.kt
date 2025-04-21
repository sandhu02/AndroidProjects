package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import kotlinwebproject.composeapp.generated.resources.Res
import kotlinwebproject.composeapp.generated.resources.compose_multiplatform
import kotlinwebproject.composeapp.generated.resources.mars

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
    var screenCount by remember {mutableStateOf(0) }

    Surface (modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(Res.drawable.mars) , contentDescription = null , contentScale = ContentScale.FillWidth)
        Column (modifier = Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {

            var cardColor = Color(32, 179, 91,155)
            if (screenCount == 1) {
                cardColor = Color(27, 91, 150,155)
            }
            else if (screenCount == 2) {
                cardColor = Color(95, 32, 122,155)
            }
            else if (screenCount == 3){
                cardColor = Color(0,0,0,0)
            }

            Card (
                backgroundColor = cardColor ,
                shape = RoundedCornerShape(35.dp) ,
                elevation = 10.dp,
                onClick = {
                    if (screenCount == 0) {
                        screenCount = 1
                    } else if (screenCount == 1){
                        screenCount = 2
                    }
                    else if (screenCount == 2) {
                        screenCount = 3
                    }
                    else if (screenCount == 3) {
                        screenCount = 0
                    }
                }
            ) {
                Text ("The Kotlin Multiplatform plugin helps you develop applications that work on both Android and iOS. \n" +
                        "With the Kotlin Multiplatform plugin for Android Studio, you can:\n" +
                        "1. Write business logic just once and share the code on both platforms.\n" +
                        "2. Run and debug the iOS part of your application on iOS targets straight from Android Studio.\n" +
                        "3. Quickly create a new multiplatform project, or add a multiplatform module into an existing one.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    textAlign = TextAlign.Justify
                )

            }
        }

    }

    }
}