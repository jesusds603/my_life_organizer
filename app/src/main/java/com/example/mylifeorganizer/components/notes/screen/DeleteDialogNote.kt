package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.NoteWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeleteDialogNote(
    changeShowDeleteDialog: (Boolean) -> Unit,
    noteWithCategories: NoteWithCategories?
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    AlertDialog(
        onDismissRequest = { changeShowDeleteDialog(false) },
        confirmButton = {
            Button(
                onClick = {
                    noteViewModel.deleteNote(noteWithCategories?.note!!)
                    changeShowDeleteDialog(false)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(
                    text = if (isLangEng) "Delete" else "Eliminar",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { changeShowDeleteDialog(false) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(
                    text = if (isLangEng) "Cancel" else "Cancelar",
                    color = themeColors.text1
                )
            }
        },
        text = {
            Text(
                text = if (isLangEng) "Are you sure you want to delete this note?" else "Estás seguro de eliminar esta nota?",
                color = themeColors.text1
            )
        },
        title = {
            Text(
                text = if (isLangEng) "Delete Note" else "Eliminar Nota",
                style = MaterialTheme.typography.titleLarge,
                color = themeColors.text1
            )
        },
        containerColor = themeColors.bgDialog
    )
}