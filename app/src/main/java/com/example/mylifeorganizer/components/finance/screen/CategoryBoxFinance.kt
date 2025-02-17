package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryFinanceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryBoxFinance(
    category: CategoryFinanceEntity?,
    name: String,
    bgColor: String,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    var showOptions by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .background(
                color = if(name == "All") {
                    themeColors.backGround1
                } else {
                    themeViewModel.getCategoryColor(bgColor)
                }
            )
            .padding(horizontal = 8.dp, vertical = 1.dp)
            .clickable {
                appViewModel.updateSelectedCategoryForFinanceScreen(name)
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        if(category != null) {
                            showOptions = true
                        }
                    }
                )
            }
    ) {
        Text(
            text = name,
            color = themeColors.text1
        )
    }

    if(showOptions) {
        DropdownMenu(
            expanded = showOptions,
            onDismissRequest = { showOptions = false },
            modifier = Modifier
                .background(themeColors.bgDialog)
                .padding(16.dp)
        ) {
            DropdownMenuItem(
                onClick = {
                    showEditDialog = true
                },
                modifier = Modifier.background(themeColors.backGround1),
                text = {
                    Text(
                        text = if(isLangEng) "Edit" else "Editar",
                        color = themeColors.text1
                    )
                }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            DropdownMenuItem(
                onClick = {
                    showDeleteDialog = true
                },
                modifier = Modifier.background(themeColors.backGround1),
                text = {
                    Text(
                        text = if(isLangEng) "Delete" else "Eliminar",
                        color = themeColors.text1
                    )
                }
            )
        }
    }

    if(showEditDialog) {
        EditCategoryDialog(
            category = category!!,
            changeShowEditDialog = { showEditDialog = it }
        )
    }
}