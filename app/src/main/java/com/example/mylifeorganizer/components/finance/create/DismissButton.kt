package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DismissButton() {
    val themeViewModel: ThemeViewModel = viewModel()
    val appViewModel: AppViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val isAddingFinance = appViewModel.isAddingFinance.value
    val isEditingFinance = appViewModel.isEditingFinance.value

    Button(
        onClick = {
            if(isAddingFinance) {
                appViewModel.toggleAddingFinance()
            }
            if(isEditingFinance) {
                appViewModel.toggleEditingFinance()
            }
            appViewModel.updateTitleForNewFinance("")
            appViewModel.updateDescriptionForNewFinance("")
            appViewModel.updateAmountForNewFinance(0)
            appViewModel.updateDateForNewFinance("Today")
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = themeColors.backGround1
        )
    ) {
        Text(
            text = if(isLangEng) "Cancel" else "Cancelar",
            color = themeColors.text1
        )
    }
}