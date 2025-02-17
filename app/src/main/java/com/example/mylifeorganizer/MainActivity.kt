package com.example.mylifeorganizer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.screens.MainScreen
import com.example.mylifeorganizer.ui.theme.MyLifeOrganizerTheme
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.github.mikephil.charting.utils.Utils
import com.example.mylifeorganizer.worker.scheduleDailyTaskWorker
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        AndroidThreeTen.init(this)
        scheduleDailyTaskWorker(this)
        Utils.init(this)
        enableEdgeToEdge()

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) {}
        }

        setContent {
            MyLifeOrganizerTheme {
                val themeViewModel: ThemeViewModel = viewModel()

                val themeColors = themeViewModel.themeColors.value
                val isThemeDark = themeViewModel.isThemeDark.value

                // Controlador del sistema
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = themeColors.backGround1,
                    darkIcons = !isThemeDark
                )

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}



