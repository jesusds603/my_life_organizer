package com.example.mylifeorganizer.components.notes.common.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun CategoriesSection(
    categories: List<CategoryEntity>,
    selectedCategories: List<CategoryEntity>,
    onCategoryClick: (CategoryEntity, Boolean) -> Unit, // Callback para manejar clics en categorías
    newCategory: MutableState<String>,
    showCategoryInput: MutableState<Boolean>,
    newCategoryColor: MutableState<String>
) {
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value


    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(themeColors.backGround3)
            .padding(8.dp)
    ) {

        Text(
            text = "Categories",
            fontSize = 18.sp,
            color = themeColors.text1,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Categorías seleccionadas
        SelectedCategoriesList(
            categories = categories,
            selectedCategories = selectedCategories,
            onCategoryClick = onCategoryClick
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Categorías disponibles
        CategoriesList(
            categories = categories,
            selectedCategories = selectedCategories,
            onCategoryClick = onCategoryClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input para añadir nueva categoría
        if(showCategoryInput.value) {
            NewCategoryInput(
                newCategory = newCategory,
                showCategoryInput = showCategoryInput,
                newCategoryColor = newCategoryColor
            )

            // Color Picker
            CategoryColorPicker(newCategoryColor)
        }

        // Botón para mostrar el input de nueva categoría
        IconButton(
            onClick = { showCategoryInput.value = !showCategoryInput.value },
            modifier = Modifier.align(Alignment.End),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = themeColors.backGround2,
                contentColor = themeColors.text1
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_box_24),
                contentDescription = "Add Category",
            )
        }
    }
}