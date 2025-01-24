package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
fun FloatingOptions(
    showMenu: Boolean = false,
    changeShowMenu: (Boolean) -> Unit,
    noteViewModel: NoteViewModel
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value
    val noteId = appViewModel.selectedNoteId.value


    var showRenameDialog by remember { mutableStateOf(false) } // Controla si el diálogo de renombrar está visible
    var newTitle by remember { mutableStateOf("") } // Título temporal para renombrar

    val noteWithCategories by noteViewModel
        .getNoteWithCategoriesById(noteId ?: 1L)
        .collectAsState(initial = null)


    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { changeShowMenu(false) }, // Cierra el menú al hacer clic afuera
        modifier = Modifier.background(themeColors.backGround1)
    ) {
        DropdownMenuItem(
            text = { Text(
                text= "Edit",
                color = themeColors.text1
            ) },
            onClick = {
                // Acción para Edit
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        DropdownMenuItem(
            text = { Text(
                text= "Rename",
                color = themeColors.text1
            ) },
            onClick = {
                newTitle = noteWithCategories?.note?.title ?: ""
                changeShowMenu(false)
                showRenameDialog = true
            },
            modifier = Modifier.background(themeColors.backGround3)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        DropdownMenuItem(
            text = { Text(
                text= "Delete",
                color = themeColors.text1
            ) },
            onClick = {
                // Acción para Edit
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        DropdownMenuItem(
            text = { Text(
                text= "Details",
                color = themeColors.text1
            ) },
            onClick = {
                // Acción para Edit
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )
    }

    if(showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename Note") },
            text = { TextField(
                value = newTitle,
                onValueChange = { newTitle = it }
            ) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val updatedNote = noteWithCategories?.note?.copy(title = newTitle, updatedAt = System.currentTimeMillis())
                        if (updatedNote != null) {
                            noteViewModel.updateNote(updatedNote) {
                                showRenameDialog = false
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

