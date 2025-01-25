package com.example.mylifeorganizer.components.notes.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.AlertDialogWindow
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun AddFolderDialog(
    onDismiss: () -> Unit,
    noteViewModel: NoteViewModel,
) {
    val appViewModel: AppViewModel = viewModel()
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