package com.example.mylifeorganizer.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.components.notes.screen.NotesContainer
import com.example.mylifeorganizer.components.notes.screen.RowCategories
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun NotesScreen() {
    val appViewModel: AppViewModel = viewModel()

    val context = LocalContext.current
    val noteDB = NoteDB.getInstance(context)
    val notesRepository = NotesRepository(noteDB)
    val noteViewModel = NoteViewModel(notesRepository)

    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Estado para rastrear la categoría seleccionada
    var selectedCategory by remember { mutableStateOf("All") }

    // Estado para controlar la visibilidad del cuadro de diálogo
    var showDialog by remember { mutableStateOf(false) }
    var showAddFolderDialog by remember { mutableStateOf(false) }

    val verticalPaddingOptions = 16.dp
    val sizeTextButtons = 24.sp


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column (
            modifier = Modifier
                .fillMaxSize()

        ) {
            RowCategories(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category // Actualizar el valor al seleccionar una categoría
                },
                noteViewModel = noteViewModel
            )

            // Mostrar las notas correspondientes a la categoría seleccionada
            NotesContainer(
                selectedCategory = selectedCategory,
                noteViewModel = noteViewModel
            )
        }

        // Botón flotante
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Ubicación en la esquina inferior derecha
                .padding(16.dp),
            containerColor = themeColors.buttonAdd,
            shape = CircleShape
        ) {
            Text("+", fontSize = 24.sp, color = themeColors.text1)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {},
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
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
                                appViewModel.toggleAddingNote()
                                showDialog = false
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
                                showDialog = false
                                showAddFolderDialog = true
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

        if (showAddFolderDialog) {
            AddFolderDialog(
                onDismiss = { showAddFolderDialog = false },
                onFolderAdded = { name, parentId ->
                    noteViewModel.addFolder(
                        FolderEntity(
                            name = name,
                            parentFolderId = parentId
                        ),
                        onFolderAdded = { folderId ->
                            // Realiza acciones adicionales si es necesario
                            Log.d("AddFolder", "Folder added with ID: $folderId")
                        }
                    )
                },
                noteViewModel = noteViewModel,
                folders = emptyList()
            )
        }

    }
}



@Composable
fun AddFolderDialog(
    onDismiss: () -> Unit,
    onFolderAdded: (String, Long?) -> Unit,
    noteViewModel: NoteViewModel,
    folders: List<FolderEntity>
) {
    var folderName by remember { mutableStateOf("") }
    var selectedParentId by remember { mutableStateOf<Long?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    onFolderAdded(folderName, selectedParentId)
                    onDismiss()
                },
                enabled = folderName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Add Folder", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
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
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    label = { Text("Folder Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (folders.isNotEmpty()) {
                    Text(
                        text = "Select Parent Folder (Optional)",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
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
                                    modifier = Modifier.weight(1f)
                                )
                                if (selectedParentId == folder.folderId) {
                                    Text("✓", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}