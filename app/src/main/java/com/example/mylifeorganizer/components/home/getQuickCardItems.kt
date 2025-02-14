package com.example.mylifeorganizer.components.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeColors


data class QuickCardItem(
    val title: String,
    val iconResId: Int, // ID del ícono
    val onClick: () -> Unit,
    val backgroundColor: Color
)


// Definir la lista de tarjetas fuera de la función HomeScreen
@RequiresApi(Build.VERSION_CODES.O)
fun getQuickCardItems(
    themeColors: ThemeColors,
    appViewModel: AppViewModel,
    isLangEng: Boolean
): List<QuickCardItem> {
    return listOf(
        QuickCardItem(
            title = if (isLangEng) "New Note" else "Nueva Nota",
            iconResId = R.drawable.baseline_colorize_24,
            onClick = {
                appViewModel.changeIdFolderForAddingNote(0)
                appViewModel.toggleAddingNote()
                      },
            backgroundColor = themeColors.quickCard.addNote
        ),
        QuickCardItem(
            title = if(isLangEng) "+ Income / Expense" else "+ Ingresos / Gastos",
            iconResId = R.drawable.baseline_attach_money_24,
            onClick = {
                appViewModel.toggleAddingFinance()
                appViewModel.changeTab("Finance")
            },
            backgroundColor = themeColors.quickCard.addIncome
        ),
        QuickCardItem(
            title = if(isLangEng) "New Task" else "Nueva Tarea",
            iconResId = R.drawable.baseline_add_task_24,
            onClick = {
                appViewModel.changeTitleNewTask("")
                appViewModel.changeDescriptionNewTask("")
                appViewModel.updateSelectedDueDate("")
                appViewModel.updateSelectedDueTime("")
                appViewModel.updateSelectedCategoriesTask(emptyList())
                appViewModel.updatePriorityNewTask( 1)
                appViewModel.toggleIsTaskRecurring()
                appViewModel.updateRecurrenceTaskPattern("")
                appViewModel.updateNumDaysNewTask(0)
                appViewModel.updateSelectedWeekDaysNewTask( emptyList() )
                appViewModel.updateNumWeeksNewTask( 0)
                appViewModel.updateSelectedMonthDaysNewTask( emptyList() )
                appViewModel.updateNumMonthsNewTask(0)
                appViewModel.updateSelectedYearDaysNewTask( emptySet() )
                appViewModel.updateNumYearsNewTask(0)
                appViewModel.updateSelectedCustomIntervalNewTask(1)
                appViewModel.updateNumTimesNewTask(0)

                appViewModel.toggleShowDialogCreateTask()

                appViewModel.changeTab("Tasks")
            },
            backgroundColor = themeColors.quickCard.addTask
        )
    )
}