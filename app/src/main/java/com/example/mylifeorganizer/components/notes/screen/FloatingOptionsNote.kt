package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.AlertDialogWindow
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun FloatingOptionsNote(
    showMenu: Boolean = false,
    changeShowMenu: (Boolean) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value
    val noteId = appViewModel.selectedNoteId.value


    var showRenameDialog by remember { mutableStateOf(false) } // Controla si el diálogo de renombrar está visible
    var newTitle by remember { mutableStateOf("") } // Título temporal para renombrar
    var showDetailsDialog by remember { mutableStateOf(false) }

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
                appViewModel.changeSelectedNoteId(noteId)
                appViewModel.toggleShowingNote()
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
                noteViewModel.deleteNote(noteWithCategories?.note!!)
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
                showDetailsDialog = true
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )
    }

    if(showRenameDialog) {
        AlertDialogWindow(
            title = "Rename Note",
            confirmButtonText = "Save",
            onConfirm = {
                val updatedNote = noteWithCategories?.note?.copy(title = newTitle, updatedAt = System.currentTimeMillis())
                if (updatedNote != null) {
                    noteViewModel.updateNote(updatedNote) {
                        showRenameDialog = false
                    }
                }
            },
            dismissButtonText = "Cancel",
            onDismiss = { showRenameDialog = false },
            isConfirmButtonEnabled = newTitle.isNotBlank(),
            textFieldValue = newTitle,
            textFieldOnValueChange = { newTitle = it },
            textFieldLabel = "New Note Title"
        )
    }

    if(showDetailsDialog) {
        AlertDialog(
            onDismissRequest = { showDetailsDialog = false },
            confirmButton = {
                Button(
                    onClick = {showDetailsDialog = false},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColors.backGround1
                    )
                ) {
                    Text(
                        text = "OK"
                    )
                }
            },
            text = {
                Column (
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Title: ${noteWithCategories?.note?.title}",
                        color = themeColors.text1
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "${noteWithCategories?.note?.content?.length} characters",
                        color = themeColors.text1
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "${noteWithCategories?.note?.content?.split("\\s+".toRegex())?.filter { it.isNotBlank() }?.size ?: 0} words",
                        color = themeColors.text1
                    )
                   Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Categories: ${noteWithCategories?.categories?.joinToString(", ") { it.name }}",
                        color = themeColors.text1
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Created at: ${formatDate(noteWithCategories?.note?.createdAt ?: 0)}",
                        color = themeColors.text1
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Last modified at: ${formatDate(noteWithCategories?.note?.updatedAt ?: 0)}",
                        color = themeColors.text1
                    )
                }
            },
            title = {
                Text(
                    text = "Details",
                    style = MaterialTheme.typography.titleLarge,
                    color = themeColors.text1
                )
            },
            containerColor = themeColors.backGround2
        )
    }
}

