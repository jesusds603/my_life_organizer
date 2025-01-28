package com.example.mylifeorganizer.components.tasks.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.room.TaskEntity
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun TaskCard(
    task: TaskEntity
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .background(color = themeColors.backGround1),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(
                        id = if(task.isCompleted) {
                            R.drawable.baseline_check_box_24
                        } else {
                            R.drawable.baseline_check_box_outline_blank_24
                        }
                    ),
                    contentDescription = null,
                    tint = if(task.isCompleted) {
                        Color.Green
                    } else {
                        Color.Gray
                    }
                )
            }

            Spacer(modifier = Modifier.padding(4.dp).background(color = Color.Magenta))

            Column {
                Text(
                    text = task.title,
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = task.description,
                    color = themeColors.text2
                )
            }
        }

        Column {
            Text(
                text = task.dueDateDay,
                color = themeColors.text2,
                fontSize = 12.sp
            )
            Text(
                text = task.dueDateTime,
                color = themeColors.text2,
                fontSize = 12.sp
            )
        }
    }
}