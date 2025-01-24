package com.example.mylifeorganizer.components.notes.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryColorPicker(
    newCategoryColor: MutableState<String>
) {
    val themeViewModel: ThemeViewModel = viewModel()


    // Lista de nombres de colores
    val namesColors = themeViewModel.namesColorCategories

    FlowRow(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Magenta, shape = RoundedCornerShape(4.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        namesColors.forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape) // Hacerlo circular
                    .background(themeViewModel.getCategoryColor(color)) // Fondo del botón con el color
                    .clickable { newCategoryColor.value = namesColors[index] } // Usamos el nombre del color
            )
        }
    }
}