package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.room.PaymentMethodEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FloatingOptionsFinance(
    finance: FinanceWithCategories,
    showMenu: Boolean = false,
    changeShowMenu: (Boolean) -> Unit,
    paymentMethod: PaymentMethodEntity
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { changeShowMenu(false) }, // Cierra el menú al hacer clic afuera
        modifier = Modifier.background(themeColors.backGround1)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "Edit",
                    color = themeColors.text1
                )
            },
            onClick = {
                appViewModel.toggleEditingFinance()
                appViewModel.updateTitleForNewFinance(finance.finance.title)
                appViewModel.updateDescriptionForNewFinance(finance.finance.description)
                appViewModel.updateAmountForNewFinance(finance.finance.amount.toLong())
                appViewModel.updateSelectedCategoriesForNewFinance(finance.categories)
                appViewModel.updatePaymentMethodForNewFinance(paymentMethod)
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = "Delete",
                    color = themeColors.text1
                )
            },
            onClick = {
                noteViewModel.deleteFinance(finance.finance)
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround3)
        )
    }
}