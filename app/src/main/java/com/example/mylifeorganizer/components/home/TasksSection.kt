package com.example.mylifeorganizer.components.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksSection(
    modifier: Modifier
) {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

    val tasks = noteViewModel.tasksWithCategories.collectAsState(initial = emptyList()).value
    val tasksOccurrencesForToday by remember { noteViewModel.getOccurrencesTasksByDate(todayDate) }.collectAsState(initial = emptyList())

    Column (
        modifier = modifier
            .fillMaxSize()

    ) {
        Text(
            text = if(isLangEng) "Tasks for today" else "Tareas para hoy",
            color = themeColors.text1,
        )

        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            tasksOccurrencesForToday.forEach { occurrence ->
                val task = tasks.find { occurrence.taskId == it.task.taskId }

                item {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0x22cc0099)
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                noteViewModel.updateOccurrence(
                                    occurrence.copy(isCompleted = !occurrence.isCompleted)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.2f)
                                .fillMaxHeight()
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
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = task?.task?.title ?: if(isLangEng) "Unknown task" else "Tarea desconodida",
                                color = themeColors.text1,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = occurrence.dueTime,
                                color = themeColors.text1
                            )

                            LazyRow {
                                task?.categories?.forEach { category ->
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = 1.dp)
                                                .background(
                                                    color = themeViewModel.getCategoryColor(
                                                        category.bgColor
                                                    )
                                                )
                                                .padding(horizontal = 1.dp)
                                        ) {
                                            Text(
                                                text = category.name,
                                                color = themeColors.text1,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}