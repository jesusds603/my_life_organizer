package com.example.mylifeorganizer.components.habits.screen

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

@Composable
fun FloatingOptionsHabit(
    showMenu: Boolean = false,
    changeShowMenu: (Boolean) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value


    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { changeShowMenu(false) }, // Cierra el menú al hacer clic afuera
        modifier = Modifier.background(themeColors.backGround1)
    ) {
        DropdownMenuItem(
            text = { Text(
                text= "Delete",
                color = themeColors.text1
            ) },
            onClick = {

                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )
    }
}