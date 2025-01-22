package com.example.mylifeorganizer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun NotesScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value


    val context = LocalContext.current
    val noteDB = NoteDB.getInstance(context)
    val notesRepository = NotesRepository(noteDB)
    val noteViewModel = NoteViewModel(notesRepository)

    val categoriesWithNotes by noteViewModel.categoriesWithNotes.collectAsState(initial = emptyList())
    val notesWithCategories by noteViewModel.notesWithCategories.collectAsState(initial = emptyList())

    // Estado para rastrear la categoría seleccionada
    var selectedCategory by remember { mutableStateOf("All") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        LazyRow {

            // Agregar la opción "All"
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { selectedCategory = "All" }
                        .background(
                            if (selectedCategory == "All") themeColors.tabButtonSelected else themeColors.tabButtonDefault,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = "All",
                        color = if (selectedCategory == "All") themeColors.text1 else themeColors.text3
                    )
                }
            }

            if (categoriesWithNotes.isNotEmpty()) {

                // Agregar las demás categorías
                items(categoriesWithNotes)  { categoryWithNotes ->
                    val categoryName = categoryWithNotes.category.name

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { selectedCategory = categoryName }
                            .background(
                                if (selectedCategory == categoryName) themeColors.tabButtonSelected else themeColors.tabButtonDefault,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = categoryName,
                            color = if (selectedCategory == categoryName) themeColors.text1 else themeColors.text3
                        )
                    }
                }
            }
        }

        // Mostrar las notas correspondientes a la categoría seleccionada
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val notesToShow = if (selectedCategory == "All") {
                notesWithCategories.map { it.note }
            } else {
                val selectedCategoryNotes = categoriesWithNotes.find { it.category.name == selectedCategory }
                selectedCategoryNotes?.notes ?: emptyList()
            }

            items(notesToShow) { note ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(themeColors.backGround3, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(text = "ID: ${note.noteId}", color = themeColors.text1)
                    Text(text = "Title: ${note.title}", color = themeColors.text1)
                    Text(text = "Content: ${note.content}", color = themeColors.text1)
                    Text(text = "Created At: ${note.createdAt}", color = themeColors.text1)
                    Text(text = "Updated At: ${note.updatedAt}", color = themeColors.text1)
                }
            }
        }
    }
}