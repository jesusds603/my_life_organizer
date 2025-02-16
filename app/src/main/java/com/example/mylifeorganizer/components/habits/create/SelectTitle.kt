package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectTitle() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val titleNewHabit = appViewModel.titleNewHabit.value

    TextField(
        value = titleNewHabit,
        onValueChange = {
            appViewModel.changeTitleNewHabit(it)
        },
        label = {
            Text(
                text = if(isLangEng) "Title" else "Título",
                color = themeColors.text2,
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = themeColors.text1,
            unfocusedTextColor = themeColors.text1,
            focusedContainerColor = themeColors.backGround1,
            unfocusedContainerColor = themeColors.backGround1,
        )
    )
}