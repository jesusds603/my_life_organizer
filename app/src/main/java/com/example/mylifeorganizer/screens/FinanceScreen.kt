package com.example.mylifeorganizer.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.components.finance.create.WindowDialog
import com.example.mylifeorganizer.components.finance.screen.MainContent
import com.example.mylifeorganizer.components.finance.screen.RowCategoriesFinance
import com.example.mylifeorganizer.components.finance.screen.RowDates
import com.example.mylifeorganizer.components.finance.screen.RowPaymentMethods
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceScreen() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val financeWithCategories: List<FinanceWithCategories> = noteViewModel.financesWithCategories.collectAsState(initial = emptyList()).value

    val isAddingFinance = appViewModel.isAddingFinance.value

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            RowCategoriesFinance()

            RowPaymentMethods()

            RowDates(
                financeWithCategories = financeWithCategories
            )

            MainContent(
                financeWithCategories = financeWithCategories
            )
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
            WindowDialog()
        }

    }
}