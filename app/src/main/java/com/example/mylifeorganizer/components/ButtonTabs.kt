package com.example.mylifeorganizer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun ButtonTabs() {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val selectedTab = appViewModel.selectedTab.value
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    val heightBar = 60.dp

    // Lista de pestañas con identificador, texto mostrado y recurso de ícono
    val tabs = listOf(
        TabItem("Home", if (isLangEng) "Home" else "Inicio", Icons.Default.Home),
        TabItem("Notes", if (isLangEng) "Notes" else "Notas", R.drawable.baseline_edit_note_24),
        TabItem("Daily", if (isLangEng) "Daily" else "Diario", R.drawable.baseline_today_24),
        TabItem("Tasks", if (isLangEng) "Tasks" else "Tareas", R.drawable.baseline_checklist_24),
        TabItem("Finance", if (isLangEng) "Finance" else "Finanzas", R.drawable.baseline_wallet_24),
        TabItem("Calendar", if (isLangEng) "Calendar" else "Calendario", R.drawable.baseline_calendar_month_24),
        TabItem("Dashboard", "Dashboard", R.drawable.baseline_dashboard_24),
        TabItem("Menu", if (isLangEng) "Menu" else "Menú", R.drawable.baseline_menu_24)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightBar)
            .background(themeColors.backGround1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            TabButton(
                name = tab.displayName,
                icon = {
                    if (tab.iconResource is Int) {
                        Icon(
                            painter = painterResource(id = tab.iconResource),
                            contentDescription = tab.identifier,
                            modifier = Modifier.size(30.dp),
                            tint =  if (selectedTab == tab.identifier) themeColors.tabButtonSelected else themeColors.tabButtonDefault,
                        )
                    } else if (tab.iconResource is ImageVector) {
                        Icon(
                            imageVector = tab.iconResource,
                            contentDescription = tab.identifier,
                            modifier = Modifier.size(30.dp),
                            tint =  if (selectedTab == tab.identifier) themeColors.tabButtonSelected else themeColors.tabButtonDefault,
                        )
                    }
                },
                onClick = { appViewModel.changeTab(tab.identifier) },
                isSelected = selectedTab == tab.identifier,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Clase de datos para representar una pestaña
data class TabItem(
    val identifier: String, // Identificador único, siempre en inglés
    val displayName: String, // Texto mostrado en pantalla
    val iconResource: Any // Recurso de ícono (ImageVector o Int)
)

@Composable
fun TabButton(
    name: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            icon()
            Text(
                text = name,
                fontSize = 10.sp,
                color = if (isSelected) themeColors.tabButtonSelected else themeColors.tabButtonDefault
            )
        }
    }
}
