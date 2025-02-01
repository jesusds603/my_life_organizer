package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceCard(
    finance: FinanceWithCategories,
    paymentName: String,
    paymentColor: String
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Formato de fecha
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val monthFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    // Obtener color de fondo dependiendo del tipo (income/expense)
    val backgroundColor = if (finance.finance.type == "income") {
        themeColors.bgIncome
    } else {
        themeColors.bgExpense
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Título de la tarjeta
            Text(
                text = finance.finance.title,
                color = themeColors.text1,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )


            // Monto de la tarjeta
            Box(
                modifier = Modifier
                    .padding(1.dp)
                    .background(themeColors.backGround1)
                    .padding(2.dp)

            ) {
                Text(
                    text = "\$ ${finance.finance.amount}",
                    color = themeColors.text1,
                    fontSize = 16.sp
                )
            }

        }

        Spacer(modifier = Modifier.height(4.dp))

        // Método de pago de la tarjeta
        Box(
            modifier = Modifier
                .padding(1.dp)
                .background(themeViewModel.getCategoryColor(paymentColor))
                .padding(1.dp)
        ) {
            Text(
                text = paymentName,
                color = themeColors.text1,
            )
        }


        Spacer(modifier = Modifier.height(4.dp))

        // Categorias
        LazyRow {
            items(finance.categories) { category ->
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .background(themeViewModel.getCategoryColor(category.bgColor))
                        .padding(1.dp)

                ) {
                    Text(
                        text = category.name,
                        color = themeColors.text1,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}