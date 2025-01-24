package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.room.FolderWithSubfolders
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.room.NoteWithoutContentWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun NotesContainer(
    selectedCategory: String,
    noteViewModel: NoteViewModel
) {
    val appViewModel: AppViewModel = viewModel()
    val notesWithoutContentWithCategories by noteViewModel.notesWithoutContentWithCategories.collectAsState(initial = emptyList())

    val folders by noteViewModel.folders.collectAsState(initial = emptyList())
    var selectedFolderId by remember { mutableStateOf<Long?>(null) }
    var notesInFolder by remember { mutableStateOf<List<NoteEntity>>(emptyList()) }
    var expandedFolders by remember { mutableStateOf<Set<Long>>(emptySet()) }

    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Función recursiva para mostrar carpetas y subcarpetas
        fun displayFolders(folders: List<FolderEntity>, parentId: Long?, depth: Int) {
            val filteredFolders = folders.filter { it.parentFolderId == parentId }

            filteredFolders.forEach { folder ->
                item {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = (16 * depth).dp, vertical = 8.dp)
                            .clickable {
                                expandedFolders = if(expandedFolders.contains(folder.folderId)) {
                                    expandedFolders - folder.folderId
                                } else {
                                    expandedFolders + folder.folderId
                                }
                            }
                            .background(themeColors.backGround4, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = folder.name,
                            color = themeColors.text1,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }


                // Mostrar subfolders si el folder actual está expandido
                if (expandedFolders.contains(folder.folderId)) {
                    displayFolders(folders, folder.folderId, depth + 1)
                }

                // Mostrar notas en este folder
                if (expandedFolders.contains(folder.folderId)) {
                    val notesInFolder = notesWithoutContentWithCategories.filter {
                        it.note.folderId == folder.folderId
                    }

                    notesInFolder.forEach { noteWithCategories ->
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = (16 * (depth + 1)).dp, vertical = 4.dp)
                                    .clickable {
                                        appViewModel.changeSelectedNoteId(noteWithCategories.note.noteId)
                                        appViewModel.toggleShowingNote()
                                    }
                                    .background(themeColors.backGround4, shape = RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = noteWithCategories.note.title.replace("\n", " "),
                                    color = themeColors.text1,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }


        // Mostrar carpetas y subcarpetas desde el nivel raíz
        displayFolders(folders, parentId = null, depth = 0)

        val notesWithoutFolders = notesWithoutContentWithCategories.filter { it.note.folderId == null }
        val sortedNotesWithoutFolders = notesWithoutFolders.sortedByDescending { it.note.updatedAt }

        // Ordenamos las notas por `updatedAt` descendente
        val sortedNotes = notesWithoutContentWithCategories.sortedByDescending { it.note.updatedAt }

        val notesToShow = if (selectedCategory == "All") {
            sortedNotesWithoutFolders.map { it }
        } else {
            sortedNotesWithoutFolders.filter { noteWithCategories ->
                noteWithCategories.categories.any { it.name == selectedCategory}
            }.map { it }
        }

        items(notesToShow) { note ->
            var showMenu by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 8.dp)
                    .background(themeColors.backGround4, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .clickable {
                        appViewModel.changeSelectedNoteId(note.note.noteId)
                        appViewModel.toggleShowingNote()
                    }
            ) {
                val formattedUpdatedAt = formatDate(note.note.updatedAt)

                // Fila superior con el título y el botón de opciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.note.title.replace("\n", " "),
                        color = themeColors.text1,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Box {
                        Text(
                            text = "⋮",
                            color = themeColors.text1,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    showMenu = true
                                    appViewModel.changeSelectedNoteId(note.note.noteId)
                                } // Al hacer clic, mostrar el menú
                                .background(
                                    color = themeColors.backGround1,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(vertical = 2.dp, horizontal = 8.dp)
                        )

                        // Ventana flotante con las opciones
                        FloatingOptions(
                            showMenu = showMenu,
                            changeShowMenu = { showMenu = it },
                            noteViewModel = noteViewModel
                        )
                    }
                }


                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Modified:  ",
                        color = themeColors.text3,
                        fontSize = 12.sp
                    )
                    Text(
                        text = formattedUpdatedAt,
                        color = themeColors.text3,
                        fontSize = 12.sp
                    )
                }

                // FlowRow para las categorías de la nota
                LazyRow(
                    modifier = Modifier.padding(top = 1.dp)
                ) {
                    val categories = notesToShow.find { it.note.noteId== note.note.noteId }?.categories ?: emptyList()

                    items(categories) { category ->
                        Box(
                            modifier = Modifier
                                .padding(end = 1.dp)
                                .background(
                                    themeViewModel.getCategoryColor(category.bgColor),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = category.name,
                                color = themeColors.text1,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// Función para formatear las fechas
fun formatDate(timestamp: Long): String {
    return try {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        // En caso de error, devuelve un texto por defecto
        "Invalid Date"
    }
}