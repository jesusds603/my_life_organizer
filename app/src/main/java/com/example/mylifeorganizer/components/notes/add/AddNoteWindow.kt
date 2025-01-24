package com.example.mylifeorganizer.components.notes.add

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.notes.common.categories.CategoriesSection
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun AddNoteWindow(modifier: Modifier = Modifier) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value

    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }

    val context = LocalContext.current
    val noteDB = NoteDB.getInstance(context)
    val notesRepository = NotesRepository(noteDB)
    val noteViewModel = NoteViewModel(notesRepository)
    val categories by noteViewModel.categories.collectAsState(initial = emptyList())

    val newCategory by remember { mutableStateOf("") }
    val newCategoryColor by remember { mutableStateOf("") }
    val showCategoryInput by remember { mutableStateOf(false) }
    var selectedCategories by remember { mutableStateOf<List<CategoryEntity>>(emptyList()) }

    var showCategoriesDialog by remember { mutableStateOf(false) }

    BackHandler {
        if(newContent.isNotEmpty()) {
            val finalTitle = when {
                newTitle.isEmpty() && newContent.isNotEmpty() -> newContent.take(40)
                newTitle.isNotEmpty() -> newTitle
                else -> "" // En caso de que ambos esten vacíos no guardamos nada
            }

            val finalContent = if(newContent.isNotEmpty()) newContent else ""

            noteViewModel.addNote(
                note =  NoteEntity(
                    title = finalTitle,
                    content = finalContent
                ),
                onNoteAdded = { noteId ->
                    // Guardar las relaciones con las categorías seleccionadas
                    selectedCategories.forEach { categoryEntity ->
                        noteViewModel.linkNoteWithCategory(
                            noteId = noteId,
                            categoryId = categoryEntity.categoryId
                        )
                    }
                }
            )
        }
        appViewModel.toggleAddingNote()
    }


    Column (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val scrollState = rememberScrollState()

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
                            selectedCategories = if (isSelected) {
                                selectedCategories.filter { it != category }
                            } else {
                                selectedCategories + category
                            }
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


