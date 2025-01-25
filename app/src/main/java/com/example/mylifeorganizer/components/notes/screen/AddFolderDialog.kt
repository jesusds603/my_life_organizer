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
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun AddFolderDialog(
    onDismiss: () -> Unit,
    noteViewModel: NoteViewModel,
    folders: List<FolderEntity>
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value

    var folderName by remember { mutableStateOf("") }
    var selectedParentId by remember { mutableStateOf<Long?>(null) }
    val idFolderForAddingSubFolder = appViewModel.idFolderForAddingSubFolder.value

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    noteViewModel.addFolder(
                        FolderEntity(
                            folderId = System.currentTimeMillis(),
                            name = folderName,
                            parentFolderId = idFolderForAddingSubFolder
                        ),
                        onFolderAdded = { folderId ->
                            // Realiza acciones adicionales si es necesario
                            Log.d("AddFolder", "Folder added with ID: $folderId")
                        }
                    )
                    onDismiss()
                },
                enabled = folderName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text("Add Folder", color = themeColors.text1)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSecondary)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Create New Folder",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = themeColors.text1
                )

                TextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    label = { Text("Folder Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = themeColors.text1,
                        unfocusedTextColor = themeColors.text2,
                        focusedContainerColor = themeColors.backGround1,
                        unfocusedContainerColor = themeColors.backGround2,
                    )
                )

                if (folders.isNotEmpty()) {
                    Text(
                        text = "Select Parent Folder (Optional)",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = themeColors.text1
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(themeColors.backGround2)
                            .padding(8.dp)
                    ) {
                        items(folders) { folder ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedParentId = folder.folderId }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = folder.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f),
                                    color = themeColors.text1
                                )
                                if (selectedParentId == folder.folderId) {
                                    Text("✓", color = themeColors.categoriesNotes.green)
                                }
                            }
                        }
                    }
                }
            }
        },
        containerColor = themeColors.backGround4,
    )
}