package com.example.mylifeorganizer.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.NoteEntity
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class AppViewModel(application: Application) : AndroidViewModel(application)  {

    private val noteDB = NoteDB.getInstance(application.applicationContext)
    private val notesRepository = NotesRepository(noteDB)

    // Aquí inicializamos NoteViewModel para compartirlo
    val noteViewModel: NoteViewModel = NoteViewModel(notesRepository)

    // Language state
    val isLangEng = mutableStateOf(true)

    fun toggleLanguage() {
        isLangEng.value = !isLangEng.value
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

    // Ordenar notas en la screen
    // Valores: createdAscending, createdDescending, updatedAscending, updatedDescending, nameAscending, nameDescending
    val selectedOrderingNotes = mutableStateOf("updatedDescending") // la mas reciente arriba
    fun changeSelectedOrderingNotes(ordering: String) {
        selectedOrderingNotes.value = ordering
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

    val titleNewTask = mutableStateOf("")
    fun changeTitleNewTask(title: String) {
        titleNewTask.value = title
    }

    val descriptionNewTask = mutableStateOf("")
    fun changeDescriptionNewTask(description: String) {
        descriptionNewTask.value = description
    }

    val showDialogCreateTask = mutableStateOf(false)
    fun toggleShowDialogCreateTask() {
        showDialogCreateTask.value = !showDialogCreateTask.value
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


}