package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.components.notes.common.categories.CategoryBox
import com.example.mylifeorganizer.room.NoteWithoutContentWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteCard(
    noteViewModel: NoteViewModel,
    note: NoteWithoutContentWithCategories,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var showMenu by remember { mutableStateOf(false) }


    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .background(themeColors.backGround4, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_edit_note_24),
            contentDescription = null,
            tint = themeColors.text1,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    appViewModel.changeSelectedNoteId(note.note.noteId)
                    appViewModel.toggleShowingNote()
                    appViewModel.changeIdFolderForAddingNote(note.note.folderId)
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
                    Icon(
                        painter = painterResource(R.drawable.baseline_more_vert_24),
                        contentDescription = null,
                        tint = themeColors.text1,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                showMenu = true
                                appViewModel.changeSelectedNoteId(note.note.noteId)
                            }
                    )

                    // Ventana flotante con las opciones
                    FloatingOptionsNote(
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
                modifier = Modifier.padding(top = 1.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val categories = note.categories

                items(categories) { category ->
                    CategoryBox(
                        category = category,
                        selectedCategory = "",
                        onCategorySelected = {},
                        categoryName = category.name
                    )
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