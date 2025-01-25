package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.notes.common.categories.CategoryBox
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun RowCategories(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    noteViewModel: NoteViewModel
) {
    val categories by noteViewModel.categories.collectAsState(initial = emptyList())

    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value


    LazyRow (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = themeColors.backGround1)
    ) {

        // Agregar la opción "All"
        item {
            CategoryBox(
                category = CategoryEntity(name = "All", bgColor = "backGround1"),
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
                categoryName = "All"
            )
        }

        if (categories.isNotEmpty()) {
            // Agregar las demás categorías
            items(categories)  { category ->
                val categoryName = category.name

                CategoryBox(
                    category = category,
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected,
                    categoryName = categoryName
                )
            }
        }
    }
}