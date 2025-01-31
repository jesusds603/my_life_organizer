package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowDates() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var selectedPeriodFinanceScreen by remember { mutableStateOf("Month") } // "Day", "Week", "Month", "Year", "All"
    var showDialog by remember { mutableStateOf(false) }

    Row {

        TextButton(
            onClick = {
                showDialog = true
            }
        ) {
            Text(
                text = selectedPeriodFinanceScreen,
                color = themeColors.text1
            )
        }

    }
}