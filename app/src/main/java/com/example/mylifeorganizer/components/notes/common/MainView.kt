package com.example.mylifeorganizer.components.notes.common

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.notes.common.categories.CategoriesSection
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun MainView(
    modifier: Modifier = Modifier,
    title: String,
    onChangeTitle: (String) -> Unit,
    content: String,
    onChangeContent: (String) -> Unit,
    categories: List<CategoryEntity>, // Todas las categorías disponibles
    selectedCategories: List<CategoryEntity>, // Categorías seleccionadas para agregar o eliminar
    onChangeSelectedCategories: (List<CategoryEntity>) -> Unit,
    noteViewModel: NoteViewModel,
    newCategory: String,
    showCategoryInput: Boolean,
    newCategoryColor: String,
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var showCategoriesDialog by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val scrollState = rememberScrollState()

        // Input para el título
        TextField(
            value = title,
            onValueChange = { onChangeTitle(it) },
            placeholder = { Text("Title", color = themeColors.text3) },
            modifier = Modifier
                .padding( bottom = 8.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text2,
                focusedContainerColor = themeColors.backGround2,
                unfocusedContainerColor = themeColors.backGround3,
            )
        )

        // Input para el contenido
        TextField(
            value = content,
            onValueChange = { onChangeContent(it) },
            placeholder = { Text("Content", color = themeColors.text3) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ocupar el espacio restante disponible
                .padding( bottom = 4.dp)
                .verticalScroll(scrollState),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text2,
                focusedContainerColor = themeColors.backGround2,
                unfocusedContainerColor = themeColors.backGround3,
            )
        )

        // Botón para agregar categorías
        Button(
            onClick = { showCategoriesDialog = true },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text("Agregar Categorías")
        }



        // Mostrar categorías seleccionadas
        if(selectedCategories.isNotEmpty()) {
            LazyRow (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items(selectedCategories.size) { index ->
                    val categoryName = selectedCategories[index].name
                    val categoryColor = selectedCategories[index].bgColor

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(color = themeViewModel.getCategoryColor(categoryColor))
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                    ) {
                        Text(
                            text = categoryName,
                            color = themeColors.text1
                        )
                    }
                }
            }
        }

        // Ventana flotante para categorías
        if (showCategoriesDialog) {
            Dialog(onDismissRequest = { showCategoriesDialog = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(themeColors.backgroundTransparent1)
                ) {
                    CategoriesSection(
                        noteViewModel,
                        categories,
                        selectedCategories,
                        onCategoryClick = { category, isSelected ->
                            onChangeSelectedCategories(
                                if (isSelected) {
                                    selectedCategories.filter { it != category }
                                } else {
                                    selectedCategories + category
                                }
                            )
                        },
                        newCategory = remember { mutableStateOf(newCategory) },
                        showCategoryInput = remember { mutableStateOf(showCategoryInput) },
                        newCategoryColor = remember { mutableStateOf(newCategoryColor) }
                    )
                }

                // Agregar BackHandler para cerrar el diálogo de categorías
                BackHandler {
                    showCategoriesDialog = false
                }
            }

        }
    }
}