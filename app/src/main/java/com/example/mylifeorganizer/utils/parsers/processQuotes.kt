package com.example.mylifeorganizer.utils.parsers

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun processQuotes(line: String): AnnotatedString {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val annotatedString = buildAnnotatedString {
        // Expresión regular para detectar citas (>, >>, >>>, etc.)
        val regex = Regex("^(>{1,})\\s*(.*)")
        val matchResult = regex.find(line)

        if (matchResult != null) {
            // Obtener el número de > (nivel de anidación de la cita)
            val numQuotes = matchResult.groupValues[1].length
            // Obtener el texto de la cita (sin los >)
            val quoteText = matchResult.groupValues[2]

            // Definir el estilo según el nivel de anidación
            val textColor = themeColors.text2 // Usar el color del tema para el texto
            val fontFamily = FontFamily.Serif // Usar una fuente serif para las citas

            // Agregar un bloque de cita con estilo
            withStyle(
                SpanStyle(
                    color = textColor,
                    fontSize = 14.sp,
                    fontFamily = fontFamily
                )
            ) {
                // Agregar padding izquierdo para la cita
                append(" ".repeat(numQuotes * 4)) // 4 espacios por nivel de anidación

                // Agregar símbolo de apertura de cita
                append("❝ ")

                // Procesar negritas y cursivas dentro de la cita
                append(processAsterisks(quoteText))

                // Agregar símbolo de cierre de cita
                append(" ❞")
            }

            // Agregar un salto de línea después de la cita
            append("\n")
        } else {
            // Si no es una cita, agregar la línea sin cambios
            append(line)
        }
    }
    return annotatedString
}