package com.example.mylifeorganizer.components.tasks.create

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MinuteDial(
    selectedMinute: Int,
    onMinuteSelected: (Int) -> Unit,
) {
    val radiusFraction = 1f // Tamaño del círculo
    val radiusInnerFraction = 0.8f // Tamaño del círculo interno
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val radius = 120.dp * radiusFraction
    val radiusInner = 120.dp * radiusInnerFraction
    val density = LocalDensity.current
    val radiusPx = with(density) { radius.toPx() }
    val radiusInnerPx = with(density) { radiusInner.toPx() }
    val center = radiusPx
    val itemAngle = 360f / 60 // Divisiones de 60 minutos

    Canvas(
        modifier = Modifier
            .size(radius * 2.2f)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    // Detectar la posición tocada
                    val dx = offset.x - center
                    val dy = offset.y - center
                    val angle = Math.toDegrees(atan2(dy, dx).toDouble())
                        .let { if (it < 0) it + 360 else it }

                    val tappedMinute = ((angle + 90 - (itemAngle / 2)) % 360 / itemAngle).toInt() + 1
                    onMinuteSelected(tappedMinute)
                }
            }
    ) {
        // Dibujar números y puntos
        for (minute in 0 until 60) {
            val angle = Math.toRadians((minute * itemAngle - 90).toDouble())
            val x = center + (radiusPx * cos(angle)).toFloat()
            val y = center + (radiusPx * sin(angle)).toFloat()
            val xInner = center + (radiusInnerPx * cos(angle)).toFloat()
            val yInner = center + (radiusInnerPx * sin(angle)).toFloat()

            if (minute % 5 == 0) {
                // Dibujar números (múltiplos de 5)
                drawContext.canvas.nativeCanvas.drawText(
                    minute.toString(),
                    x,
                    y,
                    Paint().apply {
                        color =
                            if (minute == selectedMinute) Color.Red.toArgb() else themeColors.text1.toArgb()
                        textSize = 50f
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
            else {
                // Dibujar puntos
                drawCircle(
                    color = if(minute == selectedMinute) Color.Red else themeColors.text1,
                    radius = 7f,
                    center = Offset(xInner, yInner)
                )
            }
        }
    }
}
