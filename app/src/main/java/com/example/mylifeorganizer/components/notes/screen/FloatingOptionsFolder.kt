package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    showMoveDialog: Boolean,
    onShowMoveDialog: (Boolean) -> Unit
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    DropdownMenu(
        expanded = showDialog,
        onDismissRequest = { onShowDialog(false) },
        modifier = Modifier
            .background(themeColors.bgDialog)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Add Note" else "Añadir Nota",
                    color = themeColors.text1
                )
            },
            onClick = {
                appViewModel.changeIdFolderForAddingNote(folder.folderId)
                appViewModel.toggleAddingNote()
                onShowDialog(false)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.size(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Add Subfolder" else "Añadir Subcarpeta",
                    color = themeColors.text1
                )
            },
            onClick = {
                onShowDialog(false)
                onShowAddSubfolderDialog(true)
                appViewModel.changeIdFolderForAddingSubFolder(folder.folderId)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.size(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Rename Folder" else "Renombrar Carpeta",
                    color = themeColors.text1
                )
            },
            onClick = {
                onNewName(folder.name)
                onShowDialog(false)
                onShowRenameDialog(true)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.size(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Move Folder" else "Mover Carpeta",
                    color = themeColors.text1
                )
            },
            onClick = {
                onShowMoveDialog(true)
                onShowDialog(false)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.size(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Delete Folder" else "Borrar Carpeta",
                    color = themeColors.text1
                )
            },
            onClick = {
                noteViewModel.deleteFolder(folder)
                onShowDialog(false)
            },
            modifier = Modifier.background(themeColors.backGround1),
        )

        Spacer(modifier = Modifier.size(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Details" else "Detalles",
                    color = themeColors.text1
                )
            },
            onClick = {
                onShowDetailsDialog(true)
                onShowDialog(false)
            },
            modifier = Modifier.background(themeColors.backGround1),
        )

        Spacer(modifier = Modifier.size(8.dp))
    }
}