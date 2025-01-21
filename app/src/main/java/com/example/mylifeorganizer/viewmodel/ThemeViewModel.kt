package com.example.mylifeorganizer.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel


class ThemeViewModel : ViewModel() {
    // Theme state
    val isThemeDark = mutableStateOf(true)

    fun toggleTheme() {
        isThemeDark.value = !isThemeDark.value
        updateThemeColors()
    }

    var themeColors = mutableStateOf(
        ThemeColors(
            backGround1 = Color(0xff000000),
            backGround2 = Color(0xff111111),
            backGround3 = Color(0xff222222),
            text1 = Color(0xffffffff),
            text2 = Color(0xffeeeeee),
            text3 = Color(0xffdddddd),
            tabButtonDefault = Color(0xff061380),
            tabButtonSelected = Color(0xffa00796),
            backGroundRed = Color(0xffaa0c05),
            buttonAdd = Color(0xFF2196F3),
            buttonDelete = Color(0xFFF44336),
            quickCard = ThemeColors.QuickCardColors(
                addNote = Color(0xffdbb807),
                addDaily = Color(0xFF04AF74),
                addShoppingList = Color(0xFF9307BA),
                addExpense = Color(0xFFA00606),
                addIncome = Color(0xFF15BF09),
                addTask = Color(0xFF303F9F)
            ),
            backgroundTransparent1 = Color(0xcc000000)
        )
    )

    private fun updateThemeColors() {
        themeColors.value = if (isThemeDark.value) {
            ThemeColors(
                backGround1 = Color(0xff000000),
                backGround2 = Color(0xff111111),
                backGround3 = Color(0xff222222),
                text1 = Color(0xffffffff),
                text2 = Color(0xffeeeeee),
                text3 = Color(0xffdddddd),
                tabButtonDefault = Color(0xff061380),
                tabButtonSelected = Color(0xffa00796),
                backGroundRed = Color(0xffaa0c05),
                buttonAdd = Color(0xFF2196F3),
                buttonDelete = Color(0xFFF44336),
                quickCard = ThemeColors.QuickCardColors(
                    addNote = Color(0xffdbb807),
                    addDaily = Color(0xFF04AF74),
                    addShoppingList = Color(0xFF9307BA),
                    addExpense = Color(0xFFA00606),
                    addIncome = Color(0xFF15BF09),
                    addTask = Color(0xFF303F9F)
                ),
                backgroundTransparent1 = Color(0xcc000000)
            )
        } else {
            ThemeColors(
                backGround1 = Color(0xffffffff),
                backGround2 = Color(0xffeeeeee),
                backGround3 = Color(0xffdddddd),
                text1 = Color(0xff000000),
                text2 = Color(0xff111111),
                text3 = Color(0xff222222),
                tabButtonDefault = Color(0xFF0EAC7E),
                tabButtonSelected = Color(0xFFEA25DC),
                backGroundRed = Color(0xFFF31C4E),
                buttonAdd = Color(0xFF24EFA8),
                buttonDelete = Color(0xFFE91E63),
                quickCard = ThemeColors.QuickCardColors(
                    addNote = Color(0xFFF1ED1D),
                    addDaily = Color(0xFF2CEEB1),
                    addShoppingList = Color(0xFFC162F5),
                    addExpense = Color(0xFFF33450),
                    addIncome = Color(0xFF5CF63A),
                    addTask = Color(0xFF2196F3)
                ),
                backgroundTransparent1 = Color(0xccffffff)
            )
        }
    }
}

data class ThemeColors(
    val backGround1: Color,
    val backGround2: Color,
    val backGround3: Color,
    val text1: Color,
    val text2: Color,
    val text3: Color,
    val tabButtonDefault: Color,
    val tabButtonSelected: Color,
    val backGroundRed: Color,
    val buttonAdd: Color,
    val buttonDelete: Color,
    val quickCard: QuickCardColors,
    val backgroundTransparent1: Color,
) {
    data class QuickCardColors(
        val addNote: Color,
        val addDaily: Color,
        val addShoppingList: Color,
        val addExpense: Color,
        val addIncome: Color,
        val addTask: Color,
    )
}