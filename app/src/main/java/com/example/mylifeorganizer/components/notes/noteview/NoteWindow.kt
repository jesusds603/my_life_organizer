package com.example.mylifeorganizer.components.notes.noteview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun NoteWindow(modifier: Modifier = Modifier) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val context = LocalContext.current
    val noteDB = NoteDB.getInstance(context)
    val notesRepository = NotesRepository(noteDB)
    val noteViewModel = NoteViewModel(notesRepository)

    val themeColors = themeViewModel.themeColors.value

    val selectedNote = appViewModel.selectedNote.value

    val titleState = remember { mutableStateOf(selectedNote?.title ?: "") }
    val contentState = remember { mutableStateOf(selectedNote?.content ?: "") }
    val categoriesForNote by noteViewModel.getCategoriesByNote(selectedNote?.noteId ?: 1).collectAsState(initial = emptyList())


    BackHandler {
        noteViewModel.updateNote(
            note = NoteEntity(
                title = titleState.value,
                content = contentState.value
            )
        )

        appViewModel.toggleShowingNote()
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val scrollState = rememberScrollState()

        // Campo editable para el título
        TextField(
            value = titleState.value,
            onValueChange = { newValue ->
                titleState.value = newValue
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text2,
                focusedContainerColor = themeColors.backGround2,
                unfocusedContainerColor = themeColors.backGround3,
            )
        )

        // Campo editable para el contenido
        TextField(
            value = contentState.value,
            onValueChange = { newValue ->
                contentState.value = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .weight(1f),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text2,
                focusedContainerColor = themeColors.backGround2,
                unfocusedContainerColor = themeColors.backGround3,
            )
        )

        if(categoriesForNote.isNotEmpty()) {
            // Categorías
            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items (categoriesForNote) { categryForNote ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(themeViewModel.getCategoryColor(categryForNote.bgColor))
                            .padding(vertical = 2.dp, horizontal = 8.dp)
                    ) {
                        Text(
                            text = categryForNote.name,
                            color = themeColors.text1
                        )
                    }
                }
            }
        }
    }
}