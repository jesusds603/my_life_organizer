package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPayment() {
    val themeViewModel: ThemeViewModel = viewModel()
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val allPaymentMehods = noteViewModel.paymentMethods.collectAsState(initial = emptyList()).value
    val paymentMethodForNewFinance = appViewModel.paymentMethodForNewFinance

    var isCreatingPaymentMethodForFinance by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
            appViewModel.toggleAddingPaymentMethodForFinance()
        },
        confirmButton = {
            Button(
                onClick = {
                    appViewModel.toggleAddingPaymentMethodForFinance()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(
                    text = "OK",
                    color = themeColors.text1
                )
            }
        },
        title = {
            Text(
                text = if(isLangEng) "Add New Payment Method" else "Añadir Nuevo Método de Pago",
                color = themeColors.text1
            )
        },
        text = {
            Column {
                Row{
                    Text(
                        text = if(isLangEng) "Available Payment Methods:" else "Métodos de Pago Disponibles:",
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
                    CreatingPayment()
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
                    text = if(isLangEng) "Selected Payment Method:" else "Método de Pago Seleccionado:",
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
        containerColor = themeColors.bgDialog,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.large // Bordes más redondeados
    )
}