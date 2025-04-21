package com.example.amphibians

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.amphibians.data.Amphibian
import com.example.amphibians.network.RetrofitCall
import com.example.compose.AmphibiansTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmphibiansTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ){
                    AmphibianApp()
                }
            }
        }
    }
}

@Composable
fun AmphibianApp(){
    val retrofitcall = remember { RetrofitCall() }
    val fetchedData = retrofitcall.fetchedData
    LaunchedEffect(Unit) {
        retrofitcall.fetchData()
    }
    if (fetchedData != null) {
        Scaffold(topBar = {AmphibianTopBar()} , modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())) {
            innerPadding -> AmphibianListScreen(amphibians = fetchedData , modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun AmphibianTopBar(){
    Row (horizontalArrangement = Arrangement.Start , modifier = Modifier.padding(12.dp)){
        Text(text = stringResource(id = R.string.app_name) , style = MaterialTheme.typography.displayLarge)
    }
}

@Composable
fun AmphibianListScreen(amphibians:List<Amphibian?> , modifier: Modifier = Modifier){
    LazyColumn (modifier = modifier) {
        items(amphibians) {
            amphibian ->
            if (amphibian != null) {
                AmphibianCard(name = amphibian.name, type = amphibian.type, description = amphibian.description, photo = amphibian.imgSrc)
            }
        }
    }
}

@Composable
fun AmphibianCard(name:String , type:String , description:String , photo:String){
    Card(
        modifier = Modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(25.dp))
    ) {
        Column {
            Row {
                Text(
                    text = name ,
                    modifier = Modifier.padding(12.dp) ,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "($type)" ,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            AsyncImage(
                model = photo,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth() ,
                contentScale = ContentScale.Crop
            )
            Text(
                text = description,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
fun appPreview(){
    AmphibiansTheme {
        AmphibiansTheme {
            Surface(
                modifier = Modifier.fillMaxSize()
            ){
                AmphibianApp()
            }
        }
    }
}
