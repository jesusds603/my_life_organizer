package com.example.mylifeorganizer.utils.parsers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

fun processLists(line: AnnotatedString, spaceMultiplier: Int = 2): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        // Expresión regular para detectar listas no ordenadas (guiones) o numeradas (números)
        val regex = Regex("^(\\s*)([-*+]|\\d+\\.)\\s*(.*)")
        val matchResult = regex.find(line.text)

        if (matchResult != null) {
            // Obtener los espacios antes del símbolo o número
            val spacesBefore = matchResult.groupValues[1]
            // Obtener el símbolo o número (guion, asterisco, signo de más o número con punto)
            val symbolOrNumber = matchResult.groupValues[2]
            // Obtener el texto después del símbolo o número
            val listText = matchResult.groupValues[3]

            // Obtener el nivel de anidación (basado en la cantidad de espacios antes del símbolo o número)
            val level = spacesBefore.length / 2 // Cada 2 espacios es un nivel

            // Calcular los espacios multiplicados según el nivel
            val multipliedSpaces = " ".repeat(spacesBefore.length * spaceMultiplier)

            // Definir el estilo según el tipo de lista y el nivel de anidación
            when {
                // Listas no ordenadas (guiones, asteriscos o signos de más)
                symbolOrNumber.matches(Regex("[-*+]")) -> {
                    val (symbol, color, fontSize) = when (level % 3) { // Ciclar cada 3 niveles
                        0 -> Triple("✦", Color.Magenta, 18.sp) // Nivel 0: estrella, magenta, tamaño 18.sp
                        1 -> Triple("●", Color.Blue, 18.sp)   // Nivel 1: círculo, azul, tamaño 18.sp
                        2 -> Triple("✵", Color.Green, 18.sp)  // Nivel 2: estrella, verde, tamaño 18.sp
                        else -> Triple("✦", Color.Magenta, 18.sp) // Por defecto: estrella, magenta, tamaño 18.sp
                    }

                    // Agregar los espacios multiplicados antes del símbolo
                    append(multipliedSpaces)

                    // Aplicar estilo al símbolo (tamaño y color)
                    withStyle(SpanStyle(color = color, fontSize = fontSize)) {
                        append("$symbol ")
                    }
                }

                // Listas numeradas (números con punto)
                symbolOrNumber.matches(Regex("\\d+\\.")) -> {
                    val color = when (level % 3) { // Ciclar cada 3 niveles
                        0 -> Color.Magenta // Nivel 0: magenta
                        1 -> Color.Blue    // Nivel 1: azul
                        2 -> Color.Green   // Nivel 2: verde
                        else -> Color.Magenta // Por defecto: magenta
                    }

                    // Agregar los espacios multiplicados antes del número
                    append(multipliedSpaces)

                    // Aplicar estilo al número (negrita y color)
                    withStyle(SpanStyle(color = color, fontWeight = FontWeight.Bold)) {
                        append("$symbolOrNumber ")
                    }
                }
            }

            // Agregar el texto de la lista
            append(listText)
        } else {
            // Si no es una lista, agregar la línea sin cambios
            append(line)
        }
    }
    return annotatedString
}