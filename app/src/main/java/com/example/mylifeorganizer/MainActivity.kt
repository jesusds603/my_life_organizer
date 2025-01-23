package com.example.mylifeorganizer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.screens.MainScreen
import com.example.mylifeorganizer.ui.theme.MyLifeOrganizerTheme
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyLifeOrganizerTheme {
                val themeViewModel: ThemeViewModel = viewModel()

                var themeColors = themeViewModel.themeColors.value
                var isThemeDark = themeViewModel.isThemeDark.value

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



