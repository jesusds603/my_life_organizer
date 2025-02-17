package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddCategoryFinance() {
    val themeViewModel: ThemeViewModel = viewModel()
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val allCategories = noteViewModel.categoriesFinance.collectAsState(initial = emptyList()).value
    val selectedCategoriesForNewFinance = appViewModel.selectedCategoriesForNewFinance
    val isCreatingCategoryForFinance = appViewModel.isCreatingCategoryForFinance.value


    AlertDialog(
        onDismissRequest = {
            appViewModel.toggleAddingCategoryForFinance()
        },
        confirmButton = {
            Button(
                onClick = { appViewModel.toggleAddingCategoryForFinance() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(
                    text = "OK",
                    color = themeColors.text1
                )
            }
        },
        title = {
            Text(
                text = if(isLangEng) "Add New Category" else "Añadir Nueva Categoría",
                color = themeColors.text1
            )
        },
        text = {
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if(isLangEng) "Available Categories:" else "Categorías Disponibles:",
                        color = themeColors.text1
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(
                        modifier = Modifier.size(40.dp),
                        onClick = {
                            appViewModel.toggleCreatingCategoryForFinance()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = themeColors.text1,
                            containerColor = themeColors.backGround1
                        ),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "+",
                            color = themeColors.text1
                        )
                    }
                }

                if(isCreatingCategoryForFinance) {
                    CreatingCategory()
                }

                LazyRow {
                    items(allCategories) { category ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .background(themeViewModel.getCategoryColor(category.bgColor))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .clickable {
                                    appViewModel.updateSelectedCategoriesForNewFinance(selectedCategoriesForNewFinance + category)
                                }
                        ) {
                            Text(
                                text = category.name,
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if(isLangEng) {
                        "Press on a category to add it or remove it from the list to the finance"
                    } else {
                        "Presiona en una categoría para agregarlo o eliminarlo de la lista para la finanzas"
                    },
                    color = themeColors.text1
                )

                LazyRow {
                    items(selectedCategoriesForNewFinance) { category ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .background(themeViewModel.getCategoryColor(category.bgColor))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .clickable {
                                    appViewModel.updateSelectedCategoriesForNewFinance(selectedCategoriesForNewFinance - category)
                                }
                        ) {
                            Text(
                                text = category.name,
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        containerColor = themeColors.bgDialog,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.large // Bordes más redondeados
    )
}