package com.example.mylifeorganizer.components.calendar

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCalendarWindow() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value

    val selectedDateCalendar = appViewModel.selectedDateCalendar.value

    var isShowingHabits by remember { mutableStateOf(false) }
    var isShowingTasks by remember { mutableStateOf(false) }
    var isShowingFinances by remember { mutableStateOf(false) }

    val habitsOccurrencesForDay = noteViewModel.getHabitOccurrencesByDate(
        selectedDateCalendar.format(
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
        )
    ).collectAsState(initial = emptyList()).value
    val habits = noteViewModel.habits.collectAsState(initial = emptyList()).value

    val tasksOccurrencesForDay = noteViewModel.getOccurrencesTasksByDate(
        selectedDateCalendar.format(
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
        )
    ).collectAsState(initial = emptyList()).value
    val tasks = noteViewModel.tasksWithCategories.collectAsState(initial = emptyList()).value

    val finances = noteViewModel.getFinancesByDate(
        selectedDateCalendar.format(
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
        )
    ).collectAsState(initial = emptyList()).value

    val totalExpense = finances.filter { it.finance.type == "expense" }.sumOf { it.finance.amount }
    val totalIncome = finances.filter { it.finance.type == "income" }.sumOf { it.finance.amount }
    val balance = totalIncome - totalExpense

    BackHandler {
        appViewModel.toggleShowingDayCalendar()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = selectedDateCalendar.format(DateTimeFormatter.ofPattern("yyyy/MMM/dd")),
            color = themeColors.text1,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = themeColors.backGround2
                )
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Habits",
                color = themeColors.text1,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    isShowingHabits = !isShowingHabits
                },
                modifier = Modifier
                    .background(
                        color = themeColors.backGround4,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(
                        id = if(isShowingHabits) {
                            R.drawable.baseline_keyboard_arrow_down_24
                        } else {
                            R.drawable.baseline_keyboard_arrow_right_24
                        }
                    ),
                    contentDescription = "",
                    tint = themeColors.text1,
                )
            }
        }

        if(isShowingHabits) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                habitsOccurrencesForDay.forEach { occurrence ->
                    val habit = habits.find { occurrence.habitId == it.habitId }

                    item {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = {
                                    noteViewModel.updateHabitOccurrence(
                                        occurrence.copy(isCompleted = !occurrence.isCompleted)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.1f)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (occurrence.isCompleted) {
                                            R.drawable.baseline_check_box_24
                                        } else {
                                            R.drawable.baseline_check_box_outline_blank_24
                                        }
                                    ),
                                    contentDescription = null,
                                    tint = if (occurrence.isCompleted) Color.Green else Color.Gray
                                )
                            }

                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = themeViewModel.getCategoryColor(habit?.color ?: "")
                                    )
                                    .padding(horizontal = 2.dp)
                            ) {
                                Text(
                                    text = habit?.title ?: "Unknown habit",
                                    color = themeColors.text1,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = occurrence.time,
                                    color = themeColors.text1
                                )
                            }
                        }
                    }
                }
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = themeColors.backGround2
                )
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tasks",
                color = themeColors.text1,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    isShowingTasks = !isShowingTasks
                },
                modifier = Modifier
                    .background(
                        color = themeColors.backGround4,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(
                        id = if(isShowingTasks) {
                            R.drawable.baseline_keyboard_arrow_down_24
                        } else {
                            R.drawable.baseline_keyboard_arrow_right_24
                        }
                    ),
                    contentDescription = "",
                    tint = themeColors.text1,
                )
            }
        }

        if(isShowingTasks) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                tasksOccurrencesForDay.forEach { occurrence ->
                    val task = tasks.find { occurrence.taskId == it.task.taskId }

                    item {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = {
                                    noteViewModel.updateOccurrence(
                                        occurrence.copy(isCompleted = !occurrence.isCompleted)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.1f)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (occurrence.isCompleted) {
                                            R.drawable.baseline_check_box_24
                                        } else {
                                            R.drawable.baseline_check_box_outline_blank_24
                                        }
                                    ),
                                    contentDescription = null,
                                    tint = if (occurrence.isCompleted) Color.Green else Color.Gray
                                )
                            }

                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = themeColors.backGround4
                                    )
                                    .padding(horizontal = 2.dp)
                            ) {
                                Text(
                                    text = task?.task?.title ?: "Unknown task",
                                    color = themeColors.text1,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = occurrence.dueTime,
                                    color = themeColors.text1
                                )
                            }
                        }
                    }
                }
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = themeColors.backGround2
                )
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = "Finances",
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$ $totalIncome",
                    color = themeColors.text1,
                    modifier = Modifier
                        .background(color = themeColors.bgIncome)
                        .padding(2.dp)
                )

                Text(
                    text = "$ $totalExpense",
                    color = themeColors.text1,
                    modifier = Modifier
                        .background(color = themeColors.bgExpense)
                        .padding(2.dp)
                )

                Text(
                    text = "$ $balance",
                    color = themeColors.text1,
                    modifier = Modifier
                        .background(color = themeColors.backGround1)
                        .padding(2.dp)
                )
            }

            IconButton(
                onClick = {
                    isShowingFinances = !isShowingFinances
                },
                modifier = Modifier
                    .background(
                        color = themeColors.backGround4,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(
                        id = if(isShowingFinances) {
                            R.drawable.baseline_keyboard_arrow_down_24
                        } else {
                            R.drawable.baseline_keyboard_arrow_right_24
                        }
                    ),
                    contentDescription = "",
                    tint = themeColors.text1,
                )
            }
        }

        if (isShowingFinances) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                finances.forEach { finance ->
                    item {
                        Column (
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .background(
                                     color = if(finance.finance.type == "expense") {
                                         themeColors.bgExpense
                                     } else {
                                         themeColors.bgIncome
                                     }
                                 )
                                 .padding(4.dp)
                        ) {
                            Text(
                                text = finance.finance.title,
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = finance.finance.description,
                                color = themeColors.text1
                            )

                            Text(
                                text = finance.finance.amount.toString(),
                                color = themeColors.text1,
                                modifier = Modifier
                                    .background(color = themeColors.backGround1)
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}