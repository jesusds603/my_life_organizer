package com.example.mylifeorganizer.components.notes.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mylifeorganizer.room.NoteWithCategories

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    noteWithCategories: NoteWithCategories? = null,
    title: String,
    onChangeTitle: (String) -> Unit,
    content: String,
    onChangeContent: (String) -> Unit
) {

}