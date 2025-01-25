package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun VerticalLine(
    depth: Int
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val verticalLinesColors = themeViewModel.themeColors.value.verticalLinesNotes

    Box(
        modifier = Modifier
            .padding(horizontal = 1.dp)
            .width(1.dp) // Ancho de la línea
            .fillMaxHeight()
            .background(
                color = if(depth < verticalLinesColors.size) verticalLinesColors[depth] else themeColors.text1
            )
    )
    Spacer(modifier = Modifier.width(4.dp)) // Espacio entre líneas
}