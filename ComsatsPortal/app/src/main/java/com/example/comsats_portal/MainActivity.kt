package com.example.comsats_portal

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.comsats_portal.ui.theme.ComsatsPortalTheme

class MainActivity : ComponentActivity() {
    private val webViewHolder = WebViewHolder()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onBackPressed = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val webView = webViewHolder.webView
                if (webView != null && webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish() // exit app if no more web history
                }
            }
        }
        // Handle back press for WebView
        onBackPressedDispatcher.addCallback(this , onBackPressed)

        setContent {
            ComsatsPortalTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "COMSATS Student Portal" ,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            colors = TopAppBarColors(
                                containerColor = Color(0xFF4B0082),
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White,
                                actionIconContentColor = Color.White,
                                scrolledContainerColor = Color(0xFF370061)
                            )
                        )
                    }
                ){ innerPadding ->
                    WebPageViewer(
                        modifier = Modifier.padding(innerPadding),
                        holder = webViewHolder
                    )
                }
            }
        }
    }
}

