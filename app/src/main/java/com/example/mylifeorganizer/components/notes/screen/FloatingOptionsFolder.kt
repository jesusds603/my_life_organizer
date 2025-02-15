package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FloatingOptionsFolder(
    folder: FolderEntity,
    showDialog: Boolean,
    onShowDialog: (Boolean) -> Unit,
    onShowDetailsDialog: (Boolean) -> Unit,
    onShowAddSubfolderDialog: (Boolean) -> Unit,
    onShowRenameDialog: (Boolean) -> Unit,
    onNewName: (String) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    var showMoveDialog by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = showDialog,
        onDismissRequest = { onShowDialog(false) },
        modifier = Modifier.background(themeColors.backGround1)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "Add Note",
                    color = themeColors.text1
                )
            },
            onClick = {
                appViewModel.changeIdFolderForAddingNote(folder.folderId)
                appViewModel.toggleAddingNote()
                onShowDialog(false)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )

        Spacer(modifier = Modifier.size(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = "Add Subfolder",
                    color = themeColors.text1
                )
            },
            onClick = {
                onShowDialog(false)
                onShowAddSubfolderDialog(true)
                appViewModel.changeIdFolderForAddingSubFolder(folder.folderId)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )

        Spacer(modifier = Modifier.size(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = "Rename Folder",
                    color = themeColors.text1
                )
            },
            onClick = {
                onNewName(folder.name)
                onShowDialog(false)
                onShowRenameDialog(true)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )

        Spacer(modifier = Modifier.size(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = "Delete Folder",
                    color = themeColors.text1
                )
            },
            onClick = {
                noteViewModel.deleteFolder(folder)
                onShowDialog(false)
            },
            modifier = Modifier.background(themeColors.backGround3),
        )

        Spacer(modifier = Modifier.size(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = "Details",
                    color = themeColors.text1
                )
            },
            onClick = {
                onShowDetailsDialog(true)
                onShowDialog(false)
            },
            modifier = Modifier.background(themeColors.backGround3),
        )

        Spacer(modifier = Modifier.size(8.dp))
    }

    if(showMoveDialog) {

    }
}