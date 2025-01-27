package com.example.mylifeorganizer.components.notes.edit

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.notes.common.MainView
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel


@Composable
fun NoteWindow(
    modifier: Modifier = Modifier,
    noteViewModel: NoteViewModel
) {
    val appViewModel: AppViewModel = viewModel()


    val categories by noteViewModel.categories.collectAsState(initial = emptyList())
    val selectedNoteId = appViewModel.selectedNoteId.value
    // Observamos la nota seleccionada y sus categorías
    val noteWithCategories by noteViewModel
        .getNoteWithCategoriesById(selectedNoteId ?: 1L)
        .collectAsState(initial = null)

    val categoriesForNote by noteViewModel
        .getCategoriesByNote(selectedNoteId ?: 1L)
        .collectAsState(initial = emptyList())

    // Estados reactivos para el título y contenido
    val (titleState, originalTitle) = remember { mutableStateOf("") to mutableStateOf("") }
    val (contentState, originalContent) = remember { mutableStateOf("") to mutableStateOf("") }


    val newCategory by remember { mutableStateOf("") }
    val newCategoryColor by remember { mutableStateOf("") }
    val showCategoryInput by remember { mutableStateOf(false) }
    var selectedCategories by remember { mutableStateOf<List<CategoryEntity>>(emptyList()) }

    val idFolderForAddingNote = appViewModel.idFolderForAddingNote.value

    // Sincronizar el estado del título y contenido con la nota obtenida
    LaunchedEffect(noteWithCategories) {
        noteWithCategories?.note?.let { note ->
            titleState.value = note.title
            contentState.value = note.content
            originalTitle.value = note.title
            originalContent.value = note.content
        }
        selectedCategories = categoriesForNote
    }

    BackHandler {
        // Si el título está vacío, usamos los primeros 40 caracteres del contenido
        val adjustedTitle = if (titleState.value.isBlank()) {
            contentState.value.replace("\n", " ").take(40)
        } else {
            titleState.value
        }

        // Crear la nueva entidad de la nota
        val updatedNote = NoteEntity(
            noteId = noteWithCategories?.note?.noteId ?: 0L, // Asegurar que el ID esté presente
            title = adjustedTitle,
            content = contentState.value,
            updatedAt = System.currentTimeMillis(),
            isFavorite = noteWithCategories?.note?.isFavorite ?: false,
            isArchived = noteWithCategories?.note?.isArchived ?: false,
            createdAt = noteWithCategories?.note?.createdAt ?: System.currentTimeMillis(),
            folderId = idFolderForAddingNote
        )

        // Recopilar las categorías seleccionadas
        val selectedCategoryIds = selectedCategories.map { it.categoryId }

        // Llamar al metodo del ViewModel para actualizar la nota y las categorías
        if(titleState.value != originalTitle.value || contentState.value != originalContent.value) {
            noteViewModel.updateNoteWithCategories(updatedNote, selectedCategoryIds)
        }

        appViewModel.toggleShowingNote()
        appViewModel.changeIdFolderForAddingNote(0)
    }

    // Mostramos toda la información de la nota
    noteWithCategories?.let { note ->

        MainView(
            modifier = modifier,
            title = titleState.value,
            onChangeTitle = { newValue ->
                titleState.value = newValue
            },
            content = contentState.value,
            onChangeContent = { newValue ->
                contentState.value = newValue
            },
            categories = categories,
            selectedCategories = selectedCategories,
            onChangeSelectedCategories = { selectedCategories = it },
            noteViewModel = noteViewModel,
            newCategory = newCategory,
            showCategoryInput = showCategoryInput,
            newCategoryColor = newCategoryColor,
        )
    }
}