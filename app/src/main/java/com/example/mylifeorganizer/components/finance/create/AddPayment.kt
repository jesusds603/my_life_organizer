package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryFinanceEntity
import com.example.mylifeorganizer.room.PaymentMethodEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPayment() {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel

    val allPaymentMehods = noteViewModel.paymentMethods.collectAsState(initial = emptyList()).value
    val paymentMethodForNewFinance = appViewModel.paymentMethodForNewFinance

    var isCreatingPaymentMethodForFinance by remember { mutableStateOf(false) }
    var nameNewPaymentMethod by remember { mutableStateOf("") }
    var colorNewPaymentMethod by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {
            appViewModel.toggleAddingPaymentMethodForFinance()
        },
        confirmButton = {
            Button(
                onClick = {
                    appViewModel.toggleAddingPaymentMethodForFinance()
                }
            ) {
                Text(text = "OK", color = themeColors.text1)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    appViewModel.toggleAddingPaymentMethodForFinance()
                }
            ) {
                Text(text = "Cancel", color = themeColors.text1)
            }
        },
        title = {
            Text(
                text = "Add New Payment Method",
                color = themeColors.text1
            )
        },
        text = {
            Column {
                Row{
                    Text(
                        text = "Available Payment Methods:",
                        color = themeColors.text1
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(
                        modifier = Modifier.size(40.dp),
                        onClick = {
                            isCreatingPaymentMethodForFinance = true
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

                if(isCreatingPaymentMethodForFinance) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp)
                            .background(themeColors.backGround1)
                            .border(1.dp, themeColors.text1)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Write the name of the payment method:",
                            color = themeColors.text1,
                        )

                        TextField(
                            value = nameNewPaymentMethod,
                            onValueChange = { nameNewPaymentMethod = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = themeColors.text1,
                                unfocusedTextColor = themeColors.text1,
                                focusedContainerColor = themeViewModel.getCategoryColor(colorNewPaymentMethod),
                                unfocusedContainerColor = themeViewModel.getCategoryColor(colorNewPaymentMethod),
                            )
                        )

                        Text(
                            text = "Select the color of the method:",
                            color = themeColors.text1,
                        )
                        FlowRow {
                            themeViewModel.namesColorCategories.forEach { color ->
                                TextButton(
                                    modifier = Modifier.size(30.dp),
                                    onClick = { colorNewPaymentMethod = color },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = themeColors.text1,
                                        containerColor = themeViewModel.getCategoryColor(color)
                                    ),
                                    shape = CircleShape,
                                ) {}
                            }
                        }

                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = {
                                    noteViewModel.addPaymentMethod(
                                        PaymentMethodEntity(
                                            name = nameNewPaymentMethod,
                                            bgColor = colorNewPaymentMethod
                                        )
                                    )
                                    isCreatingPaymentMethodForFinance = false
                                    nameNewPaymentMethod = ""
                                    colorNewPaymentMethod = ""
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = themeColors.text1,
                                    containerColor = themeColors.buttonAdd
                                )
                            ) {
                                Text(
                                    text = "Create",
                                    color = themeColors.text1
                                )
                            }

                            TextButton(
                                onClick = {
                                    isCreatingPaymentMethodForFinance = false
                                    nameNewPaymentMethod = ""
                                    colorNewPaymentMethod = ""
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = themeColors.text1,
                                    containerColor = themeColors.buttonDelete
                                )
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = themeColors.text1
                                )
                            }
                        }
                    }
                }

                LazyRow {
                    items(allPaymentMehods) { paymentMethod ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .background(themeViewModel.getCategoryColor(paymentMethod.bgColor))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .clickable {
                                    appViewModel.updatePaymentMethodForNewFinance(paymentMethod)
                                }
                        ) {
                            Text(
                                text = paymentMethod.name,
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = "Selected Payment Method:",
                    color = themeColors.text1
                )
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
            }
        },
        containerColor = themeColors.backGround3,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.large // Bordes más redondeados
    )

}