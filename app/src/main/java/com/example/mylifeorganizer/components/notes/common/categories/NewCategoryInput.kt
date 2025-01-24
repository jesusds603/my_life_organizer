package com.example.mylifeorganizer.components.notes.common.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun NewCategoryInput(
    noteViewModel: NoteViewModel,
    newCategory: MutableState<String>,
    showCategoryInput: MutableState<Boolean>,
    newCategoryColor: MutableState<String>
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value

    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = newCategory.value,
            onValueChange = { newValue:String ->
                newCategory.value = newValue // Actualiza el valor mutable
            },
            placeholder = { Text("New Category") },
            modifier = Modifier.padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text2,
                focusedContainerColor = themeViewModel.getCategoryColor(newCategoryColor.value),
                unfocusedContainerColor = themeViewModel.getCategoryColor(newCategoryColor.value),
            )
        )

        TextButton(onClick = {
            // Crear la nueva categoría y agregarla
            val category = CategoryEntity(
                name = newCategory.value,
                bgColor = newCategoryColor.value
            )
            noteViewModel.addCategory(category)
            newCategory.value = ""  // Limpiar el campo de categoría después de crearla
            showCategoryInput.value = false  // Cerrar el campo de categoría
        }) {
            Text("Add", color = themeColors.text1)
        }
    }
}