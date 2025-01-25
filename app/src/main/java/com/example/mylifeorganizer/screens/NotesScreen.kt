package com.example.mylifeorganizer.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.notes.screen.AddFolderDialog
import com.example.mylifeorganizer.components.notes.screen.DialogOptionsAdd
import com.example.mylifeorganizer.components.notes.screen.NotesFoldersContainer
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
            NotesFoldersContainer(
                selectedCategory = selectedCategory,
                noteViewModel = noteViewModel
            )
        }

        // Botón flotante para agregar nuevas notas o folders a nivel raíz
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
            DialogOptionsAdd(
                onShowDialog = { showDialog = it },
                onShowAddFolderDialog = {
                    showAddFolderDialog = it
                    appViewModel.changeIdFolderForAddingSubFolder(0)
                }
            )
        }

        if (showAddFolderDialog) {
            AddFolderDialog(
                onDismiss = { showAddFolderDialog = false },
                noteViewModel = noteViewModel,
            )
        }

    }
}

