package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun NotesFoldersContainer(
    selectedCategory: String,
    noteViewModel: NoteViewModel
) {
    val notesWithoutContentWithCategories by noteViewModel.notesWithoutContentWithCategories.collectAsState(initial = emptyList())

    val folders by noteViewModel.folders.collectAsState(initial = emptyList())
    var expandedFolders by remember { mutableStateOf<Set<Long>>(emptySet()) }


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Función recursiva para mostrar carpetas y subcarpetas
        fun displayFolders(folders: List<FolderEntity>, parentId: Long?, depth: Int) {
            val filteredFolders = folders.filter { it.parentFolderId == parentId }

            filteredFolders.forEach { folder ->
                item {

                    FolderCard(
                        noteViewModel = noteViewModel,
                        expandedFolders = expandedFolders,
                        onFolderClick = { clickedFolders ->
                            expandedFolders = clickedFolders
                        },
                        folder = folder,
                        depth = depth
                    )

                }


                // Mostrar subfolders si el folder actual está expandido
                if (expandedFolders.contains(folder.folderId)) {
                    displayFolders(folders, folder.folderId, depth + 1)

                    val notesInFolder = notesWithoutContentWithCategories.filter {
                        it.note.folderId == folder.folderId
                    }

                    val sortedNotesInFolder = notesInFolder.sortedByDescending { it.note.updatedAt }
                    val notesToShow = if (selectedCategory == "All") {
                        sortedNotesInFolder.map { it }
                    } else {
                        sortedNotesInFolder.filter { noteWithCategories ->
                            noteWithCategories.categories.any { it.name == selectedCategory}
                        }.map { it }
                    }

                    notesInFolder.forEach { noteWithCategories ->
                        item {
                            NoteCard(
                                noteViewModel = noteViewModel,
                                note = noteWithCategories,
                                notesToShow = notesToShow
                            )
                        }
                    }
                }
            }
        }


        // Mostrar carpetas y subcarpetas desde el nivel raíz
        displayFolders(folders, parentId = null, depth = 0)

        val notesWithoutFolders = notesWithoutContentWithCategories.filter { it.note.folderId == null }
        val sortedNotesWithoutFolders = notesWithoutFolders.sortedByDescending { it.note.updatedAt }

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