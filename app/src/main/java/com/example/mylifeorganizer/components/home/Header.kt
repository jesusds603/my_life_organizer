package com.example.mylifeorganizer.components.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Header() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val isThemeDark = themeViewModel.isThemeDark.value
    val isLangEng = appViewModel.isLangEng.value

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0x22660066))
            .height(50.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color.Magenta,
                    start = Offset(0f, size.height - strokeWidth / 2),
                    end = Offset(size.width, size.height - strokeWidth / 2),
                    strokeWidth = strokeWidth
                )
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nombre de la App
        Text(
            text = "MyLifeOrganizer",
            color = themeColors.text1,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace
        )

        // Botones de idioma y tema
        Row {
            // Idioma
            TextButton(
                onClick = { appViewModel.toggleLanguage() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_language_24),
                        contentDescription = "Language",
                        modifier = Modifier.size(24.dp), // Ajusta el tamaño del ícono
                        tint = themeColors.tabButtonDefault
                    )
                    Text(
                        text = if (isLangEng) "EN" else "ES",
                        fontSize = 30.sp, // Tamaño de fuente adecuado para combinar con el ícono
                        color = themeColors.text1,
                        modifier = Modifier.padding(start = 8.dp) // Espaciado entre ícono y texto
                    )
                }
            }

            // Tema
            TextButton(
                onClick = { themeViewModel.toggleTheme() }
            ) {
                Text(
                    text = if(isThemeDark) "☀\uFE0F" else "\uD83C\uDF12",
                    fontSize = 30.sp,
                )
            }
        }

    }

}