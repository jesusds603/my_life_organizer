package com.example.mylifeorganizer.components.tasks.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent() {
    val appViewModel: AppViewModel = viewModel()
    val noteViewModel: NoteViewModel = appViewModel.noteViewModel

    val occurrenceTasks = noteViewModel.getAllOccurrences().collectAsState(initial = emptyList()).value
    val tasksWithCategories = noteViewModel.tasksWithCategories.collectAsState(initial = emptyList()).value

    val nameSelectedCategoryTasksScreen = appViewModel.nameSelectedCategorieTasksScreen

    // Create a map of taskId to TaskWithCategories for quick lookup
    val taskMap = tasksWithCategories.associateBy { it.task.taskId }

    // Associate each occurrence with its task
    val occurrencesWithTasks = occurrenceTasks.map { occurrence ->
        occurrence to taskMap[occurrence.taskId]
    }


    // Filtrar categorías por nombre
    val filteredOccurrences = if(nameSelectedCategoryTasksScreen == "All") {
        occurrencesWithTasks
    } else {
        occurrencesWithTasks.filter { (_, task) ->
            task?.categories?.any { category ->
                category.name == nameSelectedCategoryTasksScreen
            } ?: false
        }
    }

    // Obtener la fecha actual
    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) // Formato: "yyyy/MM/dd"

//    println("currentDate: $currentDate")

    val sortedOcurrences = filteredOccurrences.sortedBy { it.first.dueDate }


    // Agrupar tareas por día
    val ocurrencesGroupedByDay = sortedOcurrences.groupBy { it.first.dueDate }

    // Separar tareas no hechas (fecha anterior a la actual y no completadas)
    val notDoneOcurrences = ocurrencesGroupedByDay.mapValues { (date, ocurrences) ->
        ocurrences.filter { ocurrence ->
            !ocurrence.first.isCompleted && ocurrence.first.dueDate < currentDate
        }
    }.filterValues { it.isNotEmpty() }

    // Separar tareas pendientes (fecha mayor o igual a la actual y no completadas)
    val pendingOcurrences = ocurrencesGroupedByDay.mapValues { (date, ocurrences) ->
        ocurrences.filter { ocurrence ->
            !ocurrence.first.isCompleted && ocurrence.first.dueDate >= currentDate
        }
    }.filterValues { it.isNotEmpty() }

    val completedOcurrences = ocurrencesGroupedByDay.mapValues { (_, ocurrences) ->
        ocurrences.filter { it.first.isCompleted }
    }.filterValues { it.isNotEmpty() }

    var selectedDueOrCompleted by remember { mutableStateOf("due") } // "due" or "completed" or "notDone"

    var showDialogDeleteTask by remember { mutableStateOf(false) }



    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OptionsTask(
            tasksWithCategories = tasksWithCategories,
            changeShowDialogDeleteTask = { showDialogDeleteTask = it }
        )

        if(showDialogDeleteTask) {
            DialogDeleteTask(
                changeShowDialogDeleteTask = { showDialogDeleteTask = it }
            )
        }


        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {


            RowMenu(
                selectedDueOrCompleted = selectedDueOrCompleted,
                changeSelectedDueOrCompleted = { selectedDueOrCompleted = it }
            )

            ColumnTasks(
                selectedDueOrCompleted = selectedDueOrCompleted,
                pendingOcurrences = pendingOcurrences,
                completedOcurrences = completedOcurrences,
                notDoneOcurrences = notDoneOcurrences
            )
        }
    }
}
