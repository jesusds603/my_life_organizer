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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun CircleDial(
    items: List<Int>,
    selectedValue: Int,
    onValueSelected: (Int) -> Unit,
    isActive: Boolean,
    radiusFraction: Float = 1f // Controls the radius of the circle
) {
    val radius = 120.dp * radiusFraction
    val center = with(LocalDensity.current) { radius.toPx() }
    val itemAngle = 360f / items.size

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(radius * 2)
            .graphicsLayer { alpha = if (isActive) 1f else 0.4f } // Dim inactive circle
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            items.forEachIndexed { index, item ->
                val angle = Math.toRadians((index * itemAngle - 90).toDouble())
                val x = center + (center * cos(angle)).toFloat()
                val y = center + (center * sin(angle)).toFloat()

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        item.toString(),
                        x,
                        y,
                        Paint().apply {
                            color = if (item == selectedValue) Color.Red.toArgb() else Color.Black.toArgb()
                            textSize = 36f
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }
            }
        }

        Modifier.pointerInput(Unit) {
            detectTapGestures { offset ->
                val tappedAngle = Math.toDegrees(
                    atan2(offset.y - center, offset.x - center).toDouble()
                ).let { if (it < 0) it + 360 else it }

                val tappedIndex = ((tappedAngle / itemAngle).roundToInt()) % items.size
                onValueSelected(items[tappedIndex])
            }
        }
    }
}
