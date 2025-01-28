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
import kotlin.math.sqrt

@Composable
fun HourDial(
    selectedHour: Int,
    onHourSelected: (Int) -> Unit,
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val radiusFractionOuter= 1f
    val radiusFractionInner = 0.5f
    val radiusFractionDiv = 0.7f

    val density = LocalDensity.current
    val outerRadius = 120.dp * radiusFractionOuter
    val innerRadius = 120.dp * radiusFractionInner
    val divRadius = 120.dp * radiusFractionDiv

    val outerRadiusPx = with(density) { outerRadius.toPx() }
    val innerRadiusPx = with(density) { innerRadius.toPx() }
    val divRadiusPx = with(density) { divRadius.toPx() }

    val center = outerRadiusPx
    val itemAngle = 360f / 12 // Divisiones de 12 horas

    Canvas(
        modifier = Modifier
            .size(outerRadius * 2.2f)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val dx = offset.x - center
                    val dy = offset.y - center
                    val distance = sqrt(dx * dx + dy * dy)
                    val angle = Math.toDegrees(atan2(dy, dx).toDouble())
                        .let { if (it < 0) it + 360 else it }

                    val tappedHour = ((angle + 90 - (itemAngle / 2)) % 360 / itemAngle).toInt() + 1

                    if (distance <= divRadiusPx) {
                        onHourSelected(tappedHour)
                    } else {
                        onHourSelected(tappedHour + 12)
                    }
                }
            }
    ) {
        // Dibujar números del círculo interno (1-12)
        for (hour in 1..12) {
            val angle = Math.toRadians((hour * itemAngle - 90).toDouble())
            val x = center + (innerRadiusPx * cos(angle)).toFloat()
            val y = center + (innerRadiusPx * sin(angle)).toFloat()

            drawContext.canvas.nativeCanvas.drawText(
                hour.toString(),
                x,
                y,
                Paint().apply {
                    color = if(hour == selectedHour) Color.Red.toArgb() else themeColors.text1.toArgb()
                    textSize = 50f
                    textAlign = Paint.Align.CENTER
                }
            )
        }

        // Dibujar números del círculo externo (13-24)
        for (hour in 13..24) {
            val angle = Math.toRadians(((hour - 12) * itemAngle - 90).toDouble())
            val x = center + (outerRadiusPx * cos(angle)).toFloat()
            val y = center + (outerRadiusPx * sin(angle)).toFloat()

            drawContext.canvas.nativeCanvas.drawText(
                hour.toString(),
                x,
                y,
                Paint().apply {
                    color = if(hour == selectedHour) Color.Red.toArgb() else themeColors.text1.toArgb()
                    textSize = 50f
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }
}
