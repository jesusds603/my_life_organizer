package com.example.mylifeorganizer.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.NoteDB

import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun AddNoteWindow(modifier: Modifier = Modifier) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value

    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }

    val context = LocalContext.current
    val noteDB = NoteDB.getInstance(context)
    val notesRepository = NotesRepository(noteDB)
    val noteViewModel = NoteViewModel(notesRepository)
    val categoriesWithNotes by noteViewModel.categoriesWithNotes.collectAsState(initial = emptyList())

    var newCategory by remember { mutableStateOf("") }
    var showCategoryInput by remember { mutableStateOf(false) }
    var selectedCategories by remember { mutableStateOf<List<CategoryEntity>>(emptyList()) }
    var selectedCategoryColors by remember { mutableStateOf<Map<Int, Color>>(emptyMap()) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(themeColors.backgroundTransparent1),
    ) {

        Column (
            modifier = Modifier.fillMaxWidth()
        ) {
            // Input para el título
            TextField(
                value = newTitle,
                onValueChange = { newTitle = it },
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
                value = newContent,
                onValueChange = { newContent = it},
                placeholder = { Text("Content", color = themeColors.text3) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding( bottom = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = themeColors.text1,
                    unfocusedTextColor = themeColors.text2,
                    focusedContainerColor = themeColors.backGround2,
                    unfocusedContainerColor = themeColors.backGround3,
                )
            )

            // Categorías
            Text(
                text = "Categories",
                fontSize = 18.sp,
                color = themeColors.text1,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(themeColors.backGround3)
                    .padding(8.dp)
            ) {
                Column {
                    if (categoriesWithNotes.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(100.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(categoriesWithNotes) { categoryWithNotes ->
                                val category = categoryWithNotes.category
                                val isSelected = selectedCategories.contains(category)

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (isSelected) Color.Green else Color.Red
                                        )
                                        .clickable {
                                            // Al seleccionar una categoría, cambiar su estado (seleccionada/no seleccionada)
                                            if (isSelected) {
                                                selectedCategories = selectedCategories.filter { it != category }
                                                // selectedCategoryColors = selectedCategoryColors - category.categoryId
                                            } else {
                                                selectedCategories = selectedCategories + category
                                                // selectedCategoryColors = selectedCategoryColors + (category.categoryId to Color(category.bgColorLight))
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = category.name,
                                        color = if (isSelected) Color.White else Color.Black,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Aquí puedes manejar el caso en que no haya categorías, por ejemplo mostrando un mensaje
                        Text(
                            text = "No categories available",
                            color = themeColors.text1,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }



                    // Input para añadir nueva categoría
                    if(showCategoryInput) {
                        TextField(
                            value = newCategory,
                            onValueChange = { newCategory = it },
                            placeholder = { Text("New Category") },
                            modifier = Modifier.padding(vertical = 8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = themeColors.text1,
                                unfocusedTextColor = themeColors.text2,
                                focusedContainerColor = themeColors.backGround2,
                                unfocusedContainerColor = themeColors.backGround3,
                            )
                        )
                        TextButton(onClick = {
                            // Crear la nueva categoría y agregarla
                            val category = CategoryEntity(
                                name = newCategory,
                                bgColorDark = "#7e097e",  // Ejemplo de color de fondo oscuro
                                bgColorLight = "#df18df"  // Ejemplo de color de fondo claro
                            )
                            noteViewModel.addCategory(category)
                            newCategory = ""  // Limpiar el campo de categoría después de crearla
                            showCategoryInput = false  // Cerrar el campo de categoría
                        }) {
                            Text("Add Category", color = themeColors.text1)
                        }
                    }

                    // Botón para mostrar el input de nueva categoría
                    IconButton(
                        onClick = { showCategoryInput = !showCategoryInput },
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

            // Botones para guardar y cancelar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { appViewModel.toggleAddingNote() }
                ) {
                    Text(text = "Cancel", color = Color.Red)
                }
                TextButton(
                    onClick = { /* Guardar la nota */ }
                ) {
                    Text(text = "Save", color = Color.Green)
                }
            }
        }


    }
}

fun hexToColor(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}