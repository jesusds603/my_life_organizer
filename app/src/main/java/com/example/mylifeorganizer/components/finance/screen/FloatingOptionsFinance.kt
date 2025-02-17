package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    val isLangEng = appViewModel.isLangEng.value

    var showDeleteDialog by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { changeShowMenu(false) }, // Cierra el menú al hacer clic afuera
        modifier = Modifier
            .background(themeColors.bgDialog)
            .padding(16.dp)
    ) {
//        DropdownMenuItem(
//            text = {
//                Text(
//                    text = if(isLangEng) "Edit" else "Editar",
//                    color = themeColors.text1
//                )
//            },
//            onClick = {
//                appViewModel.toggleEditingFinance()
//                appViewModel.updateTitleForNewFinance(finance.finance.title)
//                appViewModel.updateDescriptionForNewFinance(finance.finance.description)
//                appViewModel.updateAmountForNewFinance(finance.finance.amount.toLong())
//                appViewModel.updateSelectedCategoriesForNewFinance(finance.categories)
//                appViewModel.updatePaymentMethodForNewFinance(paymentMethod)
//                changeShowMenu(false)
//            },
//            modifier = Modifier.background(themeColors.backGround1)
//        )
//
//        Spacer(modifier = Modifier.padding(8.dp))

        DropdownMenuItem(
            text = {
                Text(
                    text = if(isLangEng) "Delete" else "Eliminar",
                    color = themeColors.text1
                )
            },
            onClick = {
                showDeleteDialog = true
                changeShowMenu(false)
            },
            modifier = Modifier.background(themeColors.backGround1)
        )
    }

    if(showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        noteViewModel.deleteFinance(finance.finance)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColors.backGround1,
                    )
                ) {
                    Text(
                        text = if(isLangEng) "Delete" else "Eliminar",
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColors.backGround1,
                    )
                ) {
                    Text(
                        text = if(isLangEng) "Cancel" else "Cancelar",
                        color = themeColors.text1
                    )
                }
            },
            title = {
                Text(
                    text = if(isLangEng) "Delete Finance" else "Eliminar Finanza",
                    color = themeColors.text1
                )
            },
            text = {
                Text(
                    text = if (isLangEng) "Are you sure you want to delete this finance?" else "Estas seguro que quieres eliminar esta finanza?",
                    color = themeColors.text1
                )
            },
            containerColor = themeColors.bgDialog
        )
    }
}