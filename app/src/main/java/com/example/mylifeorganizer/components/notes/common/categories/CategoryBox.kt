package com.example.mylifeorganizer.components.notes.common.categories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.AlertDialogWindow
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryBox(
    category: CategoryEntity,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categoryName: String
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val isLangEng = appViewModel.isLangEng.value
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var showOptionsForCategory by remember { mutableStateOf(false) }
    var showDialogRename by remember { mutableStateOf(false) }

    var nameForRename by remember { mutableStateOf(category.name) }

    val cornerRadius = 4.dp

    Box(
        modifier = Modifier
            .clickable {
                onCategorySelected(categoryName)
            }
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(horizontal = 4.dp)
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showOptionsForCategory = true
                        Log.d("CategoryBox", "Long press detected for category box")
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = themeViewModel.getCategoryColor(category.bgColor),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(horizontal = 8.dp), // Espaciado adicional dentro del fondo,
        ) {
            Text(
                text = categoryName,
                color = if (selectedCategory == categoryName) themeColors.text1 else themeColors.text3,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }

    if(showOptionsForCategory) {
        // Ventana flotante con las opciones
        FloatingOptionsCategory(
            showMenu = showOptionsForCategory,
            changeShowMenu = { showOptionsForCategory = it },
            changeShowDialogRename = { showDialogRename = it }
        )
    }

    if(showDialogRename) {
        AlertDialogWindow(
            title = if(isLangEng) "Rename category" else "Renombrar categoría",
            confirmButtonText = if(isLangEng) "Rename" else "Renombrar",
            onConfirm = {
                noteViewModel.updateCategory(
                    category.copy(name = nameForRename)
                )
                showDialogRename = false
                showOptionsForCategory = false
            },
            dismissButtonText = if(isLangEng) "Cancel" else "Cancelar",
            onDismiss = {
                showDialogRename = false
            },
            isConfirmButtonEnabled = true,
            textFieldValue = nameForRename,
            textFieldOnValueChange = {
                nameForRename = it
            },
            textFieldLabel = if(isLangEng) "New name" else "Nuevo nombre",
        )

    }
}