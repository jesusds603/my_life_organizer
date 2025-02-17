package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TitleDescriptionAmount() {
    val themeViewModel: ThemeViewModel = viewModel()
    val appViewModel: AppViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value


    val titleForNewFinance = appViewModel.titleForNewFinance
    val descriptionForNewFinance = appViewModel.descriptionForNewFinance
    val amountForNewFinance = appViewModel.amountForNewFinance

    TextField(
        value = titleForNewFinance,
        onValueChange = { appViewModel.updateTitleForNewFinance(it) },
        label = {
            Text(
                text = if(isLangEng) "Title" else "Título",
                color = themeColors.text1
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedTextColor = themeColors.text1,
            unfocusedTextColor = themeColors.text1,
            focusedContainerColor = themeColors.backGround1,
            unfocusedContainerColor = themeColors.backGround1,
        )
    )
    TextField(
        value = descriptionForNewFinance,
        onValueChange = { appViewModel.updateDescriptionForNewFinance(it) },
        label = {
            Text(
                text = if(isLangEng) "Description" else "Descripción",
                color = themeColors.text1
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = themeColors.text1,
            unfocusedTextColor = themeColors.text1,
            focusedContainerColor = themeColors.backGround1,
            unfocusedContainerColor = themeColors.backGround1,
        )
    )
    TextField(
        value = amountForNewFinance.toString(),
        onValueChange = { input ->
            if(input.isNotEmpty()){
                appViewModel.updateAmountForNewFinance(input.toLong()) // Actualiza el ViewModel
            }
        },
        label = {
            Text(
                text = if(isLangEng) "Amount $" else "Monto $",
                color = themeColors.text1
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedTextColor = themeColors.text1,
            unfocusedTextColor = themeColors.text1,
            focusedContainerColor = themeColors.backGround1,
            unfocusedContainerColor = themeColors.backGround1,
        )
    )
}