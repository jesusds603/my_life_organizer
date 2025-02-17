package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FinanceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmButton() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val titleForNewFinance = appViewModel.titleForNewFinance
    val descriptionForNewFinance = appViewModel.descriptionForNewFinance
    val amountForNewFinance = appViewModel.amountForNewFinance
    val dateForNewFinance = appViewModel.dateForNewFinance
    val isExpenseForNewFinance = appViewModel.isExpenseForNewFinance

    val financeIdForEditing = appViewModel.financeIdForEditing
    val isAddingFinance = appViewModel.isAddingFinance.value
    val isEditingFinance = appViewModel.isEditingFinance.value
    val selectedCategoriesForNewFinance = appViewModel.selectedCategoriesForNewFinance
    val paymentMethodForNewFinance = appViewModel.paymentMethodForNewFinance

    Button(
        onClick = {
            if(isAddingFinance && !isEditingFinance) {
                noteViewModel.addFinance(
                    FinanceEntity(
                        title = titleForNewFinance,
                        description = descriptionForNewFinance,
                        amount = amountForNewFinance.toDouble(),
                        date = if(dateForNewFinance == "Today") LocalDate.now().format(
                            DateTimeFormatter.ofPattern("yyyy/MM/dd")) else dateForNewFinance,
                        type = if (isExpenseForNewFinance) "expense" else "income",
                        paymentId = paymentMethodForNewFinance?.paymentId
                    ),
                    onFinanceAdded = { financeId ->
                        selectedCategoriesForNewFinance.forEach { category ->
                            noteViewModel.linkFinanceToCategory(
                                financeId,
                                category.categoryId
                            )
                        }

                    }
                )

                appViewModel.toggleAddingFinance()

            }
            if(isEditingFinance && !isAddingFinance) {
                noteViewModel.updateFinance(
                    FinanceEntity(
                        financeId = financeIdForEditing.value!!,
                        title = titleForNewFinance,
                        description = descriptionForNewFinance,
                        amount = amountForNewFinance.toDouble(),
                        date = if(dateForNewFinance == "Today") LocalDate.now().format(
                            DateTimeFormatter.ofPattern("yyyy/MM/dd")) else dateForNewFinance,
                        type = if (isExpenseForNewFinance) "expense" else "income",
                    ),
                    onFinanceUpdated = { financeId ->
                        // Eliminamos todas las relaciones anteriores de categorias
                        noteViewModel.deleteFinanceCategories(financeId)

                        // Linkear las nuevas categorias
                        selectedCategoriesForNewFinance.forEach { category ->
                            noteViewModel.linkFinanceToCategory(
                                financeId,
                                category.categoryId
                            )
                        }
                    }
                )

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
            text = if(isLangEng) "Add Finance" else "Añadir Finanza",
            color = themeColors.text1
        )
    }
}