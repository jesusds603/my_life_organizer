package com.example.mylifeorganizer.components.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun RowSelector(
    selectedYear: Int,
    onYearChange: (Int) -> Unit,
    selectedMonth: Int,
    onMonthChange: (Int) -> Unit,
    monthString: String
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Selector de Año y Mes
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Año con flechas
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
                contentDescription = "Año anterior",
                tint = themeColors.text1,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onYearChange(selectedYear - 1) }
            )
            Text(
                text = selectedYear.toString(),
                color = themeColors.text1
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                contentDescription = "Año siguiente",
                tint = themeColors.text1,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onYearChange(selectedYear + 1) }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Mes con flechas
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
                contentDescription = "Mes anterior",
                tint = themeColors.text1,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        if (selectedMonth == 1) {
                            onMonthChange(12)
                            onYearChange(selectedYear - 1)
                        } else {
                            onMonthChange(selectedMonth - 1)
                        }
                    }
            )
            Text(
                text = monthString,
                color = themeColors.text1
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                contentDescription = "Mes siguiente",
                tint = themeColors.text1,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        if (selectedMonth == 12) {
                            onMonthChange(1)
                            onYearChange(selectedYear + 1)
                        } else {
                            onMonthChange(selectedMonth + 1)
                        }
                    }
            )
        }
    }
}