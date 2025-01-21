package com.example.mylifeorganizer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

private val ICON_SIZE = 30.dp

@Composable
fun ButtonTabs() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val selectedTab = appViewModel.selectedTab.value
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(themeColors.backGround1),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TabButton for Home
        TabButton(
            name = if(isLangEng) "Home" else "Inicio",
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(ICON_SIZE)
                )
            },
            isSelected = selectedTab == "Home",
            onClick = { appViewModel.changeTab("Home") },
            modifier = Modifier.weight(1f) // Distribuye uniformemente el ancho
        )
        // TabButton for Notes
        TabButton(
            name = if(isLangEng) "Notes" else "Notas",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_note_24),
                    contentDescription = "Notes",
                    modifier = Modifier.size(ICON_SIZE)
                )
            },
            isSelected = selectedTab == "Notes",
            onClick = { appViewModel.changeTab("Notes") },
            modifier = Modifier.weight(1f) // Distribuye uniformemente el ancho
        )
        // TabButton for Tasks
        TabButton(
            name = if(isLangEng) "Tasks" else "tareas",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_checklist_24),
                    contentDescription = "Tasks",
                    modifier = Modifier.size(ICON_SIZE)
                )
            },
            isSelected = selectedTab == "Tasks",
            onClick = { appViewModel.changeTab("Tasks") },
            modifier = Modifier.weight(1f) // Distribuye uniformemente el ancho
        )
        // TabButton for Habits
        TabButton(
            name = if(isLangEng) "Habits" else "Hábitos",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_checklist_24),
                    contentDescription = "Habits",
                    modifier = Modifier.size(ICON_SIZE)
                )
            },
            isSelected = selectedTab == "Habits",
            onClick = { appViewModel.changeTab("Habits") },
            modifier = Modifier.weight(1f) // Distribuye uniformemente el ancho
        )
        // TabButton for Calendar
        TabButton(
            name = if(isLangEng) "Calendar" else "Calendario",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = "Calendar",
                    modifier = Modifier.size(ICON_SIZE)
                )
            },
            isSelected = selectedTab == "Calendar",
            onClick = { appViewModel.changeTab("Calendar") },
            modifier = Modifier.weight(1f) // Distribuye uniformemente el ancho
        )
        // TabButton for Dashboard
        TabButton(
            name = "Dashboard",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_dashboard_24),
                    contentDescription = "Dashboard",
                    modifier = Modifier.size(ICON_SIZE)
                )
            },
            isSelected = selectedTab == "Dashboard",
            onClick = { appViewModel.changeTab("Dashboard") },
            modifier = Modifier.weight(1f) // Distribuye uniformemente el ancho
        )

    }
}

@Composable
fun TabButton(
    name: String,
    icon: @Composable () -> Unit, // Corrected type
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themeViewModel: ThemeViewModel = viewModel()

    val themeColors = themeViewModel.themeColors.value

    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxHeight()
            .padding(0.dp)
            .background(Color.Transparent),
        colors = ButtonDefaults.textButtonColors(
            contentColor = themeColors.text1,
            containerColor = if (isSelected) themeColors.tabButtonSelected else themeColors.tabButtonDefault
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            icon()

            Text(
                text = name,
                fontSize = 10.sp
            )
        }
    }
}
