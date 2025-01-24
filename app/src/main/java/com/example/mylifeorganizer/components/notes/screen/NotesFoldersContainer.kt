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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun NotesFoldersContainer(
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_folder_24),
                                contentDescription = null,
                                tint = themeColors.text1,
                            )
                            Text(
                                text = folder.name,
                                color = themeColors.text1,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        TextButton(
                            onClick = {}
                        ) {
                            Text(
                                text = "::",
                                color = themeColors.text1,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
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

            NoteCard(
                noteViewModel = noteViewModel,
                note = note,
                notesToShow = notesToShow
            )
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