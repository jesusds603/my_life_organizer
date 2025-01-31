package com.example.mylifeorganizer.utils.parsers

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString

@Composable
fun formatText(text: String): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        // Expresión regular para detectar bloques de código (```)
        val regex = Regex("```([\\s\\S]*?)```|~~~([\\s\\S]*?)~~~", RegexOption.DOT_MATCHES_ALL)
        val matches = regex.findAll(text)

        var lastIndex = 0
        val blocks = mutableListOf<Pair<Boolean, String>>() // Lista de bloques (código o no código)

        // Dividir el texto en bloques de código y no código
        matches.forEach { match ->
            // Agregar el texto antes del bloque de código como no código
            if (match.range.first > lastIndex) {
                blocks.add(false to text.substring(lastIndex, match.range.first))
            }

            // Agregar el bloque de código
            blocks.add(true to match.groupValues[1].trim())

            // Actualizar el último índice procesado
            lastIndex = match.range.last + 1
        }

        // Agregar el texto restante después del último bloque de código como no código
        if (lastIndex < text.length) {
            blocks.add(false to text.substring(lastIndex))
        }

        // Procesar cada bloque según su tipo
        blocks.forEach { (isCodeBlock, content) ->
            if (isCodeBlock) {
                // Si es un bloque de código, aplicar processCodeBlocks
                append(processCodeBlocks("```$content```"))
            } else {
                // Si no es un bloque de código, dividir en líneas y aplicar los demás formatos
                val lines = content.split("\n")

                lines.forEach { line ->
                    // Procesar separadores (***, ---, ___) primero
                    val separatorLine = processSeparators(line)

                    if (separatorLine.text != line) {
                        // Si es un separador, agregar la línea formateada
                        append(separatorLine)
                    } else {
                        // Si no es un separador, procesar títulos, citas, asteriscos y listas
                        val titleLine = processTitles(line)

                        if (titleLine.text != line) {
                            // Si es un título, agregar la línea formateada
                            append(titleLine)
                        } else {
                            // Procesar citas (líneas que comienzan con >)
                            val quoteLine = processQuotes(line)

                            if (quoteLine.text != line) {
                                // Si es una cita, agregar la línea formateada
                                append(quoteLine)
                            } else {
                                // Si no es una cita, procesar asteriscos y listas
                                val asterisksLine = processAsterisks(line)
                                val listLine = processLists(asterisksLine)
                                append(listLine)
                            }
                        }
                    }

                    // Agregar un salto de línea después de cada línea
                    append("\n")
                }
            }
        }
    }
    return annotatedString
}