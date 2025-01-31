package com.example.mylifeorganizer.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyMarkdownParserField(
    modifier: Modifier,
    content: String,
    onChangeContent: (String) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val scrollStateEdit = rememberScrollState()
    val scrollStateView = rememberScrollState()

    val isVisualizingNote = appViewModel.isVisualizingNote.value


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

    fun processQuotes(line: String): AnnotatedString {
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

    fun processCodeBlocks(text: String): AnnotatedString {
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 4.dp)
    ) {

        // Contenido principal
        if (isVisualizingNote) {
            Text(
                text = formatText(content),
                color = themeColors.text1,
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollStateView)
                    .padding(4.dp),
                fontSize = 14.sp
            )
        } else {
            TextField(
                value = content,
                onValueChange = {
                    onChangeContent(it)
                },
                textStyle = TextStyle(color = themeColors.text1, fontSize = 14.sp),
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollStateEdit),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = themeColors.text1,
                    unfocusedTextColor = themeColors.text2,
                    focusedContainerColor = themeColors.backGround2,
                    unfocusedContainerColor = themeColors.backGround2,
                    cursorColor = Color.Magenta
                ),
            )
        }
    }
}
