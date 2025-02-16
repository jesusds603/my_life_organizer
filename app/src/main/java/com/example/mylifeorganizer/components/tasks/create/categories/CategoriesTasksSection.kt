package com.example.mylifeorganizer.components.tasks.create.categories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoriesTasksSection() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val selectedCategoriesTask = appViewModel.selectedCategoriesTask


    Column (
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = themeColors.backGround1)
            .padding(4.dp)
    ) {
        Text(
            text = if(isLangEng) "Categories:" else "Categorías:",
            color = themeColors.text1,
            fontWeight = FontWeight.Bold
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (selectedCategoriesTask.isNotEmpty()) {
                LazyRow (
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    items(selectedCategoriesTask) { category ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(color = themeViewModel.getCategoryColor(category.bgColor))
                                .clickable {
                                    appViewModel.updateSelectedCategoriesTask( selectedCategoriesTask - category)
                                }
                        ) {
                            Text(
                                text = category.name,
                                color = themeColors.text1
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = if(isLangEng) "No categories selected" else "No hay categorías seleccionadas",
                    color = themeColors.text1,
                    modifier = Modifier.weight(1f)
                )
            }

            Box(
                modifier = Modifier
                    .wrapContentSize() // Ajusta el Box al contenido
                    .background(
                        color = themeColors.backGround1,
                        shape = CircleShape
                    )
                    .clickable {
                        appViewModel.toggleShowCreateCategoryDialogTask()
                    }
                    .padding(8.dp) // Espaciado interno del botón
            ) {
                Text(
                    text = "+",
                    color = themeColors.text1,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center // Alinea el texto dentro del Box
                )
            }
        }



    }
}