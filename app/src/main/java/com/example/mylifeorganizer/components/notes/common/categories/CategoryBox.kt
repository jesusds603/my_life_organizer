package com.example.mylifeorganizer.components.notes.common.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun CategoryBox(
    category: CategoryEntity,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categoryName: String
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val padding4 = 4.dp
    val padding2 = 2.dp
    val fontSize = 16.sp
    val cornerRadius = 4.dp
    val padding8 = 8.dp

    Box(
        modifier = Modifier
            .clickable { onCategorySelected(categoryName) }
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(horizontal = padding8)
            .drawWithContent {
                // Dibuja el contenido primero
                drawContent()

                // Si está seleccionada, dibuja un borde verde en la parte inferior
                if (selectedCategory == categoryName) {
                    drawLine(
                        color = Color.Green,
                        start = Offset(0f, size.height), // Comienza en el fondo de la Box
                        end = Offset(size.width, size.height), // Termina en el fondo de la Box
                        strokeWidth = 10f
                    )
                }
            }
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = themeViewModel.getCategoryColor(category.bgColor),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(vertical = padding2, horizontal = padding4), // Espaciado adicional dentro del fondo,
        ) {
            Text(
                text = categoryName,
                color = if (selectedCategory == categoryName) themeColors.text1 else themeColors.text3,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }

    }
}