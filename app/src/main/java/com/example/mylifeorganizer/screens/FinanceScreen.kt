package com.example.mylifeorganizer.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val isAddingFinance = appViewModel.isAddingFinance.value

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxSize()
        ) {

        }

        FloatingActionButton(
            onClick = {
                appViewModel.toggleAddingFinance()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Ubicación en la esquina inferior derecha
                .padding(16.dp),
            containerColor = themeColors.buttonAdd,
            shape = CircleShape
        ) {
            Text("+", fontSize = 24.sp, color = themeColors.text1)
        }

        if (isAddingFinance) {
            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var amount by remember { mutableStateOf("") }
            var date by remember { mutableStateOf("Today") }
            var isExpense by remember { mutableStateOf(true) } // Estado para alternar tipo

            AlertDialog(
                onDismissRequest = {

                },
                confirmButton = {
                    Button(
                        onClick = { appViewModel.toggleAddingFinance() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColors.backGround2
                        )
                    ) {
                        Text(text = "Add Finance", color = themeColors.text1)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { appViewModel.toggleAddingFinance() },
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
                    Column (
                    ) {
                        TextField(
                            value = title,
                            onValueChange = { title = it },
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
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description", color = themeColors.text1) },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = themeColors.text1,
                                unfocusedTextColor = themeColors.text1,
                                focusedContainerColor = themeColors.backGround1,
                                unfocusedContainerColor = themeColors.backGround1,
                            )
                        )
                        TextField(
                            value = amount,
                            onValueChange = { amount = it.filter { c -> c.isDigit() || c == '.' } },
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
                        Text("Date (yyyy/MM/dd)", color = themeColors.text1)
                        TextField(
                            value = date,
                            onValueChange = { date = it },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = themeColors.text1,
                                unfocusedTextColor = themeColors.text1,
                                focusedContainerColor = themeColors.backGround1,
                                unfocusedContainerColor = themeColors.backGround1,
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón para alternar entre Expense e Income
                        Button(
                            onClick = { isExpense = !isExpense },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = if(isExpense) themeColors.text1 else Color.Black,
                                containerColor = if (isExpense) Color.Red else Color.Green
                            )
                        ) {
                            Text(
                                text = if (isExpense) "Expense" else "Income",
                                color = if(isExpense) Color.White else Color.Black
                            )
                        }
                    }
                },
                containerColor = themeColors.backGround3,
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.large // Bordes más redondeados
            )
        }

    }
}