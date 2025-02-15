package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.NoteWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoveNoteDialog(
    changeShowMoveDialog: (Boolean) -> Unit,
    noteWithCategories: NoteWithCategories?
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value
    val folders by noteViewModel.folders.collectAsState(initial = emptyList())
    val selectedOrderingNotes = appViewModel.selectedOrderingNotes.value
    var expandedFolders by remember { mutableStateOf<Set<Long>>(emptySet()) }
    var selectedFolderId by remember { mutableStateOf<Long?>(null) }


    AlertDialog(
        onDismissRequest = { changeShowMoveDialog(false) },
        confirmButton = {
            Button(
                onClick = {
                    changeShowMoveDialog(false)
                    if (selectedFolderId != null) {
                        noteViewModel.updateNote(
                            note = noteWithCategories!!.note.copy(folderId = selectedFolderId!!),
                            onNoteUpdated = {}
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(
                    text = if (isLangEng) "Move" else "Mover",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { changeShowMoveDialog(false) },
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
        title = {
            Text(
                text = if (isLangEng) "Move note to:" else "Mover nota a:",
                color = themeColors.text1
            )
        },
        text = {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                fun displayFolders(parentId: Long, depth: Int) {
                    val filteredFolders = folders.filter { it.parentFolderId == parentId }

                    val sortedFolders = when(selectedOrderingNotes) {
                        "createdAscending" -> filteredFolders.sortedBy { it.createdAt }
                        "createdDescending" -> filteredFolders.sortedByDescending { it.createdAt }
                        "updatedAscending" -> filteredFolders.sortedBy { it.createdAt }
                        "updatedDescending" -> filteredFolders.sortedByDescending { it.createdAt }
                        "nameAscending" -> filteredFolders.sortedBy { it.name }
                        "nameDescending" -> filteredFolders.sortedByDescending { it.name }
                        else -> filteredFolders.sortedByDescending { it.createdAt }
                    }

                    sortedFolders.forEach { folder ->
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                    .padding(vertical = 1.dp),
                            ) {
                                // Lines
                                if (depth > 0) {
                                    // Líneas verticales a la izquierda
                                    repeat(depth) { index -> // `index` es el índice del ciclo
                                        VerticalLine(
                                            depth = index // Pasar el índice como argumento
                                        )
                                    }
                                }

                                // Folder Card
                                FolderCardForMove(
                                    folderId = folder.folderId,
                                    folderName = folder.name,
                                    isSelected = folder.folderId == selectedFolderId,
                                    isExpanded = expandedFolders.contains(folder.folderId),
                                    onFolderClicked = { selectedFolderId = it },
                                    onExpandClicked = {
                                        expandedFolders = if (expandedFolders.contains(it)) {
                                            expandedFolders - it
                                        } else {
                                            expandedFolders + it
                                        }
                                    }
                                )
                            }
                        }

                        // Mostrar subfolders si el folder actual está expandido
                        if (expandedFolders.contains(folder.folderId)) {

                            // Mostrar subcarpetas de forma recursiva
                            displayFolders(folder.folderId, depth + 1)
                        }
                    }
                }

                item {
                    // Root Folder Card
                    FolderCardForMove(
                        folderId = 0L,
                        folderName = if (isLangEng) "Root" else "Raíz",
                        isSelected = 0L == selectedFolderId,
                        isExpanded = expandedFolders.contains(0L),
                        onFolderClicked = { selectedFolderId = it },
                        onExpandClicked = {
                            expandedFolders = if (expandedFolders.contains(it)) {
                                expandedFolders - it
                            } else {
                                expandedFolders + it
                            }
                        }
                    )
                }

                displayFolders(parentId = 0L, depth = 0)
            }
        },
        containerColor = themeColors.bgDialog,
    )
}
