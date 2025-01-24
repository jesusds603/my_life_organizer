package com.example.mylifeorganizer.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mylifeorganizer.components.notes.screen.NotesContainer
import com.example.mylifeorganizer.components.notes.screen.RowCategories
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.viewmodel.NoteViewModel


@Composable
fun NotesScreen() {

    val context = LocalContext.current
    val noteDB = NoteDB.getInstance(context)
    val notesRepository = NotesRepository(noteDB)
    val noteViewModel = NoteViewModel(notesRepository)

    val notesWithoutContentWithCategories by noteViewModel.notesWithoutContentWithCategories.collectAsState(initial = emptyList())
    val categories by noteViewModel.categories.collectAsState(initial = emptyList())

    // Estado para rastrear la categoría seleccionada
    var selectedCategory by remember { mutableStateOf("All") }

    Column (
        modifier = Modifier
            .fillMaxSize()

    ) {
        RowCategories(
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = category // Actualizar el valor al seleccionar una categoría
            },
            categories = categories
        )

        // Mostrar las notas correspondientes a la categoría seleccionada
        NotesContainer(
            selectedCategory = selectedCategory,
            notesWithoutContentWithCategories = notesWithoutContentWithCategories
        )
    }
}
