package com.example.mylifeorganizer.components.finance.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.room.PaymentMethodEntity
import com.example.mylifeorganizer.viewmodel.ThemeViewModel



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceCard(
    finance: FinanceWithCategories,
    paymentMethod: PaymentMethodEntity
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var showMenu by remember { mutableStateOf(false) }

    // Obtener color de fondo dependiendo del tipo (income/expense)
    val backgroundColor = if(showMenu) {
        themeColors.backGround4
    } else {
        if (finance.finance.type == "income") {
            themeColors.bgIncome
        } else {
            themeColors.bgExpense
        }
    }


    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showMenu = true},
                    onTap = {}
                )
            }
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

            // Método de pago de la tarjeta
            Box(
                modifier = Modifier
                    .padding(1.dp)
                    .background(themeViewModel.getCategoryColor(paymentMethod.bgColor))
                    .padding(1.dp)
            ) {
                Text(
                    text = paymentMethod.name,
                    color = themeColors.text1,
                )
            }

        }

        Spacer(modifier = Modifier.height(2.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Categorias
            LazyRow (
                modifier = Modifier
                    .weight(1f)
            ) {
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


        FloatingOptionsFinance(
            finance = finance,
            showMenu = showMenu,
            changeShowMenu = { showMenu = it},
            paymentMethod = paymentMethod
        )
    }
}