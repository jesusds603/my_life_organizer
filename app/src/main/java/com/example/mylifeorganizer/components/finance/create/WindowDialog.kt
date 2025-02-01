package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FinanceEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WindowDialog() {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel

    val titleForNewFinance = appViewModel.titleForNewFinance
    val descriptionForNewFinance = appViewModel.descriptionForNewFinance
    val amountForNewFinance = appViewModel.amountForNewFinance
    val dateForNewFinance = appViewModel.dateForNewFinance
    val isExpenseForNewFinance = appViewModel.isExpenseForNewFinance

    val financeIdForEditing = appViewModel.financeIdForEditing
    val isAddingFinance = appViewModel.isAddingFinance.value
    val isEditingFinance = appViewModel.isEditingFinance.value
    val isAddingDateForFinance = appViewModel.isAddingDateForFinance.value
    val isAddingCategoryForFinance = appViewModel.isAddingCategoryForFinance.value
    val isAddingPaymentMethodForFinance = appViewModel.isAddingPaymentMethodForFinance.value
    val selectedCategoriesForNewFinance = appViewModel.selectedCategoriesForNewFinance
    val paymentMethodForNewFinance = appViewModel.paymentMethodForNewFinance

    AlertDialog(
        onDismissRequest = {
            appViewModel.toggleAddingFinance()
        },
        confirmButton = {
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
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(text = "Add Finance", color = themeColors.text1)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    appViewModel.toggleAddingFinance()
                    appViewModel.updateTitleForNewFinance("")
                    appViewModel.updateDescriptionForNewFinance("")
                    appViewModel.updateAmountForNewFinance(0)
                    appViewModel.updateDateForNewFinance("Today")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(text = "Cancel", color = themeColors.text1)
            }
        },
        title = {
            Text(
                text = "Add New Finance",
                color = themeColors.text1
            )
        },
        text = {
            Column {
                TextField(
                    value = titleForNewFinance,
                    onValueChange = { appViewModel.updateTitleForNewFinance(it) },
                    label = { Text("Title", color = themeColors.text1) },
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
                    label = { Text("Description", color = themeColors.text1) },
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
                        appViewModel.updateAmountForNewFinance(input.toLong()) // Actualiza el ViewModel
                    },
                    label = { Text("Amount $", color = themeColors.text1) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = themeColors.text1,
                        unfocusedTextColor = themeColors.text1,
                        focusedContainerColor = themeColors.backGround1,
                        unfocusedContainerColor = themeColors.backGround1,
                    )
                )

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Date: ", color = themeColors.text1)
                    TextButton(
                        onClick = {
                            appViewModel.toggleAddingDateForFinance()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = themeColors.text1,
                            containerColor = themeColors.backGround1
                        )
                    ) {
                        Text(
                            text = dateForNewFinance,
                            color = themeColors.text1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text("Categories: ", color = themeColors.text1)

                    Spacer(modifier = Modifier.width(4.dp))

                    TextButton(
                        modifier = Modifier.size(40.dp),
                        onClick = {
                            appViewModel.toggleAddingCategoryForFinance()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = themeColors.text1,
                            containerColor = themeColors.backGround1
                        ),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "+",
                            color = themeColors.text1
                        )
                    }
                }

                LazyRow (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(selectedCategoriesForNewFinance) { category ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .background(themeViewModel.getCategoryColor(category.bgColor))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = category.name,
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))


                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text("Payment Method: ", color = themeColors.text1)

                    Spacer(modifier = Modifier.width(4.dp))

                    TextButton(
                        modifier = Modifier.size(40.dp),
                        onClick = {
                            appViewModel.toggleAddingPaymentMethodForFinance()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = themeColors.text1,
                            containerColor = themeColors.backGround1
                        ),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "+",
                            color = themeColors.text1
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .background(themeViewModel.getCategoryColor(paymentMethodForNewFinance?.bgColor ?: ""))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = paymentMethodForNewFinance?.name ?: "",
                        color = themeColors.text1,
                        fontWeight = FontWeight.Bold
                    )
                }

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
                        text = if (isExpenseForNewFinance) "Expense" else "Income",
                        color = if(isExpenseForNewFinance) Color.White else Color.Black
                    )
                }
            }
        },
        containerColor = themeColors.backGround3,
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