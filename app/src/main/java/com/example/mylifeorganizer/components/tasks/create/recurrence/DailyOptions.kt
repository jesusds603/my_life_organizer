package com.example.mylifeorganizer.components.tasks.create.recurrence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeColors
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyOptions(
    numDaysNewTask: Int,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors: ThemeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if(isLangEng) "Num. of days" else "Núm. de días",
            color = themeColors.text1
        )

        // Input para ingresar el número de días
        TextField(
            value = numDaysNewTask.toString(),
            onValueChange = {
                // Solo aceptar valores numéricos
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    appViewModel.updateNumDaysNewTask(if (it.isEmpty()) 0 else it.toInt())
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.width(100.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text1,
                focusedContainerColor = themeColors.backGround1,
                unfocusedContainerColor = themeColors.backGround1
            )
        )
    }
}