package com.example.mylifeorganizer.utils.parsers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

fun processTitles(line: String): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        // Verificamos si la línea empieza con uno o más #
        val matchResult = Regex("^(#{1,6})\\s+(.*)").find(line)

        if (matchResult != null) {
            // Obtener el número de numerales
            val numHashes = matchResult.groupValues[1].length
            // Obtener el texto sin los numerales
            val lineText = matchResult.groupValues[2]

            // Definir el tamaño de fuente en función del número de numerales
            val fontSize = when (numHashes) {
                1 -> 36.sp
                2 -> 32.sp
                3 -> 28.sp
                4 -> 24.sp
                5 -> 20.sp
                6 -> 16.sp
                else -> 12.sp // Tamaño por defecto
            }

            // Aplicar estilo al texto (negrita, tamaño y color magenta)
            withStyle(
                style = SpanStyle(
                    color = Color.Magenta,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize
                )
            ) {
                append(lineText)
            }
        } else {
            // Si no es un título, agregar la línea sin cambios
            append(line)
        }
    }
    return annotatedString
}