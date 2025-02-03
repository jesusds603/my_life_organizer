package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectDuration() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val durationForNewHabit = appViewModel.durationForNewHabit.value
    var showDurationSelector by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { showDurationSelector = !showDurationSelector },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(text = "Duration", color = themeColors.text1)
            }
            Text(
                text = if (durationForNewHabit == 0) "No duration" else "$durationForNewHabit min",
                color = themeColors.text1,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        if (showDurationSelector) {
            Text(text = "Select duration (minutes):", color = themeColors.text1)
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items((0..60).toList()) { minute ->
                    Box(
                        modifier = Modifier
                            .clickable {
                                appViewModel.changeDurationForNewHabit(minute)
                                showDurationSelector = false
                            }
                            .padding(4.dp)
                            .background(
                                if (durationForNewHabit == minute) Color.Magenta else themeColors.backGround2
                            )
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
                        Text(
                            text = minute.toString(),
                            color = themeColors.text1
                        )
                    }
                }
            }
        }
    }
}
