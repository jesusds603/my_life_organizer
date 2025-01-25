package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun DialogOptionsAdd(
    onShowDialog: (Boolean) -> Unit,
    onShowAddFolderDialog: (Boolean) -> Unit
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val verticalPaddingOptions = 16.dp
    val sizeTextButtons = 24.sp


    AlertDialog(
        onDismissRequest = { onShowDialog(false) },
        confirmButton = {},
        dismissButton = {
            Button(
                onClick = { onShowDialog(false) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(text = "Cancel", color = themeColors.text1)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Choose an option",
                    style = MaterialTheme.typography.titleLarge,
                    color = themeColors.text1,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Button Add Note
                TextButton(
                    onClick = {
                        appViewModel.changeIdFolderForAddingNote(0)
                        appViewModel.toggleAddingNote()
                        onShowDialog(false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = verticalPaddingOptions)
                        .clip(MaterialTheme.shapes.medium),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = themeColors.text1,
                        containerColor = themeColors.backGround2
                    )
                ) {
                    // Icono + texto "Add Note"
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_edit_note_24),
                            contentDescription = "Add Note",
                            modifier = Modifier.size(50.dp).padding(end = 8.dp) // Espaciado entre el icono y el texto
                        )
                        Text(
                            text = "Add Note",
                            fontSize = sizeTextButtons
                        )
                    }
                }
                TextButton(
                    onClick = {
                        onShowDialog(false)
                        onShowAddFolderDialog(true)
                        appViewModel.changeIdFolderForAddingSubFolder(0)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = verticalPaddingOptions)
                        .clip(MaterialTheme.shapes.medium),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = themeColors.text1,
                        containerColor = themeColors.backGround2
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_create_new_folder_24),
                            contentDescription = "Add Folder",
                            modifier = Modifier.size(50.dp).padding(end = 8.dp)

                        )
                        Text(
                            text = "Add Folder",
                            fontSize = sizeTextButtons
                        )
                    }
                }
            }
        },
        containerColor = themeColors.backGround3,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.large // Bordes más redondeados
    )
}