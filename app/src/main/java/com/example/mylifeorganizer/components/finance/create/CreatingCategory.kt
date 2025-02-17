package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryFinanceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatingCategory() {
    val themeViewModel: ThemeViewModel = viewModel()
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    var nameNewCategory by remember { mutableStateOf("") }
    var colorNewCategory by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .background(themeColors.backGround1)
            .border(1.dp, themeColors.text1)
            .padding(4.dp)
    ) {
        Text(
            text = if(isLangEng) "Write the name of the category:" else "Escribe el nombre de la categoría:",
            color = themeColors.text1,
        )

        TextField(
            value = nameNewCategory,
            onValueChange = { nameNewCategory = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text1,
                focusedContainerColor = themeViewModel.getCategoryColor(colorNewCategory),
                unfocusedContainerColor = themeViewModel.getCategoryColor(colorNewCategory),
            )
        )

        Text(
            text = if(isLangEng) "Select the color of the category:" else "Selecciona el color de la categoría:",
            color = themeColors.text1,
        )
        FlowRow {
            themeViewModel.namesColorCategories.forEach { color ->
                TextButton(
                    modifier = Modifier.size(30.dp),
                    onClick = { colorNewCategory = color },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = themeColors.text1,
                        containerColor = themeViewModel.getCategoryColor(color)
                    ),
                    shape = CircleShape,
                ) {}
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = {
                    noteViewModel.addCategoryFinance(
                        CategoryFinanceEntity(
                            name = nameNewCategory,
                            bgColor = colorNewCategory
                        )
                    )
                    appViewModel.toggleCreatingCategoryForFinance()
                    nameNewCategory = ""
                    colorNewCategory = ""
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = themeColors.text1,
                    containerColor = themeColors.buttonAdd
                )
            ) {
                Text(
                    text = if(isLangEng) "Create" else "Crear",
                    color = themeColors.text1
                )
            }

            TextButton(
                onClick = {
                    appViewModel.toggleCreatingCategoryForFinance()
                    nameNewCategory = ""
                    colorNewCategory = ""
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = themeColors.text1,
                    containerColor = themeColors.buttonDelete
                )
            ) {
                Text(
                    text = if(isLangEng) "Cancel" else "Cancelar",
                    color = themeColors.text1
                )
            }
        }
    }
}