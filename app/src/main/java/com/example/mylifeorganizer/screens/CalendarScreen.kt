package com.example.mylifeorganizer.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.example.mylifeorganizer.utils.TextMarkdown

@Composable
fun CalendarScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val markdownContent = """
        # ¡Hola Mundo!
        Este es un ejemplo de **Markdown** en Jetpack Compose.

        - Elemento 1
        - Elemento 2
        - Elemento 3

        [Enlace a Google](https://www.google.com)
        
        $$\int_{a}^{b} f(x) dx$$
    """.trimIndent()

    Text(
        text = "Markdown",
        color = themeViewModel.themeColors.value.text1
    )

    TextMarkdown(markdown = markdownContent)

}