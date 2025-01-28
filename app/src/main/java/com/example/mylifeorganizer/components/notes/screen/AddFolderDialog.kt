package com.example.mylifeorganizer.components.notes.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.AlertDialogWindow
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun AddFolderDialog(
    onDismiss: () -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value

    var folderName by remember { mutableStateOf("") }
    val idFolderForAddingSubFolder = appViewModel.idFolderForAddingSubFolder.value

    AlertDialogWindow(
        title = "Create New Folder",
        confirmButtonText = "Add Folder",
        onConfirm = {
            noteViewModel.addFolder(
                FolderEntity(
                    folderId = System.currentTimeMillis(),
                    name = folderName,
                    parentFolderId = idFolderForAddingSubFolder
                ),
                onFolderAdded = { folderId ->
                    // Acciones adicionales
                    Log.d("AddFolder", "Folder added with ID: $folderId")
                }
            )
            onDismiss()
        },
        dismissButtonText = "Cancel",
        onDismiss = { onDismiss() },
        isConfirmButtonEnabled = folderName.isNotBlank(),
        textFieldValue = folderName,
        textFieldOnValueChange = { folderName = it },
        textFieldLabel = "Folder Name"
    )

}