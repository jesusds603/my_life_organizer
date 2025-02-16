package com.example.mylifeorganizer.components.tasks.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PriorityTask (

) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val priorityNewTask = appViewModel.priorityNewTask

    val heights = listOf(0.2f, 0.4f, 0.6f, 0.8f, 1.0f)
    val colors = heights.map { heightPercentage ->
        lerp(Color.Blue, Color.Red, heightPercentage) // Interpolar entre azul y rojo
    }

    // Seleccionar prioridad
    Row {
        Text(
            text = "${if(isLangEng) "Priority" else "Prioridad"}: $priorityNewTask  ",
            color = themeColors.text1
        )

        Box(
            modifier = Modifier
                .size(15.dp)
                .background(
                    color = if(priorityNewTask > 0) {
                        colors[priorityNewTask - 1]
                    } else {
                        Color.Gray
                    }
                )
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.Bottom // Alineación en la parte inferior
    ) {


        heights.forEachIndexed { index, heightPercentage ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight(heightPercentage)
                    .background(colors[index]) // Asignar el color interpolado
                    .clickable {
                        appViewModel.updatePriorityNewTask(index+1)
                    }
            )
        }
    }
}