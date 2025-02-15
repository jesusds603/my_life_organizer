package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.components.AlertDialogWindow
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FolderCard(
    folder: FolderEntity,
    depth: Int
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val expandedFolders = appViewModel.expandedFolders.value

    var showDialog by remember { mutableStateOf(false) }

    var showRenameDialog by remember { mutableStateOf(false) } // Controla si el diálogo de renombrar está visible
    var newName by remember { mutableStateOf("") } // Título temporal para renombrar
    var showDetailsDialog by remember { mutableStateOf(false) }

    var showAddSubfolderDialog by remember { mutableStateOf(false) }
    var showMoveDialog by remember { mutableStateOf(false) }

    var selectedFolderId by remember { mutableStateOf<Long?>(null) } // Para mover ruta

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

        // Card
        Row(
            modifier = Modifier
                .background(
                    color = themeColors.bgCardFolder,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            showDialog = true
                        }
                    )
                }
                .clickable {
                    appViewModel.changeExpandedFolders(
                        if (expandedFolders.contains(folder.folderId)) {
                            expandedFolders - folder.folderId
                        } else {
                            expandedFolders + folder.folderId
                        }
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f) // Esto asegura que el texto y el icono ocupen todo el espacio disponible
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_folder_24),
                    contentDescription = null,
                    tint = themeColors.text1,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(1.dp))
                Icon(
                    painter = if (expandedFolders.contains(folder.folderId))  {
                        painterResource(id = R.drawable.baseline_keyboard_arrow_down_24)
                    } else {
                        painterResource(id = R.drawable.baseline_keyboard_arrow_right_24)
                    },
                    contentDescription = null,
                    tint = themeColors.text1,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = folder.name,
                    color = themeColors.text1,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Manejo de texto largo
                )
            }

            Box {
                Icon(
                    painter = painterResource(R.drawable.baseline_more_vert_24),
                    contentDescription = null,
                    tint = themeColors.text1,
                    modifier = Modifier
                        .height(20.dp)
                        .width(32.dp)
                        .clickable {
                            showDialog = true
                        }
                )

                if (showDialog) {
                    FloatingOptionsFolder(
                        folder = folder,
                        showDialog = showDialog,
                        onShowDialog = { showDialog = it },
                        onShowDetailsDialog = { showDetailsDialog = it },
                        onShowAddSubfolderDialog = { showAddSubfolderDialog = it },
                        onShowRenameDialog = { showRenameDialog = it },
                        onNewName = { newName = it },
                        showMoveDialog = showMoveDialog,
                        onShowMoveDialog = { showMoveDialog = it }
                    )
                }
            }


            if (showRenameDialog) {
                AlertDialogWindow(
                    title = if(isLangEng) "Rename Folder" else "Renombrar Carpeta",
                    confirmButtonText = if(isLangEng) "Rename" else "Renombrar",
                    onConfirm = {
                        val updatedFolder = folder.copy(name = newName)
                        noteViewModel.updateFolder(updatedFolder)
                        showRenameDialog = false
                    },
                    dismissButtonText = if(isLangEng) "Cancel" else "Cancelar",
                    onDismiss = { showRenameDialog = false },
                    isConfirmButtonEnabled = newName.isNotBlank(),
                    textFieldValue = newName,
                    textFieldOnValueChange = { newName = it },
                    textFieldLabel = if(isLangEng) "New Name" else "Nuevo Nombre"
                )
            }

            if (showAddSubfolderDialog) {
                AlertDialogWindow(
                    title = if(isLangEng) "Add Subfolder" else "Añadir Subcarpeta",
                    confirmButtonText = if(isLangEng) "Add" else "Añadir",
                    onConfirm = {
                        val newSubfolder = FolderEntity(
                            folderId = System.currentTimeMillis(),
                            name = newName,
                            parentFolderId = folder.folderId,
                        )
                        noteViewModel.addFolder(
                            newSubfolder,
                            onFolderAdded = { folderId ->
                                Log.d(
                                    "AddSubfolder",
                                    "Subfolder added with ID: $folderId and parent ID: ${folder.folderId}"
                                )
                            }
                        )
                        showAddSubfolderDialog = false
                    },
                    dismissButtonText = if(isLangEng) "Cancel" else "Cancelar",
                    onDismiss = { showAddSubfolderDialog = false },
                    isConfirmButtonEnabled = newName.isNotBlank(),
                    textFieldValue = newName,
                    textFieldOnValueChange = { newName = it },
                    textFieldLabel = if (isLangEng) "Subfolder Name" else "Nombre de la Subcarpeta"
                )
            }

            if (showDetailsDialog) {
                DialogDetailsFolder(
                    onShowDetailsDialog = { showDetailsDialog = it },
                    folder = folder
                )
            }

            if(showMoveDialog) {
                MoveItemDialog(
                    selectedFolderId = selectedFolderId,
                    onFolderSelected = { selectedFolderId = it }, // Actualiza el folder seleccionado
                    changeShowMoveDialog = { showMoveDialog = it },
                    item = folder,
                    isMovingNote = false, // Indica que estamos moviendo una carpeta
                    onMoveItem = { newParentFolderId ->
                        // Actualiza la carpeta con el nuevo parentFolderId
                        noteViewModel.updateFolder(
                            folder = folder.copy(parentFolderId = newParentFolderId),
                        )
                    }
                )
            }
        }
    }
}
