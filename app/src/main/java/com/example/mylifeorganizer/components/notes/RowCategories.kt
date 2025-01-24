package com.example.mylifeorganizer.components.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryWithNotes
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun RowCategories(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categoriesWithNotes: List<CategoryWithNotes>
) {
    val appViewModel: AppViewModel = viewModel()

    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val padding4 = 4.dp
    val padding2 = 2.dp
    val fontSize = 16.sp
    val cornerRadius = 4.dp
    val padding8 = 8.dp


    LazyRow (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = themeColors.backGround1)
    ) {

        // Agregar la opción "All"
        item {
            Box(
                modifier = Modifier
                    .clickable {onCategorySelected("All")}
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(cornerRadius)
                    )
                    .padding(horizontal = padding8)
                    .drawWithContent {
                        // Dibuja el contenido primero
                        drawContent()

                        // Si está seleccionada, dibuja un borde verde en la parte inferior
                        if (selectedCategory ==  "All") {
                            drawLine(
                                color = Color.Green,
                                start = Offset(0f, size.height), // Comienza en el fondo de la Box
                                end = Offset(size.width, size.height), // Termina en el fondo de la Box
                                strokeWidth = 10f
                            )
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = themeColors.backGround1,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                        .padding(vertical = padding2, horizontal = padding4), // Espaciado adicional dentro del fondo
                ) {
                    Text(
                        text = "All",
                        color = if (selectedCategory == "All") themeColors.text1 else themeColors.text3,
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }

        if (categoriesWithNotes.isNotEmpty()) {
            // Agregar las demás categorías
            items(categoriesWithNotes)  { categoryWithNotes ->
                val categoryName = categoryWithNotes.category.name

                Box(
                    modifier = Modifier
                        .clickable { onCategorySelected(categoryName) }
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                        .padding(horizontal = padding8)
                        .drawWithContent {
                            // Dibuja el contenido primero
                            drawContent()

                            // Si está seleccionada, dibuja un borde verde en la parte inferior
                            if (selectedCategory == categoryName) {
                                drawLine(
                                    color = Color.Green,
                                    start = Offset(0f, size.height), // Comienza en el fondo de la Box
                                    end = Offset(size.width, size.height), // Termina en el fondo de la Box
                                    strokeWidth = 10f
                                )
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = themeViewModel.getCategoryColor(categoryWithNotes.category.bgColor),
                                shape = RoundedCornerShape(cornerRadius)
                            )
                            .padding(vertical = padding2, horizontal = padding4), // Espaciado adicional dentro del fondo,
                    ) {
                        Text(
                            text = categoryName,
                            color = if (selectedCategory == categoryName) themeColors.text1 else themeColors.text3,
                            fontSize = fontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }
    }
}