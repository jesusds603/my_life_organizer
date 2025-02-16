package com.example.mylifeorganizer.components.tasks.create.recurrence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeColors
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomOptions(
    selectedCustomIntervalNewTask: Int,
    numTimesNewTask: Int,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors: ThemeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value


    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Caja para mostrar el intervalo de días
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .background(themeColors.backGround1)
                .padding(8.dp)
        ) {
            Text(
                text = if(isLangEng) {
                    "Each $selectedCustomIntervalNewTask days"
                } else {
                    "Cada $selectedCustomIntervalNewTask días"
                },
                color = themeColors.text1
            )
        }

        // DropdownMenu para elegir el intervalo de días (1-30)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(themeColors.bgDialog)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            (1..30).forEach { interval ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "$interval ${if(isLangEng) "days" else "días"}",
                            color = themeColors.text1
                        )
                    },
                    onClick = {
                        appViewModel.updateSelectedCustomIntervalNewTask(interval)
                        expanded = false
                    },
                    modifier = Modifier
                        .background(themeColors.backGround1)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // TextField para ingresar el número de repeticiones
        TextField(
            value = numTimesNewTask.toString(),
            onValueChange = {
                // Asegurarse de que solo se ingresen valores numéricos
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    appViewModel.updateNumTimesNewTask(if (it.isEmpty()) 0 else it.toInt())
                }
            },
            label = {
                Text(
                    text = if(isLangEng) "Num. of repetitions" else "Núm. de repeticiones",
                    color = themeColors.text1
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text1,
                focusedContainerColor = themeColors.backGround1,
                unfocusedContainerColor = themeColors.backGround1
            )
        )
    }
}