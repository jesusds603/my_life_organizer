package com.example.mylifeorganizer.components.tasks.create

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun CircleDial(
    items: List<Int>,
    selectedValue: Int,
    onValueSelected: (Int) -> Unit,
    isActive: Boolean,
    radiusFraction: Float = 1f, // Controla el radio del círculo
    isMinuteDial: Boolean = false, // Indica si es un dial de minutos
    isOuterCircle: Boolean = false, // Indica si es el círculo externo de horas
    showDotsForMissing: Boolean = false // Si es true, muestra puntos para los valores faltantes
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Radios para los círculos
    val innerRadius = 80.dp * radiusFraction // Círculo interno para horas 1-12
    val outerRadius = 120.dp * radiusFraction // Círculo externo para horas 13-24 o minutos
    val averageRadius = (innerRadius + outerRadius) / 2 // Punto medio entre ambos círculos

    // Convertir radios a píxeles
    val density = LocalDensity.current
    val innerRadiusPx = with(density) { innerRadius.toPx() }
    val outerRadiusPx = with(density) { outerRadius.toPx() }
    val averageRadiusPx = with(density) { averageRadius.toPx() }

    val center = outerRadiusPx
    val itemAngle = if (isMinuteDial) 360f / 60 else 360f / items.size

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(outerRadius * 2)
            .graphicsLayer { alpha = if (isActive) 1f else 0.4f } // Atenúa el círculo inactivo
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Calcular la distancia desde el clic al centro
                        val dx = offset.x - center
                        val dy = offset.y - center
                        val distance = sqrt(dx * dx + dy * dy)

                        // Calcular el ángulo del clic
                        val tappedAngle = Math.toDegrees(atan2(dy, dx).toDouble())
                            .let { if (it < 0) it + 360 else it }

                        // Determinar el círculo seleccionado en caso de horas
                        val itemsToUse = if (!isMinuteDial && distance > averageRadiusPx) {
                            // Círculo externo
                            if (isOuterCircle) items else emptyList()
                        } else if (!isMinuteDial && distance <= averageRadiusPx) {
                            // Círculo interno
                            if (!isOuterCircle) items else emptyList()
                        } else {
                            // Círculo de minutos
                            items
                        }

                        // Calcular el índice seleccionado
                        if (itemsToUse.isNotEmpty()) {
                            val tappedIndex = ((tappedAngle + 90 + (itemAngle / 2)) % 360 / itemAngle).toInt()
                            val adjustedIndex = tappedIndex % itemsToUse.size
                            onValueSelected(itemsToUse[adjustedIndex])
                        }
                    }
                }
        ) {
            items.forEachIndexed { index, item ->
                val angle = Math.toRadians((index * itemAngle - 90).toDouble())
                val radiusToUse = if (isMinuteDial || isOuterCircle) outerRadiusPx else innerRadiusPx
                val x = center + (radiusToUse * cos(angle)).toFloat()
                val y = center + (radiusToUse * sin(angle)).toFloat()

                drawContext.canvas.nativeCanvas.apply {
                    if (isMinuteDial && showDotsForMissing && item % 5 != 0) {
                        // Dibuja un punto para minutos no múltiplos de 5
                        drawCircle(
                            x,
                            y,
                            4f,
                            Paint().apply {
                                color = if (item == selectedValue) Color.Red.toArgb() else themeColors.text1.toArgb()
                            }
                        )
                    } else {
                        // Dibuja el texto para las horas o los minutos (múltiplos de 5)
                        drawText(
                            item.toString(),
                            x,
                            y,
                            Paint().apply {
                                textAlign = Paint.Align.CENTER
                                textSize = 32f
                                color = if (item == selectedValue) Color.Red.toArgb() else themeColors.text1.toArgb()
                                isFakeBoldText = true
                            }
                        )
                    }
                }
            }
        }
    }
}
