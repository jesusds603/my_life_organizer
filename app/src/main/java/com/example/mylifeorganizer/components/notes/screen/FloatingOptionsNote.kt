package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import androidx.annotation.RequiresApi
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FloatingOptionsNote(
    showMenu: Boolean = false,
    changeShowMenu: (Boolean) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val noteId = appViewModel.selectedNoteId.value

    var showRenameDialog by remember { mutableStateOf(false) } // Controla si el diálogo de renombrar está visible
    var newTitle by remember { mutableStateOf("") } // Título temporal para renombrar
    var showDetailsDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showMoveDialog by remember { mutableStateOf(false) }
    var selectedFolderId by remember { mutableStateOf<Long?>(null) } // Para mover


    val noteWithCategories by noteViewModel
        .getNoteWithCategoriesById(noteId ?: 1L)
        .collectAsState(initial = null)


    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { changeShowMenu(false) }, // Cierra el menú al hacer clic afuera
        modifier = Modifier
            .background(themeColors.bgDialog)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        DropdownMenuItem(
            text = { Text(
                text= if(isLangEng) "Edit" else "Editar",
                color = themeColors.text1
            ) },
            onClick = {
                appViewModel.changeSelectedNoteId(noteId)
                appViewModel.toggleShowingNote()
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        DropdownMenuItem(
            text = { Text(
                text= if(isLangEng) "Rename" else "Renombrar",
                color = themeColors.text1
            ) },
            onClick = {
                newTitle = noteWithCategories?.note?.title ?: ""
                changeShowMenu(false)
                showRenameDialog = true
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        DropdownMenuItem(
            text = { Text(
                text= if(isLangEng) "Move" else "Mover",
                color = themeColors.text1
            ) },
            onClick = {
                showMoveDialog = true
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        DropdownMenuItem(
            text = { Text(
                text = if(isLangEng) "Delete" else "Eliminar",
                color = themeColors.text1
            ) },
            onClick = {
                showDeleteDialog = true
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        DropdownMenuItem(
            text = { Text(
                text= if(isLangEng) "Details" else "Detalles",
                color = themeColors.text1
            ) },
            onClick = {
                showDetailsDialog = true
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )
    }

    if(showRenameDialog) {
        AlertDialogWindow(
            title = if(isLangEng) "Rename Note" else "Renombrar Nota",
            confirmButtonText = if(isLangEng) "Save" else "Guardar",
            onConfirm = {
                val updatedNote = noteWithCategories?.note?.copy(title = newTitle, updatedAt = System.currentTimeMillis())
                if (updatedNote != null) {
                    noteViewModel.updateNote(updatedNote) {
                        showRenameDialog = false
                    }
                }
            },
            dismissButtonText = if(isLangEng) "Cancel" else "Cancelar",
            onDismiss = { showRenameDialog = false },
            isConfirmButtonEnabled = newTitle.isNotBlank(),
            textFieldValue = newTitle,
            textFieldOnValueChange = { newTitle = it },
            textFieldLabel = if(isLangEng) "New Title" else "Nuevo Título",
        )
    }

    if(showDetailsDialog) {
       DetailsDialogNote(
           changeShowDetailsDialog = { showDetailsDialog = it },
           noteWithCategories = noteWithCategories
       )
    }

    if(showDeleteDialog) {
        DeleteDialogNote(
            changeShowDeleteDialog = { showDeleteDialog = it },
            noteWithCategories = noteWithCategories
        )
    }

    if(showMoveDialog) {
        MoveItemDialog(
            selectedFolderId = selectedFolderId,
            onFolderSelected = { selectedFolderId = it },
            changeShowMoveDialog = { showMoveDialog = it },
            item = noteWithCategories,
            isMovingNote = true,
            onMoveItem = {
                if (selectedFolderId != null) {
                    noteViewModel.updateNote(
                        note = noteWithCategories!!.note.copy(folderId = selectedFolderId!!),
                        onNoteUpdated = {}
                    )
                    showMoveDialog = false
                }
            }
        )
    }
}

