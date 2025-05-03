package com.example.cameratest

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.cameratest.ui.theme.CameraTestTheme
import androidx.compose.ui.res.imageResource
import androidx.core.graphics.createBitmap
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun bottomSheetContent(
    bitmaps : List<Bitmap> ,
    modifier: Modifier = Modifier,
) {
    val viewmodel: CameraPreviewViewmodel = viewModel()

    val context = LocalContext.current
    var imageScreenSwitch by remember { mutableStateOf(0) }
    var selectedImageIndex by remember { mutableStateOf(0) }
    val selectedImage = remember(selectedImageIndex, bitmaps) {
        if (bitmaps.isNotEmpty()) bitmaps[selectedImageIndex].asImageBitmap() else null
    }

    when (imageScreenSwitch) {
        0 ->
            if (bitmaps.isEmpty()){
                Box(
                    modifier = Modifier.padding(16.dp) ,
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Photos yet")
                }
            }
            else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp,
                    contentPadding = PaddingValues(16.dp),
                    modifier = modifier
                ) {
                    items(bitmaps.size) { index ->
                        val bitmap = bitmaps[index]
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedImageIndex = index
                                    imageScreenSwitch = 1
                                }
                        )
                    }
                }
            }

        1 ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = {
                        imageScreenSwitch = 0
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "go back",
                        modifier = Modifier.scale(1.5f)
                    )
                }
                selectedImage?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null
                    )
                } ?: Text("No image selected")
                Row (
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            if (selectedImage != null) {
                                viewmodel.saveImagetoStorage(
                                    context,
                                    selectedImage.asAndroidBitmap(),
                                    "my_photo_${System.currentTimeMillis()}"
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Save",
                            modifier = Modifier.scale(1.5f)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (selectedImage != null) {
                                viewmodel.shareImage(context , selectedImage)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "share",
                            modifier = Modifier.scale(1.5f)
                        )
                    }
                }
            }
    }

}
