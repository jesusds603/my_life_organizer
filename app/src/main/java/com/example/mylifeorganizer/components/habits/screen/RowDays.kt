package com.example.mylifeorganizer.components.habits.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun RowDays() {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val days = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        days.forEach { day ->
            Text(
                text = day,
                color = themeColors.text1,
                modifier = Modifier.weight(1f), // Distribuye el ancho equitativamente
                textAlign = TextAlign.Center, // Centra el texto
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}