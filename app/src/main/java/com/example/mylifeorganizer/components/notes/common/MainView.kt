package com.example.mylifeorganizer.components.notes.common

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.components.notes.common.categories.CategoriesSection
import com.example.mylifeorganizer.components.notes.common.categories.CategoryBox
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.utils.MyMarkdownParserField
import com.example.mylifeorganizer.utils.TextMarkdown
import com.example.mylifeorganizer.viewmodel.AppViewModel
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
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val isVisualizingNote = appViewModel.isVisualizingNote.value

    var showCategoriesDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(themeColors.backgroundTransparent1)
            .padding(4.dp)
    ) {

        Column (
            modifier = modifier
                .fillMaxSize()
        ) {


            // Input para el título
            TextField(
                value = title,
                onValueChange = { onChangeTitle(it) },
                placeholder = { Text("Title", color = themeColors.text3) },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = themeColors.text1, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = themeColors.text1,
                    unfocusedTextColor = themeColors.text2,
                    focusedContainerColor = themeColors.backGround2,
                    unfocusedContainerColor = themeColors.backGround3,
                )
            )


            MyMarkdownParserField(
                modifier = Modifier.weight(1f),
                content = content,
                onChangeContent = { onChangeContent(it) }
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
            if (selectedCategories.isNotEmpty()) {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(selectedCategories.size) { index ->
                        val categoryName = selectedCategories[index].name
                        val categoryColor = selectedCategories[index].bgColor

                        CategoryBox(
                            category = selectedCategories[index],
                            selectedCategory = "",
                            onCategorySelected = {},
                            categoryName = categoryName
                        )
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

        // Botón flotante en la esquina superior derecha
        IconButton(
            onClick = { appViewModel.toggleIsVisualizingNote() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp) // Espaciado desde los bordes
        ) {
            Icon(
                painter = painterResource(
                    id = if (isVisualizingNote) {
                        R.drawable.baseline_colorize_24
                    } else {
                        R.drawable.baseline_remove_red_eye_24
                    }
                ),
                contentDescription = null,
                tint = Color.Magenta,
            )
        }
    }

}