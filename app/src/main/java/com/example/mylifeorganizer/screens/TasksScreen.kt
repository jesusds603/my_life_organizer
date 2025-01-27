package com.example.mylifeorganizer.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TasksScreen (
    noteViewModel: NoteViewModel
) {
    val appViewModel: AppViewModel = viewModel()

    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Estado para controlar la visibilidad del cuadro de diálogo
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(0L) }
    var dueTime by remember { mutableStateOf(0L) }
    var priority by remember { mutableStateOf(1) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedCategories by remember { mutableStateOf<List<CategoryTaskEntity>>(emptyList()) }
    var showCreateCategoryDialog by remember { mutableStateOf(false) }
    // Lista de nombres de colores
    val namesColors = themeViewModel.namesColorCategories

    var newCategoryName by remember { mutableStateOf("") }
    var newCategoryColor by remember { mutableStateOf("") }

    val availableCategories by noteViewModel.categoriesTasks.collectAsState(emptyList())
    var isRecurring by remember { mutableStateOf(false) }
    var recurrencePattern by remember { mutableStateOf("Off") }
    var recurrenceInterval by remember { mutableStateOf(0) }

    val context = LocalContext.current


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {

        }

        // Botón flotante para agregar nuevas tareas
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Ubicación en la esquina inferior derecha
                .padding(16.dp),
            containerColor = themeColors.buttonAdd,
            shape = CircleShape
        ) {
            Text("+", fontSize = 24.sp, color = themeColors.text1)
        }

        // Ventana de diálogo para agregar tareas
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColors.backGround2
                        )
                    ) {
                        Text(text = "Add Task", color = themeColors.text1)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColors.backGround2
                        )
                    ) {
                        Text(text = "Cancel", color = themeColors.text1)
                    }
                },
                title = { Text(
                    text = "Add New Task",
                    color = themeColors.text1
                ) },
                text = {
                    Column {
                        // Campo de título
                        TextField(
                            value = title,
                            onValueChange = { title = it },
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
                            onValueChange = { description = it },
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
                                    showDatePicker = true
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
                                    showTimePicker = true
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
                                            priority = index + 1
                                        }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isRecurring = !isRecurring
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
                                                recurrencePattern = recurrence
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
                                                recurrenceInterval = number
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
                                                    selectedCategories = selectedCategories - category
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
                                                    selectedCategories = selectedCategories + category
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
                                            showCreateCategoryDialog = true // Acción del clic
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
                },
                containerColor = themeColors.backGround3,
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.large // Bordes más redondeados

            )
        }

        if(showDatePicker) {
            DatePicker(
                context,
                onDateSelected = { selectedDate ->
                    dueDate = selectedDate
                    showDatePicker = false
                }
            )
        }

        if(showTimePicker) {
            TimePicker(context = context) { selectedTime ->
                dueTime = selectedTime
                showTimePicker = false
            }
        }

        if(showCreateCategoryDialog) {
            AlertDialog(
                onDismissRequest = {
                    showCreateCategoryDialog = false
                    newCategoryName = ""
                    newCategoryColor = ""
                                   },
                confirmButton = {
                    Button(
                        onClick = {
                            showCreateCategoryDialog = false
                            noteViewModel.addCategoryTask(
                                CategoryTaskEntity(
                                    name = newCategoryName,
                                    bgColor = newCategoryColor
                                )
                            )
                            newCategoryName = ""
                            newCategoryColor = ""
                        }
                        ) {
                        Text(text = "Create Category", color = themeColors.text1)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showCreateCategoryDialog = false }
                    ) {
                        Text(text = "Cancel", color = themeColors.text1)
                    }
                },
                title = { Text(
                    text = "Add New Category",
                    color = themeColors.text1
                ) },
                text = {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TextField(
                            value = newCategoryName,
                            onValueChange = { newCategoryName = it},
                            label = { Text("Category Name", color = themeColors.text1) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = themeColors.text1,
                                unfocusedTextColor = themeColors.text2,
                                focusedContainerColor = themeViewModel.getCategoryColor(newCategoryColor),
                                unfocusedContainerColor = themeViewModel.getCategoryColor(newCategoryColor),
                                cursorColor = Color.Magenta
                            )
                        )

                        LazyRow {
                            items(namesColors) { categoryColor ->
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(themeViewModel.getCategoryColor(categoryColor))
                                        .clickable { newCategoryColor = categoryColor }
                                )
                            }
                        }
                    }
                },
                containerColor = themeColors.backGround2
            )
        }
    }
}

// Función para mostrar el selector de fecha
@Composable
fun DatePicker(context: Context, onDateSelected: (Long) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.timeInMillis
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
    ).show()
}


// Función para mostrar el selector de hora
@Composable
fun TimePicker(context: Context, onTimeSelected: (Long) -> Unit) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }.timeInMillis
            onTimeSelected(selectedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
}


// Funciones para formatear la fecha y la hora
fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

fun formatTime(timestamp: Long): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(Date(timestamp))
}