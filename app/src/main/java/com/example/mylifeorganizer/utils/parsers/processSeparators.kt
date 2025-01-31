package com.example.mylifeorganizer.utils.parsers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun processSeparators(line: String): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        // Expresión regular para detectar líneas con ***, --- o ___ (posiblemente con espacios)
        val regex = Regex("^\\s*([*_-]{3})\\s*$")
        val matchResult = regex.find(line)

        if (matchResult != null) {
            // Si la línea es un separador, reemplazarla con una línea de separación en color magenta
            withStyle(SpanStyle(color = Color.Magenta)) {
                append("══════════════════════════════════")
            }
        } else {
            // Si no es un separador, agregar la línea sin cambios
            append(line)
        }
    }
    return annotatedString
}