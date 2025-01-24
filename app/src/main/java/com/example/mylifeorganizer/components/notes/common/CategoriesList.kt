package com.example.mylifeorganizer.components.notes.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.CategoryWithNotes
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun CategoriesList(
    categoriesWithNotes: List<CategoryWithNotes>,
    selectedCategories: List<CategoryEntity>,
    onCategoryClick: (CategoryEntity, Boolean) -> Unit // Callback para manejar clics en categorías
) {
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value

    Text(
        text = "Available Categories",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = themeColors.text1,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    if (categoriesWithNotes.isNotEmpty()) {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(categoriesWithNotes) { categoryWithNotes ->
                val category = categoryWithNotes.category
                val isSelected = selectedCategories.contains(category)

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .background(themeViewModel.getCategoryColor(category.bgColor))
                        .clickable { onCategoryClick(category, isSelected) }
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.name,
                        color = themeColors.text1,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    } else {
        // Aquí puedes manejar el caso en que no haya categorías, por ejemplo mostrando un mensaje
        Text(
            text = "No categories available",
            color = themeColors.text1,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}