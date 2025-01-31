package com.example.mylifeorganizer.utils.parsers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun processCodeBlocks(text: String): AnnotatedString {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val annotatedString = buildAnnotatedString {
        // Expresión regular para detectar bloques de código (```)
        val regex = Regex("```([\\s\\S]*?)```|~~~([\\s\\S]*?)~~~", RegexOption.DOT_MATCHES_ALL)
        val matches = regex.findAll(text)

        var lastIndex = 0

        // Procesar cada bloque de código encontrado
        matches.forEach { match ->
            // Agregar el texto antes del bloque de código
            append(text.substring(lastIndex, match.range.first))

            // Obtener el contenido del bloque de código (sin los ```)
            val codeContent = match.groupValues[1].trim()

            // Aplicar estilo al bloque de código
            withStyle(
                SpanStyle(
                    background = themeColors.backGround2, // Fondo ligero
                    color = themeColors.text1, // Color del texto
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                // Agregar padding superior
                append("\n")

                // Agregar etiqueta de inicio </> en azul
                withStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                    append("</>────\n")
                }

                // Agregar el contenido del bloque de código con espacios y estilo
                codeContent.lines().forEach { line ->
                    append("    $line\n")
                }

                // Agregar etiqueta de cierre </> en azul
                withStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                    append("────</>\n")
                }
            }

            // Actualizar el último índice procesado
            lastIndex = match.range.last + 1
        }

        // Agregar el texto restante después del último bloque de código
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
    return annotatedString
}