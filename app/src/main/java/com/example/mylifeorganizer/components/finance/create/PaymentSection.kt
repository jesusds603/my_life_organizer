package com.example.mylifeorganizer.components.finance.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentSection() {
    val themeViewModel: ThemeViewModel = viewModel()
    val appViewModel: AppViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val paymentMethodForNewFinance = appViewModel.paymentMethodForNewFinance


    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = if(isLangEng) "Payment Method: " else "Método de Pago: ",
            color = themeColors.text1
        )

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
            .background(
                themeViewModel.getCategoryColor(
                    paymentMethodForNewFinance?.bgColor ?: ""
                )
            )
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(
            text = paymentMethodForNewFinance?.name ?: "",
            color = themeColors.text1,
            fontWeight = FontWeight.Bold
        )
    }
}