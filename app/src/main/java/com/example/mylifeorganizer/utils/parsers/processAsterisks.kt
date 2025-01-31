package com.example.mylifeorganizer.utils.parsers

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun processAsterisks(line: String): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        // Expresión regular para detectar texto entre asteriscos
        val regex = Regex("(\\*{1,3})(.*?)\\1")
        var lastIndex = 0

        // Buscar coincidencias en la línea
        regex.findAll(line).forEach { match ->
            // Texto antes de la coincidencia
            append(line.substring(lastIndex, match.range.first))

            // Obtener el número de asteriscos y el texto encerrado
            val asterisks = match.groupValues[1]
            val text = match.groupValues[2]

            // Aplicar estilo según el número de asteriscos
            when (asterisks.length) {
                1 -> {
                    // Un asterisco: cursiva
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(text)
                    }
                }
                2 -> {
                    // Dos asteriscos: negrita
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text)
                    }
                }
                3 -> {
                    // Tres asteriscos: negrita y cursiva
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    ) {
                        append(text)
                    }
                }
            }

            // Actualizar el último índice procesado
            lastIndex = match.range.last + 1
        }

        // Agregar el texto restante después de la última coincidencia
        if (lastIndex < line.length) {
            append(line.substring(lastIndex))
        }
    }
    return annotatedString
}