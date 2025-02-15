package com.example.mylifeorganizer.components.notes.common.categories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.AlertDialogWindow
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FloatingOptionsCategory(
    showMenu: Boolean,
    changeShowMenu: (Boolean) -> Unit,
    changeShowDialogRename: (Boolean) -> Unit
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    var showDialogDelete by remember { mutableStateOf(false) }


    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { changeShowMenu(false) },
        modifier = Modifier
            .background(themeColors.bgDialog)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Rename" else "Renombrar",
                    color = themeColors.text1
                )
            },
            onClick = {
                changeShowDialogRename(true)
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Delete" else "Eliminar",
                    color = themeColors.text1
                    )
            },
            onClick = {
                showDialogDelete = true
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )
    }

}