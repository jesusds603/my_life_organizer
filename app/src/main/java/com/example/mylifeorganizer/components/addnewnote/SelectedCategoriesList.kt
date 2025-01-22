package com.example.mylifeorganizer.components.addnewnote

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.CategoryWithNotes
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectedCategoriesList(
    categoriesWithNotes: List<CategoryWithNotes>,
    selectedCategories: List<CategoryEntity>,
    onCategoryClick: (CategoryEntity, Boolean) -> Unit
) {
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value

    if(selectedCategories.isNotEmpty()) {
        Text(
            text = "Selected Categories",
            fontSize = 18.sp,
            color = themeColors.text1,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        FlowRow (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.Magenta, shape = RoundedCornerShape(16.dp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            selectedCategories.forEach { selectedCategory ->
                val category = categoriesWithNotes.find { it.category == selectedCategory }?.category
                val isSelected = category != null

                Box(
                    modifier = Modifier
                        .background(themeViewModel.getCategoryColor(category?.bgColor ?: ""))
                        .clickable { onCategoryClick(selectedCategory, isSelected) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category?.name ?: "",
                        color = themeColors.text1,
                        fontSize = 18.sp
                    )
                }
            }
        }
    } else {
        // Aquí puedes manejar el caso en que no haya categorías, por ejemplo mostrando un mensaje
        Text(
            text = "You have not selected any categories",
            color = themeColors.text1,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}
