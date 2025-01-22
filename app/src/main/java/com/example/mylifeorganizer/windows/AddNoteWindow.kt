package com.example.mylifeorganizer.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.addnewnote.AddOrCancelNote
import com.example.mylifeorganizer.components.addnewnote.CategoriesSection
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
    var newCategoryColor by remember { mutableStateOf("") }
    var showCategoryInput by remember { mutableStateOf(false) }
    var selectedCategories by remember { mutableStateOf<List<CategoryEntity>>(emptyList()) }

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
            CategoriesSection(
                noteViewModel,
                categoriesWithNotes,
                selectedCategories,
                onCategoryClick = { category, isSelected ->
                    selectedCategories = if (isSelected) {
                        selectedCategories.filter { it != category }
                    } else {
                        selectedCategories + category
                    }
                },
                newCategory = remember { mutableStateOf(newCategory) }, // Pasa el estado mutable directamente
                showCategoryInput = remember { mutableStateOf(showCategoryInput) }, // Igual aquí
                newCategoryColor = remember { mutableStateOf(newCategoryColor) }
            )


            // Botones para guardar y cancelar
            AddOrCancelNote(
                noteViewModel,
                selectedCategories,
                newTitle = newTitle,
                newContent = newContent
            )
        }
    }
}


