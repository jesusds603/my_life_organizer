package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun DialogDetailsFolder(
    noteViewModel: NoteViewModel,
    onShowDetailsDialog: (Boolean) -> Unit,
    folder: FolderEntity
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    AlertDialog(
        onDismissRequest = { onShowDetailsDialog(false) },
        confirmButton = {
            Button(
                onClick = { onShowDetailsDialog(false) },
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
            var numSubfolders by remember { mutableStateOf(0) }
            var numNotes by remember { mutableStateOf(0) }
            var totalSubFolders by remember { mutableStateOf(0) }
            var totalNotes by remember { mutableStateOf(0) }

            // Llama al método para contar subcarpetas y notas
            LaunchedEffect(folder.folderId) {
                noteViewModel.countSubfoldersAndNotes(folder.folderId) { subfolderCount, noteCount ->
                    numSubfolders = subfolderCount
                    numNotes = noteCount
                }
                noteViewModel.countTotalSubfoldersAndNotes(folder.folderId) { subfolderCount, noteCount ->
                    totalSubFolders = subfolderCount
                    totalNotes = noteCount
                }
            }

            Column (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Name: ${folder.name}",
                    color = themeColors.text1
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "$numSubfolders subfolders in top level",
                    color = themeColors.text1
                )
                Text(
                    text = "$totalSubFolders subfolders in total",
                    color = themeColors.text1
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "$numNotes subnotes in top level",
                    color = themeColors.text1
                )
                Text(
                    text = "$totalNotes subnotes in total",
                    color = themeColors.text1
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "Created at: ${formatDate(folder.createdAt ?: 0)}",
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