package com.example.mylifeorganizer.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.SettingsEntity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDB = NoteDB.getInstance(application.applicationContext)
    private val notesRepository = NotesRepository(noteDB)

    // Aquí inicializamos NoteViewModel para compartirlo
    val noteViewModel: NoteViewModel = NoteViewModel(notesRepository)

    // Theme state
    val isThemeDark = mutableStateOf(true)

    init {
        viewModelScope.launch {
            val lastSettings = noteViewModel.settings.firstOrNull()

            Log.d("ThemeViewModel", "Last Settings: $lastSettings")


            if (lastSettings != null) {
                isThemeDark.value = lastSettings.isThemeDark
            } else {
                isThemeDark.value = true // Default si no hay configuración guardada
            }
            Log.d("ThemeViewModel", "isThemeDark value init: ${isThemeDark.value}")
            updateThemeColors()
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val latestSettings = noteViewModel.settings.firstOrNull()

            if (latestSettings != null) {
                // Actualizar la configuración existente
                val updatedSettings = latestSettings.copy(
                    isThemeDark = !latestSettings.isThemeDark,
                    updatedAt = System.currentTimeMillis()
                )
                noteViewModel.updateSettings(updatedSettings)
            } else {
                // Crear nueva configuración si no existe
                val newSettings = SettingsEntity(
                    isThemeDark = !isThemeDark.value,
                )
                noteViewModel.insertSettings(newSettings)
            }

            // Actualizar el estado en la UI
            isThemeDark.value = !isThemeDark.value
            Log.d("ThemeViewModel", "isThemeDark value toggle: ${isThemeDark.value}")
            updateThemeColors()
        }
    }

    val namesColorCategories = listOf(
        "red", "green", "blue", "yellow", "purple", "orange", "cyan", "pink", "gray"
    )

    // Obtener los colores de categories usando los nombres de los colores
    fun getCategoryColor(colorName: String): Color {
        return when (colorName) {
            "red"-> themeColors.value.categoriesNotes.red
            "green" -> themeColors.value.categoriesNotes.green
            "blue" -> themeColors.value.categoriesNotes.blue
            "yellow" -> themeColors.value.categoriesNotes.yellow
            "purple" -> themeColors.value.categoriesNotes.purple
            "orange" -> themeColors.value.categoriesNotes.orange
            "cyan" -> themeColors.value.categoriesNotes.cyan
            "pink" -> themeColors.value.categoriesNotes.pink
            "gray" -> themeColors.value.categoriesNotes.gray
            else -> Color.Transparent // Valor por defecto si el nombre del color no coincide
        }
    }

    var themeColors = mutableStateOf(
        ThemeColors(
            backGround1 = Color(0xff000000),
            backGround2 = Color(0xff111111),
            backGround3 = Color(0xff222222),
            backGround4 = Color(0xff333333),
            text1 = Color(0xffffffff),
            text2 = Color(0xffeeeeee),
            text3 = Color(0xffdddddd),
            textComment = Color(0xff999999),
            tabButtonDefault = Color(0xFF13F3BF),
            tabButtonSelected = Color(0xFFF32DEF),
            backGroundRed = Color(0xffaa0c05),
            buttonAdd = Color(0xFF2196F3),
            buttonDelete = Color(0xFFF44336),
            quickCard = ThemeColors.QuickCardColors(
                addNote = Color(0xffffb300),
                addDaily = Color(0xFF04AF74),
                addShoppingList = Color(0xFF9307BA),
                addExpense = Color(0xFFA00606),
                addIncome = Color(0xFF15BF09),
                addTask = Color(0xFF303F9F)
            ),
            backgroundTransparent1 = Color(0xcc000000),
            categoriesNotes = ThemeColors.CategoriesNotes(
                red = Color(0xFF8B0000),
                green = Color(0xFF04A004),
                blue = Color(0xFF00008B),
                yellow =  Color(0xFFBB880F),
                purple = Color(0xFF53089E),
                orange = Color(0xFFF34C17),
                cyan = Color(0xFF0EA380),
                pink = Color(0xFFD51FBD),
                gray = Color(0xFF5C5C5C)
            ),
            verticalLinesNotes = listOf(
                Color(0xFFE4F639),
                Color(0xFF1EF6C4),
                Color(0xFF18F521),
                Color(0xFFF51BD8),
                Color(0xFFF63939),
                Color(0xFF7D30F8),
                Color(0xFFE4F639),
                Color(0xFF1EF6C4),
                Color(0xFF18F521),
                Color(0xFFF51BD8),
                Color(0xFFF63939),
                Color(0xFF7D30F8)
            ),
            bgExpense = Color(0xFFC90926),
            bgIncome = Color(0xFF219E05),
            bgDialog = Color(0x99660066),
            bgCardNote = Color(0x44191970),
            bgCardFolder = Color(0x448B008B)
        )
    )



    private fun updateThemeColors() {
        themeColors.value = if (isThemeDark.value) {
            ThemeColors(
                backGround1 = Color(0xff000000),
                backGround2 = Color(0xff111111),
                backGround3 = Color(0xff222222),
                backGround4 = Color(0xff333333),
                text1 = Color(0xffffffff),
                text2 = Color(0xffeeeeee),
                text3 = Color(0xffdddddd),
                textComment = Color(0xff999999),
                tabButtonDefault = Color(0xFF13F3BF),
                tabButtonSelected = Color(0xFFF32DEF),
                backGroundRed = Color(0xffaa0c05),
                buttonAdd = Color(0xFF2196F3),
                buttonDelete = Color(0xFFF44336),
                quickCard = ThemeColors.QuickCardColors(
                    addNote = Color(0xffffb300),
                    addDaily = Color(0xFF04AF74),
                    addShoppingList = Color(0xFF9307BA),
                    addExpense = Color(0xFFA00606),
                    addIncome = Color(0xFF15BF09),
                    addTask = Color(0xFF303F9F)
                ),
                backgroundTransparent1 = Color(0xcc000000),
                categoriesNotes = ThemeColors.CategoriesNotes(
                    red = Color(0xFF8B0000),
                    green = Color(0xFF008000),
                    blue = Color(0xFF00008B),
                    yellow =  Color(0xFFBB880F),
                    purple = Color(0xFF800080),
                    orange = Color(0xFFFF5722),
                    cyan = Color(0xFF800040),
                    pink = Color(0xFFEC24D1),
                    gray = Color(0xFF5C5C5C)
                ),
                verticalLinesNotes = listOf(
                    Color(0xFFE4F639),
                    Color(0xFF1EF6C4),
                    Color(0xFF18F521),
                    Color(0xFFF51BD8),
                    Color(0xFFF63939),
                    Color(0xFF7D30F8),
                    Color(0xFFE4F639),
                    Color(0xFF1EF6C4),
                    Color(0xFF18F521),
                    Color(0xFFF51BD8),
                    Color(0xFFF63939),
                    Color(0xFF7D30F8)
                ),
                bgExpense = Color(0xFFC90926),
                bgIncome = Color(0xFF219E05),
                bgDialog = Color(0x99660066),
                bgCardNote = Color(0x44191970),
                bgCardFolder = Color(0x448B008B)
            )
        } else {
            ThemeColors(
                backGround1 = Color(0xffffffff),
                backGround2 = Color(0xffeeeeee),
                backGround3 = Color(0xffdddddd),
                backGround4 = Color(0xffcccccc),
                text1 = Color(0xff000000),
                text2 = Color(0xff111111),
                text3 = Color(0xff222222),
                textComment = Color(0xFF61AD9D),
                tabButtonDefault = Color(0xFF04523B),
                tabButtonSelected = Color(0xFF97108D),
                backGroundRed = Color(0xFFF31C4E),
                buttonAdd = Color(0xFF24EFA8),
                buttonDelete = Color(0xFFE91E63),
                quickCard = ThemeColors.QuickCardColors(
                    addNote = Color(0xFFFFFF00),
                    addDaily = Color(0xFF2CEEB1),
                    addShoppingList = Color(0xFFC162F5),
                    addExpense = Color(0xFFF33450),
                    addIncome = Color(0xFF5CF63A),
                    addTask = Color(0xFF00CCFF)
                ),
                backgroundTransparent1 = Color(0xccffffff),
                categoriesNotes = ThemeColors.CategoriesNotes(
                    red = Color(0xFFFA8072),
                    green = Color(0xFF00FF00),
                    blue = Color(0xFF00BFFF),
                    yellow =  Color(0xFFFFFF00),
                    purple = Color(0xFFCB88EF),
                    orange = Color(0xFFFFA500),
                    cyan = Color(0xFF00FFFF),
                    pink = Color(0xFFEE74D4),
                    gray = Color(0xFFA0A0A0),
                ),
                verticalLinesNotes = listOf(
                    Color(0xff000000),
                    Color(0xFF05B683),
                    Color(0xFF29A12E),
                    Color(0xFFB8159D),
                    Color(0xFFE91E63),
                    Color(0xFF7D30F8),
                    Color(0xFFFFFF00),
                    Color(0xFF05B683),
                    Color(0xFF29A12E),
                    Color(0xFFB8159D),
                    Color(0xFFE91E63),
                    Color(0xFF7D30F8)
                ),
                bgExpense = Color(0xFFF33450),
                bgIncome = Color(0xFF6BFC4B),
                bgDialog = Color(0xaaff00ff),
                bgCardNote = Color(0x447FFFD4),
                bgCardFolder = Color(0x44FF00FF)
            )
        }
    }
}


data class ThemeColors(
    val backGround1: Color,
    val backGround2: Color,
    val backGround3: Color,
    val backGround4: Color,
    val text1: Color,
    val text2: Color,
    val text3: Color,
    val textComment: Color,
    val tabButtonDefault: Color,
    val tabButtonSelected: Color,
    val backGroundRed: Color,
    val buttonAdd: Color,
    val buttonDelete: Color,
    val quickCard: QuickCardColors,
    val backgroundTransparent1: Color,
    val categoriesNotes: CategoriesNotes,
    val verticalLinesNotes: List<Color>,
    val bgExpense: Color,
    val bgIncome: Color,
    val bgDialog: Color,
    val bgCardNote: Color,
    val bgCardFolder: Color,
) {
    data class QuickCardColors(
        val addNote: Color,
        val addDaily: Color,
        val addShoppingList: Color,
        val addExpense: Color,
        val addIncome: Color,
        val addTask: Color,
    )

    data class CategoriesNotes(
        val red: Color,
        val green: Color,
        val blue: Color,
        val yellow: Color,
        val purple: Color,
        val orange: Color,
        val cyan: Color,
        val pink: Color,
        val gray: Color
    )
}