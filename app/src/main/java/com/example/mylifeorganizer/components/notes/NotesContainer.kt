package com.example.mylifeorganizer.components.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.mylifeorganizer.room.CategoryWithNotes
import com.example.mylifeorganizer.room.NoteWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotesContainer(
    selectedCategory: String,
    notesWithCategories: List<NoteWithCategories>,
    categoriesWithNotes: List<CategoryWithNotes>
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        val notesToShow = if (selectedCategory == "All") {
            notesWithCategories.map { it.note }
        } else {
            val selectedCategoryNotes = categoriesWithNotes.find { it.category.name == selectedCategory }
            selectedCategoryNotes?.notes ?: emptyList()
        }

        items(notesToShow) { note ->
            var showMenu by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 8.dp)
                    .background(themeColors.backGround4, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            ) {
                // Formatear las fechas
                val formattedCreatedAt = formatDate(note.createdAt)
                val formattedUpdatedAt = formatDate(note.updatedAt)

                // Fila superior con el título y el botón de opciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.title.replace("\n", " "),
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
                                .clickable { showMenu = true } // Al hacer clic, mostrar el menú
                                .background(
                                    color = themeColors.backGround1,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(vertical = 2.dp, horizontal = 8.dp)
                        )

                        // Ventana flotante con las opciones
                        FloatingOptions(
                            showMenu = showMenu,
                            changeShowMenu = { showMenu = it }
                        )
                    }
                }


                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Last updated:",
                        color = themeColors.text2,
                        fontSize = 12.sp
                    )
                    Text(
                        text = formattedUpdatedAt,
                        color = themeColors.text2,
                        fontSize = 12.sp
                    )
                }

                // FlowRow para las categorías de la nota
                FlowRow(
                    modifier = Modifier.padding(top = 1.dp)
                ) {
                    val categories = notesWithCategories.find { it.note.noteId == note.noteId }?.categories ?: emptyList()
                    categories.forEach { category ->
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