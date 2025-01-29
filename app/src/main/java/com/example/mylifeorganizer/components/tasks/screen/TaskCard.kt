package com.example.mylifeorganizer.components.tasks.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.room.TaskEntity
import com.example.mylifeorganizer.room.TaskWithCategories
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun TaskCard(
    task: TaskWithCategories
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    val heights = listOf(0.2f, 0.4f, 0.6f, 0.8f, 1.0f)
    val colors = heights.map { heightPercentage ->
        lerp(Color.Blue, Color.Red, heightPercentage) // Interpolar entre azul y rojo
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .background(color = themeColors.backGround1)
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 1.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(
                        id = if(task.task.isCompleted) {
                            R.drawable.baseline_check_box_24
                        } else {
                            R.drawable.baseline_check_box_outline_blank_24
                        }
                    ),
                    contentDescription = null,
                    tint = if(task.task.isCompleted) {
                        Color.Green
                    } else {
                        Color.Gray
                    }
                )
            }

            Spacer(modifier = Modifier.padding(4.dp).background(color = Color.Magenta))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = task.task.title,
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold
                )

                LazyRow {
                    items(task.categories) { category ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp, vertical = 0.dp)
                                .background(
                                    color = themeViewModel.getCategoryColor(category.bgColor)
                                )
                                .padding(horizontal = 3.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = category.name,
                                color = themeColors.text1,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .width(80.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Priority: ",
                    color = themeColors.text1,
                    fontSize = 10.sp
                )
                Text(
                    text = task.task.priority.toString(),
                    color = themeColors.text1,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(colors[task.task.priority-1])
                        .padding(horizontal = 3.dp, vertical = 0.dp)
                )

            }

            Text(
                text = task.task.dueDateDay,
                color = themeColors.text2,
                fontSize = 12.sp
            )
            Text(
                text = task.task.dueDateTime,
                color = themeColors.text2,
                fontSize = 12.sp
            )


        }
    }
}