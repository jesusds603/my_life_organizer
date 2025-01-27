package com.example.mylifeorganizer.components.tasks.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun ContentMainWindow(
    selectedCategories: List<CategoryTaskEntity>,
    onSelectedCategories: (List<CategoryTaskEntity>) -> Unit,
    availableCategories: List<CategoryTaskEntity>,
    title: String,
    onTitle: (String) -> Unit,
    description: String,
    onDescription: (String) -> Unit,
    onShowCreateCategoryDialog: (Boolean) -> Unit,
    onShowDatePicker: (Boolean) -> Unit,
    onShowTimePicker: (Boolean) -> Unit,
    dueDate: Long,
    dueTime: Long,
    priority: Int,
    onPriority: (Int) -> Unit,
    isRecurring: Boolean,
    onIsRecurring: (Boolean) -> Unit,
    recurrencePattern: String,
    onRecurrencePattern: (String) -> Unit,
    recurrenceInterval: Int,
    onRecurrenceInterval: (Int) -> Unit,
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value


    Column {
        // Campo de título
        TextField(
            value = title,
            onValueChange = { onTitle(it) },
            label = { Text("Title", color = themeColors.text1) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text2,
                focusedContainerColor = themeColors.backGround2,
                unfocusedContainerColor = themeColors.backGround2,
                cursorColor = Color.Magenta
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de descripción
        TextField(
            value = description,
            onValueChange = { onDescription(it) },
            label = { Text("Description", color = themeColors.text1) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = themeColors.text1,
                unfocusedTextColor = themeColors.text2,
                focusedContainerColor = themeColors.backGround2,
                unfocusedContainerColor = themeColors.backGround2,
                cursorColor = Color.Magenta
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            // Botón para seleccionar la fecha
            Button(
                onClick = {
                    onShowDatePicker(true)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(
                    text = if (dueDate > 0) formatDate(dueDate) else "Due Date",
                    color = themeColors.text1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para seleccionar la hora
            Button(
                onClick = {
                    onShowTimePicker(true)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround2
                )
            ) {
                Text(
                    text = if (dueTime > 0) formatTime(dueTime) else "Due Time",
                    color = themeColors.text1
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Seleccionar prioridad
        Text(text = "Priority: $priority", color = themeColors.text1)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            verticalAlignment = Alignment.Bottom // Alineación en la parte inferior
        ) {
            val heights = listOf(0.2f, 0.4f, 0.6f, 0.8f, 1.0f)
            val colors = heights.map { heightPercentage ->
                lerp(Color.Blue, Color.Red, heightPercentage) // Interpolar entre azul y rojo
            }

            heights.forEachIndexed { index, heightPercentage ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .fillMaxHeight(heightPercentage)
                        .background(colors[index]) // Asignar el color interpolado
                        .clickable {
                            onPriority(index + 1)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onIsRecurring(!isRecurring)
                }
        ) {
            Text(
                text = "Recurrence: $recurrencePattern",
                color = themeColors.text1,
            )
        }
        if(isRecurring) {
            LazyRow (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(listOf("Off", "Daily", "Weekly", "Monthly", "Yearly", "Custom")) { recurrence ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(color = themeColors.backGround1)
                            .clickable {
                                onRecurrencePattern(recurrence)
                            }
                    ) {
                        Text(text = recurrence, color = if(recurrence == recurrencePattern) themeColors.tabButtonSelected else themeColors.tabButtonDefault)
                    }

                }
            }
        }

        if(recurrencePattern == "Custom") {
            Text(text = "Recurrence Interval (days): $recurrenceInterval", color = themeColors.text1)
            var expanded by remember { mutableStateOf(false) }
            var selectedValue by remember { mutableStateOf(recurrenceInterval) }

            Box {
                Button(onClick = { expanded = true }) {
                    Text(text = "Select Interval")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // Generar números del 1 al 365
                    (1..365).forEach { number ->
                        DropdownMenuItem(
                            text = { Text(text = "$number") },
                            onClick = {
                                selectedValue = number
                                onRecurrenceInterval(number)
                                expanded = false
                            }
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        // Seleccionar categorías
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = themeColors.backGround1)
                .padding(4.dp)
        ) {
            Text(text = "Categories:", color = themeColors.text1, fontSize = 16.sp)
            Text(text = "Select categories for this task:", color = themeColors.text1, fontSize = 14.sp)
            if (selectedCategories.isNotEmpty()) {
                LazyRow {
                    items(selectedCategories) { category ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(color = themeViewModel.getCategoryColor(category.bgColor))
                                .clickable {
                                    onSelectedCategories(selectedCategories - category)
                                }
                        ) {
                            Text(text = category.name, color = themeColors.text1)
                        }
                    }
                }
            } else {
                Text(text = "No categories selected", color = themeColors.text1)
            }
            Text(text = "Click on a category to select it:", color = themeColors.text1)

            // Fila de categorías disponibles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween // Espacio entre la LazyRow y el Box
            ) {
                LazyRow(
                    modifier = Modifier
                        .weight(1f) // Ocupa el espacio sobrante pero deja espacio para el botón
                        .padding(end = 8.dp) // Espaciado entre la LazyRow y el Box
                ) {
                    items(availableCategories) { category ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(themeViewModel.getCategoryColor(category.bgColor))
                                .clickable {
                                    onSelectedCategories(selectedCategories + category)
                                }
                        ) {
                            Text(text = category.name, color = themeColors.text1)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .wrapContentSize() // Ajusta el Box al contenido
                        .background(Color.Magenta, shape = CircleShape) // Botón con forma circular
                        .clickable {
                            onShowCreateCategoryDialog(true) // Acción del clic
                        }
                        .padding(8.dp) // Espaciado interno del botón
                ) {
                    Text(
                        text = "+",
                        color = themeColors.text1,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center // Alinea el texto dentro del Box
                    )
                }
            }


        }

    }
}