package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesFoldersContainer(
    selectedCategory: String,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val notesWithoutContentWithCategories by noteViewModel.notesWithoutContentWithCategories.collectAsState(initial = emptyList())

    val folders by noteViewModel.folders.collectAsState(initial = emptyList())
    val expandedFolders = appViewModel.expandedFolders.value

    val selectedOrderingNotes = appViewModel.selectedOrderingNotes.value


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Función recursiva para mostrar carpetas y subcarpetas
        fun displayFoldersAndNotes(parentId: Long, depth: Int) {
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
                    FolderCard(
                        folder = folder,
                        depth = depth
                    )

                }


                // Mostrar subfolders si el folder actual está expandido
                if (expandedFolders.contains(folder.folderId)) {
                    val notesInFolder = notesWithoutContentWithCategories.filter {
                        it.note.folderId == folder.folderId
                    }

                    val sortedNotesInFolder = when(selectedOrderingNotes) {
                        "createdAscending" -> notesInFolder.sortedBy { it.note.createdAt }
                        "createdDescending" -> notesInFolder.sortedByDescending { it.note.createdAt }
                        "updatedAscending" -> notesInFolder.sortedBy { it.note.updatedAt }
                        "updatedDescending" -> notesInFolder.sortedByDescending { it.note.updatedAt }
                        "nameAscending" -> notesInFolder.sortedBy { it.note.title }
                        "nameDescending" -> notesInFolder.sortedByDescending { it.note.title }
                        else -> notesInFolder.sortedByDescending { it.note.updatedAt }
                    }
                    val notesToShow = if (selectedCategory == "All") {
                        sortedNotesInFolder
                    } else {
                        sortedNotesInFolder.filter { noteWithCategories ->
                            noteWithCategories.categories.any { it.name == selectedCategory}
                        }.map { it }
                    }

                    notesToShow.forEach { noteWithCategories ->
                        item {
                            NoteCard(
                                note = noteWithCategories,
                                depth = depth + 1
                            )
                        }
                    }


                    // Mostrar subcarpetas de forma recursiva
                    displayFoldersAndNotes(folder.folderId, depth + 1)
                }
            }
        }


        // Mostrar carpetas y subcarpetas desde el nivel raíz
        displayFoldersAndNotes(parentId = 0L, depth = 0)


        val notesWithoutFolders = notesWithoutContentWithCategories.filter {
            it.note.folderId == 0L
        }

        val sortedNotesWithoutFolders = when(selectedOrderingNotes) {
            "createdAscending" -> notesWithoutFolders.sortedBy { it.note.createdAt }
            "createdDescending" -> notesWithoutFolders.sortedByDescending { it.note.createdAt }
            "updatedAscending" -> notesWithoutFolders.sortedBy { it.note.updatedAt }
            "updatedDescending" -> notesWithoutFolders.sortedByDescending { it.note.updatedAt }
            "nameAscending" -> notesWithoutFolders.sortedBy { it.note.title }
            "nameDescending" -> notesWithoutFolders.sortedByDescending { it.note.title }
            else -> notesWithoutFolders.sortedByDescending { it.note.updatedAt }
        }

        val notesToShow = if (selectedCategory == "All") {
            sortedNotesWithoutFolders
        } else {
            sortedNotesWithoutFolders.filter { noteWithCategories ->
                noteWithCategories.categories.any { it.name == selectedCategory}
            }.map { it }
        }

        items(notesToShow) { note ->
            NoteCard(
                note = note,
                depth = 0
            )
        }
    }
}