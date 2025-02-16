package com.example.mylifeorganizer.components.tasks.screen.categories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptionsCategoryTask(
    showOptions: Boolean,
    changeShowOptions: (Boolean) -> Unit,
    changeShowEditDialog: (Boolean) -> Unit,
    changeShowDeleteDialog: (Boolean) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value


    DropdownMenu(
        expanded = showOptions,
        onDismissRequest = { changeShowOptions(false) },
        modifier = Modifier
            .background(themeColors.bgDialog)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Edit" else "Editar",
                    color = themeColors.text1
                )
            },
            onClick = {
                changeShowOptions(false)
                changeShowEditDialog(true)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Delete" else "Borrar",
                    color = themeColors.text1
                )
            },
            onClick = {
                changeShowOptions(false)
                changeShowDeleteDialog(true)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

    }
}