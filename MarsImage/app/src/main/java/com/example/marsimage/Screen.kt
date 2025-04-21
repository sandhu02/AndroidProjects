package com.example.marsimage

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.marsimage.data.MarsPhoto
import com.example.marsimage.data.marslist
import org.jetbrains.annotations.Async
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil3.compose.AsyncImage

@Composable
fun mainScreen(marsviewModel: MarsViewModel = viewModel()) {

    val uiState by marsviewModel.uiState.collectAsState()

    Surface (modifier = Modifier.fillMaxSize()){
        photoListScreen(uiState.photos)
    }
}

@Composable
fun photoCard(photo: String) {
    Card (modifier = Modifier.padding(16.dp)) {
        Column {
            AsyncImage(model = photo, contentDescription = null)
        }
    }
}

@Composable
fun photoListScreen(list: List<MarsPhoto>) {
    Column {
        LazyColumn  {
            items(list) { item ->
                photoCard(item.img_src)
            }
        }
    }
}

@Preview
@Composable
fun screen_preview(){
    mainScreen()
}