package com.example.gridapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gridapp.ui.theme.GridAppTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import com.example.gridapp.data.DataSource
import com.example.gridapp.model.Topic

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GridAppTheme {
                FieldsApp()
            }
        }
    }
}

@Composable
fun FieldsApp(){
    val layoutDirecton = LocalLayoutDirection.current
    Surface (modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(
            start = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateStartPadding(layoutDirecton),
            end = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateEndPadding(layoutDirecton)
        )
    ){
        FielsGrid(topicList = DataSource.topics )
    }

}

@Composable
fun FielsGrid(topicList: List<Topic>, modifier: Modifier = Modifier){
    LazyVerticalGrid (columns = GridCells.Fixed(2) ,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ){
        items(DataSource.topics){
            topic -> FieldCard(topic = topic)
        }
    }
}

@Composable
fun FieldCard(topic: Topic, modifier: Modifier = Modifier) {
    Card (modifier = Modifier){
        Row (modifier = modifier){
            Image(painter = painterResource(topic.fieldPhoto),
                contentDescription = stringResource(id = topic.fieldName)  ,
                modifier = Modifier.size(68.dp)
            )
            Column (modifier = modifier){
                Text(text = stringResource(id = topic.fieldName),modifier = Modifier.padding(start = 16.dp , end = 16.dp , top = 16.dp , bottom = 8.dp) , style = MaterialTheme.typography.bodyMedium)
                Row {
                    Image(painter = painterResource(id = R.drawable.ic_grain), contentDescription = "grain" , modifier = Modifier.padding(start = 16.dp))
                    Text(text = "${topic.count}" , modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GridAppTheme {
        FieldsApp()
    }
}