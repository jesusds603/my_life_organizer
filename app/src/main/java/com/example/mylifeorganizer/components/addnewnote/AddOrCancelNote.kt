package com.example.mylifeorganizer.components.addnewnote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel


@Composable
fun AddOrCancelNote (
    noteViewModel: NoteViewModel,
    selectedCategories: List<CategoryEntity>,
    newTitle: String,
    newContent: String
) {
    val appViewModel: AppViewModel = viewModel()
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
            onClick = {
                // Agregar nota y manejar el ID generado
                noteViewModel.addNote(
                    note =  NoteEntity(
                        title = newTitle,
                        content = newContent
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
                appViewModel.toggleAddingNote()
            }
        ) {
            Text(text = "Save", color = Color.Green)
        }
    }
}