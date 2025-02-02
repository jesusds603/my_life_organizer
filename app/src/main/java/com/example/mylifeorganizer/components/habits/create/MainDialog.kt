package com.example.mylifeorganizer.components.habits.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainDialog() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val titleNewHabit = appViewModel.titleNewHabit.value
    val colorNewHabit = appViewModel.colorNewHabit.value
    val namesColor = themeViewModel.namesColorCategories
    val timeForNewHabit = appViewModel.timeForNewHabit.value

    val timeOptions = listOf("any", "morning", "afternoon", "night", "hh:mm")

    AlertDialog(
        onDismissRequest = {
            appViewModel.toggleAddingHabit()
        },
        confirmButton = {
            Button(
                onClick = {
                    appViewModel.toggleAddingHabit()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround3
                )
            ) {
                Text(
                    text = "Aceptar",
                    color = themeColors.text1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    appViewModel.toggleAddingHabit()

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround3
                )
            ) {
                Text(
                    text = "Cancelar",
                    color = themeColors.text1
                )
            }
        },
        title = {
            Text(
                text = "Create Habit",
                color = themeColors.text1
            )
        },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Título
                TextField(
                    value = titleNewHabit,
                    onValueChange = {
                        appViewModel.changeTitleNewHabit(it)
                    },
                    label = {
                        Text(
                            text = "Title",
                            color = themeColors.text2,
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = themeColors.text1,
                        unfocusedTextColor = themeColors.text1,
                        focusedContainerColor = themeColors.backGround2,
                        unfocusedContainerColor = themeColors.backGround2,
                    )
                )

                // Tiempo
                Text(
                    text = "Do it at:",
                    color = themeColors.text1,
                )

                FlowRow (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                ) {
                    timeOptions.forEach { time ->
                        Button(
                            onClick = {
                                appViewModel.changeTimeForNewHabit(time)
//                                if(timeForNewHabit == "hh:mm") {
//
//                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (timeForNewHabit == time) Color.Magenta else themeColors.backGround3
                            )
                        ) {
                            Text(
                                text = time.replaceFirstChar { it.uppercase() }, // Capitaliza la primera letra
                                color = themeColors.text1,
                            )
                        }
                    }
                }


                // Colors
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Color",
                        color = themeColors.text1,
                    )

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(
                                color = themeViewModel.getCategoryColor(colorNewHabit)
                            ),
                    )
                }
                LazyRow {
                    items(namesColor) { color ->
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(
                                    color = themeViewModel.getCategoryColor(color)
                                )
                                .clickable {
                                    appViewModel.changeColorNewHabit(color)
                                }
                        )
                    }
                }
            }
        },
        containerColor = themeColors.backGround2
    )
}