package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WindowDialog() {
    val themeViewModel: ThemeViewModel = viewModel()
    val appViewModel: AppViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val isExpenseForNewFinance = appViewModel.isExpenseForNewFinance

    val isAddingDateForFinance = appViewModel.isAddingDateForFinance.value
    val isAddingCategoryForFinance = appViewModel.isAddingCategoryForFinance.value
    val isAddingPaymentMethodForFinance = appViewModel.isAddingPaymentMethodForFinance.value


    AlertDialog(
        onDismissRequest = {
            appViewModel.toggleAddingFinance()
        },
        confirmButton = {
            ConfirmButton()
        },
        dismissButton = {
            DismissButton()
        },
        title = {
            Text(
                text = if(isLangEng) "Add New Finance" else "Añadir Nueva Finanza",
                color = themeColors.text1
            )
        },
        text = {
            Column {
                TitleDescriptionAmount()

                DateRow()

                Spacer(modifier = Modifier.height(2.dp))

                CategoriesSection()

                Spacer(modifier = Modifier.height(8.dp))

                PaymentSection()

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para alternar entre Expense e Income
                Button(
                    onClick = { appViewModel.toggleIsExpenseForNewFinance() },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if(isExpenseForNewFinance) themeColors.text1 else Color.Black,
                        containerColor = if (isExpenseForNewFinance) Color.Red else Color.Green
                    )
                ) {
                    Text(
                        text = if (isExpenseForNewFinance) {
                            if(isLangEng) "Expense" else "Gasto"
                        } else {
                            if(isLangEng) "Income" else "Ingreso"
                        },
                        color = if(isExpenseForNewFinance) Color.White else Color.Black
                    )
                }
            }
        },
        containerColor = themeColors.bgDialog,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.large // Bordes más redondeados
    )

    if(isAddingDateForFinance) {
        MyDatePicker()
    }

    if(isAddingCategoryForFinance) {
        AddCategoryFinance()
    }

    if(isAddingPaymentMethodForFinance) {
        AddPayment()
    }
}