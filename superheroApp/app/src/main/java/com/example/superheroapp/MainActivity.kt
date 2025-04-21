package com.example.superheroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.superheroapp.ui.theme.SuperheroAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperheroAppTheme {
                Surface {
                    SuperheroApp()
                }
            }
        }
    }
}

@Composable
fun SuperheroApp(){
    Scaffold( topBar = {SuperheroTopBar()}  , modifier = Modifier.padding(top = 24.dp)) {  innerpadding ->
        ItemList(modifier = Modifier.padding(innerpadding))
    }
}

@Composable
fun SuperheroTopBar(){
    Row (horizontalArrangement = Arrangement.Center , modifier = Modifier
        .fillMaxWidth()){
        Text(text = stringResource(id = R.string.app_name) , style = MaterialTheme.typography.displayLarge , modifier = Modifier.padding(top = 40.dp , bottom = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SuperheroAppTheme (darkTheme = false){
        Surface {
            SuperheroApp()
        }

    }
}