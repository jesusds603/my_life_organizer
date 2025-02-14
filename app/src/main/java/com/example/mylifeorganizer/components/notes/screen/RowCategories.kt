package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.components.notes.common.categories.CategoryBox
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowCategories(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val categories by noteViewModel.categories.collectAsState(initial = emptyList())

    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value
    val selectedOrderingNotes = appViewModel.selectedOrderingNotes.value

    var showMenuOrdering by remember { mutableStateOf(false) }

    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Fila de categorías
        LazyRow (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = themeColors.backGround1)
                .weight(1f)
        ) {

            // Agregar la opción "All"
            item {
                CategoryBox(
                    category = CategoryEntity(name = "All", bgColor = "backGround1"),
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected,
                    categoryName = "All"
                )
            }

            if (categories.isNotEmpty()) {
                // Agregar las demás categorías
                items(categories)  { category ->
                    val categoryName = category.name

                    CategoryBox(
                        category = category,
                        selectedCategory = selectedCategory,
                        onCategorySelected = onCategorySelected,
                        categoryName = categoryName
                    )
                }
            }
        }

        // Menú para ordenar notas
        Box(
            modifier = Modifier
                .width(30.dp)
                .align(Alignment.CenterVertically)
                .background(color = themeColors.tabButtonSelected)
                .clickable {
                    showMenuOrdering = true
                }
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_swap_vert_24),
                contentDescription = null,
                tint = themeColors.text1,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp)
            )
        }

        if(showMenuOrdering) {
            AlertDialog(
                onDismissRequest = { showMenuOrdering = false },
                confirmButton = {
                    Button(
                        onClick = { showMenuOrdering = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColors.backGround1
                        )
                    ) {
                        Text("OK", color = themeColors.text1)
                    }
                },
                text = {
                    LazyColumn {
                        items(
                            listOf(
                                (if (isLangEng) "Created Ascending" else "Creado Ascendente") to "createdAscending",
                                (if (isLangEng) "Created Descending" else "Creado Descendente") to "createdDescending",
                                (if (isLangEng) "Updated Ascending" else "Actualizado Ascendente") to "updatedAscending",
                                (if (isLangEng) "Updated Descending" else "Actualizado Descendente") to "updatedDescending",
                                (if (isLangEng) "Name Ascending" else "Nombre Ascendente") to "nameAscending",
                                (if (isLangEng) "Name Descending" else "Nombre Descendente") to "nameDescending"
                            )
                        ) { (label, value) ->
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .clickable {
                                        appViewModel.changeSelectedOrderingNotes(value)
                                    }
                            ) {
                                Icon(
                                    painter = if (selectedOrderingNotes == value) {
                                        painterResource(R.drawable.baseline_radio_button_checked_24)
                                    } else {
                                        painterResource(R.drawable.baseline_radio_button_unchecked_24)

                                    },
                                    contentDescription = null,
                                    tint = themeColors.text1,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = label,
                                    color = themeColors.text1,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }

                        }
                    }
                },
                containerColor = themeColors.bgDialog
            )
        }
    }
}