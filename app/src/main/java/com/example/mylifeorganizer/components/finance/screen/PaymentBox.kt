package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentBox(
    name: String,
    bgColor: String,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .background(
                color = if(name == "All") {
                    themeColors.backGround1
                } else {
                    themeViewModel.getCategoryColor(bgColor)
                }
            )
            .padding(horizontal = 8.dp, vertical = 1.dp)
            .clickable {
                appViewModel.updateSelectedPaymentMethodForFinanceScreen(name)
            }
    ) {
        Text(
            text = name,
            color = themeColors.text1
        )
    }
}