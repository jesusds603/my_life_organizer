package com.example.mylifeorganizer.ads

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

// test:        ca-app-pub-3940256099942544/9214589741
// Mi banner:   ca-app-pub-6007960646352251/2206415414
@Composable
fun BannerAd(context: Context) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = {
            AdView(context).apply {
                setAdSize(AdSize.FULL_BANNER)
                adUnitId = "ca-app-pub-6007960646352251/2206415414" // Reemplaza con tu ID real
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}