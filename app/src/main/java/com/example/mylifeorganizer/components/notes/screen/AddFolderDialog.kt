package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddFolderDialog(
    onDismiss: () -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel

    val isLangEng = appViewModel.isLangEng.value

    var folderName by remember { mutableStateOf("") }
    val idFolderForAddingSubFolder = appViewModel.idFolderForAddingSubFolder.value

    AlertDialogWindow(
        title = if(isLangEng) "Create New Folder" else "Crear Nueva Carpeta",
        confirmButtonText = if(isLangEng) "Add Folder" else "Agregar Carpeta",
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
        dismissButtonText = if(isLangEng) "Cancel" else "Cancelar",
        onDismiss = { onDismiss() },
        isConfirmButtonEnabled = folderName.isNotBlank(),
        textFieldValue = folderName,
        textFieldOnValueChange = { folderName = it },
        textFieldLabel = if(isLangEng) "Folder Name" else "Nombre de la Carpeta"
    )

}