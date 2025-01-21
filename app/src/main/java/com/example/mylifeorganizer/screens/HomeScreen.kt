package com.example.mylifeorganizer.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.components.DeleteDB
import com.example.mylifeorganizer.components.home.Header
import com.example.mylifeorganizer.components.home.QuickCard
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.example.mylifeorganizer.windows.AddNoteWindow

data class QuickCardItem(
    val title: String,
    val subtitle: String,
    val iconResId: Int, // ID del ícono
    val onClick: () -> Unit,
    val backgroundColor: Color
)


@Composable
fun HomeScreen() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    var themeColors = themeViewModel.themeColors.value
    var isThemeDark = themeViewModel.isThemeDark.value
    var isLangEng = appViewModel.isLangEng.value
    var isAddingNote = appViewModel.isAddingNote.value


    val quickCardItems = listOf(
        QuickCardItem(
            title = "Add Note",
            subtitle = "Add a new note",
            iconResId = R.drawable.baseline_colorize_24,
            onClick = { appViewModel.toggleAddingNote() },
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Header()


            // Cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(quickCardItems) { item ->
                    QuickCard(
                        title = item.title,
                        subtitle = item.subtitle,
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconResId),
                                contentDescription = item.title,
                                modifier = Modifier
                                    .height(50.dp) // Ajusta la altura
                                    .aspectRatio(1f)
                            )
                        },
                        onClick = item.onClick,
                        backgroundColor = item.backgroundColor
                    )
                }
            }

        }

        if (isAddingNote) {
            AddNoteWindow(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }
}