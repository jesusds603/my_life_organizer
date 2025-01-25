package com.example.mylifeorganizer.components.home

import androidx.compose.ui.graphics.Color
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeColors


data class QuickCardItem(
    val title: String,
    val subtitle: String,
    val iconResId: Int, // ID del ícono
    val onClick: () -> Unit,
    val backgroundColor: Color
)


// Definir la lista de tarjetas fuera de la función HomeScreen
fun getQuickCardItems(
    themeColors: ThemeColors,
    appViewModel: AppViewModel
): List<QuickCardItem> {
    return listOf(
        QuickCardItem(
            title = "Add Note",
            subtitle = "Add a new note",
            iconResId = R.drawable.baseline_colorize_24,
            onClick = {
                appViewModel.changeIdFolderForAddingNote(0)
                appViewModel.toggleAddingNote()
                      },
            backgroundColor = themeColors.quickCard.addNote
        ),
        QuickCardItem(
            title = "Add Daily",
            subtitle = "Write what you did today",
            iconResId = R.drawable.baseline_library_add_24,
            onClick = {  },
            backgroundColor = themeColors.quickCard.addDaily
        ),
        QuickCardItem(
            title = "New Shopping",
            subtitle = "Make a simple and quick shopping list",
            iconResId = R.drawable.baseline_fact_check_24,
            onClick = {  },
            backgroundColor = themeColors.quickCard.addShoppingList
        ),
        QuickCardItem(
            title = "Add Expense",
            subtitle = "Write your expenses from today",
            iconResId = R.drawable.baseline_money_off_24,
            onClick = {  },
            backgroundColor = themeColors.quickCard.addExpense
        ),
        QuickCardItem(
            title = "Add Income",
            subtitle = "Write your incomes from today",
            iconResId = R.drawable.baseline_attach_money_24,
            onClick = {  },
            backgroundColor = themeColors.quickCard.addIncome
        ),
        QuickCardItem(
            title = "New Task",
            subtitle = "Write a task easily",
            iconResId = R.drawable.baseline_add_task_24,
            onClick = {  },
            backgroundColor = themeColors.quickCard.addTask
        )
    )
}