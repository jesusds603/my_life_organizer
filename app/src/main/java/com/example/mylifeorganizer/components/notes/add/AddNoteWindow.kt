package com.example.mylifeorganizer.components.notes.add

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.notes.common.MainView
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddNoteWindow(
    modifier: Modifier = Modifier,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel


    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }

    val categories by noteViewModel.categories.collectAsState(initial = emptyList())

    val newCategory by remember { mutableStateOf("") }
    val newCategoryColor by remember { mutableStateOf("") }
    val showCategoryInput by remember { mutableStateOf(false) }
    var selectedCategories by remember { mutableStateOf<List<CategoryEntity>>(emptyList()) }

    val idFolderForAddingNote = appViewModel.idFolderForAddingNote.value


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
                    content = finalContent,
                    folderId = idFolderForAddingNote
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

    MainView(
        modifier = modifier,
        title = newTitle,
        onChangeTitle = { newTitle = it },
        content = newContent,
        onChangeContent = { newContent = it },
        categories = categories,
        selectedCategories = selectedCategories,
        onChangeSelectedCategories = { selectedCategories = it },
        newCategory = newCategory,
        showCategoryInput = showCategoryInput,
        newCategoryColor = newCategoryColor,
    )
}


