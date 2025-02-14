package com.example.mylifeorganizer.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.CategoryFinanceEntity
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.PaymentMethodEntity
import com.example.mylifeorganizer.room.SettingsEntity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class AppViewModel(application: Application) : AndroidViewModel(application)  {

    private val noteDB = NoteDB.getInstance(application.applicationContext)
    private val notesRepository = NotesRepository(noteDB)

    // Aquí inicializamos NoteViewModel para compartirlo
    val noteViewModel: NoteViewModel = NoteViewModel(notesRepository)

    // Estado del idioma con valor por defecto
    val isLangEng = mutableStateOf(true)

    // Ordenar notas en la screen
    // Valores: createdAscending, createdDescending, updatedAscending, updatedDescending, nameAscending, nameDescending
    val selectedOrderingNotes = mutableStateOf("updatedDescending") // la mas reciente arriba


    init {
        viewModelScope.launch {
            val lastSettings = noteViewModel.settings.firstOrNull()


            if (lastSettings != null) {
                isLangEng.value = lastSettings.isLangEng
                selectedOrderingNotes.value = lastSettings.orderNotes
            } else {
                isLangEng.value = true // Default si no hay configuración guardada
                selectedOrderingNotes.value = "updatedDescending"
            }
        }
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val latestSettings = noteViewModel.settings.firstOrNull()

            if (latestSettings != null) {
                // Actualizar la configuración existente
                val updatedSettings = latestSettings.copy(
                    isLangEng = !latestSettings.isLangEng,
                    updatedAt = System.currentTimeMillis()
                )
                noteViewModel.updateSettings(updatedSettings)
            } else {
                // Crear nueva configuración si no existe
                val newSettings = SettingsEntity(
                    isLangEng = !isLangEng.value,
                )
                noteViewModel.insertSettings(newSettings)
            }

            // Actualizar el estado en la UI
            isLangEng.value = !isLangEng.value
        }
    }

    fun changeSelectedOrderingNotes(ordering: String) {
        viewModelScope.launch {
            val latestSettings = noteViewModel.settings.firstOrNull()
            if (latestSettings != null) {
                val updatedSettings = latestSettings.copy(
                    orderNotes = ordering,
                    updatedAt = System.currentTimeMillis()
                )
                noteViewModel.updateSettings(updatedSettings)
            } else {
                // Crear nueva configuración si no existe
                val newSettings = SettingsEntity(
                    orderNotes = ordering,
                )
                noteViewModel.insertSettings(newSettings)
            }
            selectedOrderingNotes.value = ordering
        }
    }

    // Tab state
    var selectedTab = mutableStateOf("Home")
    fun changeTab(newTab: String) {
        selectedTab.value = newTab
    }

    // Estado de activación de las tarjetas de inicio

    val isAddingNote = mutableStateOf(false)
    fun toggleAddingNote() {
        isAddingNote.value = !isAddingNote.value
    }
    val idFolderForAddingNote = mutableStateOf<Long>(0)
    fun changeIdFolderForAddingNote(folderId: Long) {
        idFolderForAddingNote.value = folderId
    }
    val idFolderForAddingSubFolder = mutableStateOf<Long>(0)
    fun changeIdFolderForAddingSubFolder(folderId: Long) {
        idFolderForAddingSubFolder.value = folderId
    }

    val isAddingDaily = mutableStateOf(false)
    fun toggleAddingDaily() {
        isAddingDaily.value = !isAddingDaily.value
    }

    // Estado para visualizar una nota
    val isShowingNote = mutableStateOf(false)
    fun toggleShowingNote() {
        isShowingNote.value = !isShowingNote.value
    }

    // -------------------------------------------------
    // NOTAS
    val selectedNoteId = mutableStateOf<Long?>(null)
    fun changeSelectedNoteId(noteId: Long?) {
        selectedNoteId.value = noteId
    }

    val selectedCategory = mutableStateOf<CategoryEntity?>(null)
    fun changeSelectedCategory(category: CategoryEntity?) {
        selectedCategory.value = category
    }


    // recordar los folders abiertos en la screen de notas para que al crear una nueva nota o editarla se mantenga la vista
    var expandedFolders = mutableStateOf<Set<Long>>(emptySet())
    fun changeExpandedFolders(folders: Set<Long>) {
        expandedFolders.value = folders
    }

    var isVisualizingNote = mutableStateOf(false)
    fun toggleIsVisualizingNote() {
        isVisualizingNote.value = !isVisualizingNote.value
    }

    // ---------------------------------------
    // ---------------------------------------

    // Para la creacción de una nueva tarea

    val showDialogCreateTask = mutableStateOf(false)
    fun toggleShowDialogCreateTask() {
        showDialogCreateTask.value = !showDialogCreateTask.value
    }

    val isEditingTask = mutableStateOf(false)
    fun toggleIsEditingTask() {
        isEditingTask.value = !isEditingTask.value
    }

    var showCreateCategoryDialogTask = mutableStateOf(false)
    fun toggleShowCreateCategoryDialogTask() {
        showCreateCategoryDialogTask.value = !showCreateCategoryDialogTask.value
    }

    var showDatePicker = mutableStateOf(false)
    fun toggleShowDatePicker() {
        showDatePicker.value = !showDatePicker.value
    }

    var showTimePicker = mutableStateOf(false)
    fun toggleShowTimePicker() {
        showTimePicker.value = !showTimePicker.value
    }

    val titleNewTask = mutableStateOf("")
    fun changeTitleNewTask(title: String) {
        titleNewTask.value = title
    }

    val descriptionNewTask = mutableStateOf("")
    fun changeDescriptionNewTask(description: String) {
        descriptionNewTask.value = description
    }

    var selectedDueDate by mutableStateOf("") // "yyyy/MM/dd
    fun updateSelectedDueDate(newDate: String) {
        selectedDueDate = newDate
    }

    var selectedDueTime by mutableStateOf("") // "HH:mm" en formato de 24 horas
    fun updateSelectedDueTime(time: String) {
        selectedDueTime = time
    }

    var selectedCategoriesTask by mutableStateOf<List<CategoryTaskEntity>>(emptyList())
    fun updateSelectedCategoriesTask(categories: List<CategoryTaskEntity>) {
        selectedCategoriesTask = categories
    }

    var priorityNewTask by mutableStateOf(1)
    fun updatePriorityNewTask(priority: Int) {
        priorityNewTask = priority
    }

    var nameSelectedCategorieTasksScreen by mutableStateOf("All")
    fun updateNameSelectedCategorieTasksScreen(name: String) {
        nameSelectedCategorieTasksScreen = name
    }

    // Recurrence
    var isTaskRecurring by mutableStateOf(false)
    fun toggleIsTaskRecurring() {
        isTaskRecurring = !isTaskRecurring
    }

    var recurrenceTaskPattern by mutableStateOf("")
    fun updateRecurrenceTaskPattern(pattern: String) {
        recurrenceTaskPattern = pattern
    }

    var numDaysNewTask by mutableStateOf(0)
    fun updateNumDaysNewTask(days: Int) {
        numDaysNewTask = days
    }

    var selectedWeekDaysNewTask by mutableStateOf<List<Int>>(emptyList())
    fun updateSelectedWeekDaysNewTask(days: List<Int>) {
        selectedWeekDaysNewTask = days
    }

    var numWeeksNewTask by mutableStateOf(0)
    fun updateNumWeeksNewTask(weeks: Int) {
        numWeeksNewTask = weeks
    }

    var selectedMonthDaysNewTask by mutableStateOf<List<Int>>(emptyList())
    fun updateSelectedMonthDaysNewTask(days: List<Int>) {
        selectedMonthDaysNewTask = days
    }

    var numMonthsNewTask by mutableStateOf(0)
    fun updateNumMonthsNewTask(months: Int) {
        numMonthsNewTask = months
    }

    var selectedYearDaysNewTask by mutableStateOf<Set<String>>(emptySet())
    fun updateSelectedYearDaysNewTask(days: Set<String>) {
        selectedYearDaysNewTask = days
    }

    var numYearsNewTask by mutableStateOf(0)
    fun updateNumYearsNewTask(years: Int) {
        numYearsNewTask = years
    }

    var selectedCustomIntervalNewTask by mutableStateOf(1)
    fun updateSelectedCustomIntervalNewTask(interval: Int) {
        selectedCustomIntervalNewTask = interval
    }

    var numTimesNewTask by mutableStateOf(0)
    fun updateNumTimesNewTask(times: Int) {
        numTimesNewTask = times
    }

    // para editar tareas
    var taskIdSelectedScreen by mutableStateOf<Long?>(null)
    fun updateTaskIdSelectedScreen(taskId: Long?) {
        taskIdSelectedScreen = taskId
    }


    // ------------------------------------------
    //                   FINANCES

    val isAddingFinance = mutableStateOf(false)
    fun toggleAddingFinance() {
        isAddingFinance.value = !isAddingFinance.value
    }

    val isEditingFinance = mutableStateOf(false)
    fun toggleEditingFinance() {
        isEditingFinance.value = !isEditingFinance.value
    }

    val financeIdForEditing = mutableStateOf<Long?>(null)
    fun updateFinanceIdForEditing(financeId: Long?) {
        financeIdForEditing.value = financeId
    }

    val isAddingDateForFinance = mutableStateOf(false)
    fun toggleAddingDateForFinance() {
        isAddingDateForFinance.value = !isAddingDateForFinance.value
    }

    val isAddingCategoryForFinance = mutableStateOf(false)
    fun toggleAddingCategoryForFinance() {
        isAddingCategoryForFinance.value = !isAddingCategoryForFinance.value
    }

    val isAddingPaymentMethodForFinance = mutableStateOf(false)
    fun toggleAddingPaymentMethodForFinance() {
        isAddingPaymentMethodForFinance.value = !isAddingPaymentMethodForFinance.value
    }

    val isCreatingCategoryForFinance = mutableStateOf(false)
    fun toggleCreatingCategoryForFinance() {
        isCreatingCategoryForFinance.value = !isCreatingCategoryForFinance.value
    }

    var titleForNewFinance by mutableStateOf("")
    fun updateTitleForNewFinance(title: String) {
        titleForNewFinance = title
    }
    var descriptionForNewFinance by mutableStateOf("")
    fun updateDescriptionForNewFinance(description: String) {
        descriptionForNewFinance = description
    }
    var amountForNewFinance by mutableStateOf(0L)
    fun updateAmountForNewFinance(amount: Long) {
        amountForNewFinance = amount
    }
    var dateForNewFinance by mutableStateOf("Today")
    fun updateDateForNewFinance(date: String) {
        dateForNewFinance = date
    }
    var isExpenseForNewFinance by mutableStateOf(true)
    fun toggleIsExpenseForNewFinance() {
        isExpenseForNewFinance = !isExpenseForNewFinance
    }
    var selectedCategoriesForNewFinance by mutableStateOf<List<CategoryFinanceEntity>>(emptyList())
    fun updateSelectedCategoriesForNewFinance(categories: List<CategoryFinanceEntity>) {
        selectedCategoriesForNewFinance = categories
    }
    var paymentMethodForNewFinance by mutableStateOf<PaymentMethodEntity?>(null)
    fun updatePaymentMethodForNewFinance(paymentMethod: PaymentMethodEntity) {
        paymentMethodForNewFinance = paymentMethod
    }

    var selectedCategoryForFinanceScreen by mutableStateOf("All")
    fun updateSelectedCategoryForFinanceScreen(category: String) {
        selectedCategoryForFinanceScreen = category
    }

    var selectedPaymentMethodForFinanceScreen by mutableStateOf("All")
    fun updateSelectedPaymentMethodForFinanceScreen(paymentMethod: String) {
        selectedPaymentMethodForFinanceScreen = paymentMethod
    }

    // Función para obtener el mes actual en formato "yyyy/MM"
    private fun getCurrentMonth(): String {
        val currentDate = LocalDate.now()
        val monthFormatter = DateTimeFormatter.ofPattern("yyyy/MM")
        return currentDate.format(monthFormatter)
    }

    var selectedMonthScreen by mutableStateOf(getCurrentMonth())
    fun updateSelectedMonthScreen(month: String) {
        selectedMonthScreen = month
    }

    // ------------------------------------------------------
    //                   HABITS
    // ------------------------------------------------------

    val isAddingHabit = mutableStateOf(false)
    fun toggleAddingHabit() {
        isAddingHabit.value = !isAddingHabit.value
    }

    val titleNewHabit = mutableStateOf("")
    fun changeTitleNewHabit(title: String) {
        titleNewHabit.value = title
    }

    val colorNewHabit = mutableStateOf("pink")
    fun changeColorNewHabit(color: String) {
        colorNewHabit.value = color
    }

    val timeForNewHabit = mutableStateOf("any") // "any", "morning", "afternoon", "night", "custom"
    fun changeTimeForNewHabit(time: String) {
        timeForNewHabit.value = time
    }

    val hourForNewHabit = mutableStateOf("08")
    fun changeHourForNewHabit(hour: String) {
        hourForNewHabit.value = hour
    }

    val minuteForNewHabit = mutableStateOf("00")
    fun changeMinuteForNewHabit(minute: String) {
        minuteForNewHabit.value = minute
    }

    val recurrencePatternForNewHabit = mutableStateOf("daily") // "daily", "monthly", "yearly"
    fun changeRecurrencePatternForNewHabit(recurrence: String) {
        recurrencePatternForNewHabit.value = recurrence
    }

    val isWeeklyAnytimeHabit = mutableStateOf(true)
    fun changeIsWeeklyAnytimeHabit(isWeeklyAnytime: Boolean) {
        isWeeklyAnytimeHabit.value = isWeeklyAnytime
    }

    val numDaysForWeeklyHabit = mutableStateOf(1)
    fun changeNumDaysWeeklyHabit(num: Int) {
        numDaysForWeeklyHabit.value = num
    }

    val recurrenceWeekDaysHabit = mutableStateOf("")
    fun changeRecurrenceWeekDaysHabit(days: String) {
        recurrenceWeekDaysHabit.value = days
    }

    val isMonthlyAnytimeHabit = mutableStateOf(true)
    fun changeIsMonthlyAnytimeHabit(isMonthlyAnytime: Boolean) {
        isMonthlyAnytimeHabit.value = isMonthlyAnytime
    }

    val numDaysForMonthlyHabit = mutableStateOf(1)
    fun changeNumDaysMonthlyHabit(num: Int) {
        numDaysForMonthlyHabit.value = num
    }

    val recurrenceMonthDaysHabit = mutableStateOf("")
    fun changeRecurrenceMonthDaysHabit(days: String) {
        recurrenceMonthDaysHabit.value = days
    }

    val isYearlyAnytimeHabit = mutableStateOf(true)
    fun changeIsYearlyAnytimeHabit(isYearlyAnytime: Boolean) {
        isYearlyAnytimeHabit.value = isYearlyAnytime
    }

    val numDaysForYearlyHabit = mutableStateOf(1)
    fun changeNumDaysYearlyHabit(num: Int) {
        numDaysForYearlyHabit.value = num
    }

    val recurrenceYearDaysHabit = mutableStateOf("")
    fun changeRecurrenceYearDaysHabit(days: String) {
        recurrenceYearDaysHabit.value = days
    }

    val durationForNewHabit = mutableIntStateOf(0)
    fun changeDurationForNewHabit(duration: Int) {
        durationForNewHabit.intValue = duration
    }

    // ------------------------------------------------------
    //                   CALENDAR
    // ------------------------------------------------------

    val selectedDateCalendar = mutableStateOf(LocalDate.now())
    fun changeSelectedDateCalendar(date: LocalDate) {
        selectedDateCalendar.value = date
    }

    val isShowingDayCalendar = mutableStateOf(false)
    fun toggleShowingDayCalendar() {
        isShowingDayCalendar.value = !isShowingDayCalendar.value
    }
}
