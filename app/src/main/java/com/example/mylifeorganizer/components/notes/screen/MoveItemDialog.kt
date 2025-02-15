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
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoveItemDialog(
    selectedFolderId: Long?, // Recibe el folder seleccionado desde fuera
    onFolderSelected: (Long) -> Unit, // Callback para actualizar el folder seleccionado
    changeShowMoveDialog: (Boolean) -> Unit,
    item: Any?, // Puede ser una nota o una carpeta
    isMovingNote: Boolean, // Indica si se está moviendo una nota o una carpeta
    onMoveItem: (Long) -> Unit // Función para manejar la actualización del item
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value
    val folders by noteViewModel.folders.collectAsState(initial = emptyList())
    val selectedOrderingNotes = appViewModel.selectedOrderingNotes.value
    var expandedFolders by remember { mutableStateOf<Set<Long>>(emptySet()) }

    // Obtener el ID de la carpeta que se está moviendo (si es una carpeta)
    val movingFolderId = if (!isMovingNote && item is FolderEntity) item.folderId else null

    AlertDialog(
        onDismissRequest = { changeShowMoveDialog(false) },
        confirmButton = {
            Button(
                onClick = {
                    changeShowMoveDialog(false)
                    if (selectedFolderId != null) {
                        onMoveItem(selectedFolderId!!) // Llamar a la función de actualización
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
                text = if (isMovingNote) {
                    if (isLangEng) "Move note to:" else "Mover nota a:"
                } else {
                    if (isLangEng) "Move folder to:" else "Mover carpeta a:"
                },
                color = themeColors.text1
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                fun displayFolders(parentId: Long, depth: Int) {
                    // Filtrar carpetas que no sean la carpeta que se está moviendo ni sus subcarpetas

                    val filteredFolders = folders.filter { folder ->
                        folder.parentFolderId == parentId && folder.folderId != movingFolderId
                    }

                    val sortedFolders = when (selectedOrderingNotes) {
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
                                    repeat(depth) { index ->
                                        VerticalLine(depth = index)
                                    }
                                }

                                // Folder Card
                                FolderCardForMove(
                                    folderId = folder.folderId,
                                    folderName = folder.name,
                                    isSelected = folder.folderId == selectedFolderId,
                                    isExpanded = expandedFolders.contains(folder.folderId),
                                    onFolderClicked = { onFolderSelected(it) }, // Actualiza el folder seleccionado
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
                        onFolderClicked = { onFolderSelected(it) }, // Actualiza el folder seleccionado
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