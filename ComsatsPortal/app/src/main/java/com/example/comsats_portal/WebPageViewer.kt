package com.example.comsats_portal

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebPageViewer(modifier: Modifier,holder: WebViewHolder) {
    val url = "https://cms.comsats.edu.pk:8083/"

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true // enable JS if needed
                    settings.setSupportZoom(true)
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false // hide zoom UI buttons

                    loadUrl(url)
                    holder.webView = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}