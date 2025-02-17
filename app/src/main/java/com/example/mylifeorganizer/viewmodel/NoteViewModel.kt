package com.example.mylifeorganizer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.CategoryFinanceEntity
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.room.FinanceEntity
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.room.FolderWithSubfolders
import com.example.mylifeorganizer.room.HabitEntity
import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.room.NoteWithCategories
import com.example.mylifeorganizer.room.PaymentMethodEntity
import com.example.mylifeorganizer.room.SettingsEntity
import com.example.mylifeorganizer.room.TaskEntity
import com.example.mylifeorganizer.room.TaskOccurrenceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NoteViewModel(private val notesRepository: NotesRepository) : ViewModel() {
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    //                      SETTINGS
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------

    fun insertSettings(settings: SettingsEntity) {
        viewModelScope.launch {
            notesRepository.insertSettings(settings)
        }
    }

    val settings = notesRepository.getSettings()

    fun updateSettings(settings: SettingsEntity) {
        viewModelScope.launch {
            notesRepository.updateSettings(settings)
            }
    }

    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    //                      NOTES
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    fun addNote(note: NoteEntity, onNoteAdded: (Long) -> Unit) {
        viewModelScope.launch {
            val noteId = notesRepository.insertNote(note)
            onNoteAdded(noteId) // Ejecuta una acción cuando se inserta la nota
        }
    }


    val notes = notesRepository.getAllNotes()
    val notesWithoutContentWithCategories = notesRepository.getAllNotesWithoutContentWithCategories()


    fun getNoteWithCategoriesById(noteId: Long): Flow<NoteWithCategories> {
        return notesRepository.getNoteWithCategoriesById(noteId)
    }

    // Eliminar una nota
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
        }
    }

    // Actualizar una nota
    fun updateNote(note: NoteEntity, onNoteUpdated: (Long) -> Unit) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
            onNoteUpdated(note.noteId) // Ejecuta una acción cuando se actualiza la nota
        }
    }


    // ---------------------------------------------------------------


    fun addCategory(category: CategoryEntity) {
        viewModelScope.launch {
            notesRepository.insertCategory(category)
        }
    }

    val categories = notesRepository.getAllCategories()

    // Filtrar categorías por una nota específica
    fun getCategoriesByNote(noteId: Long): Flow<List<CategoryEntity>> {
        return notesRepository.getCategoriesByNote(noteId)
    }

    fun getNotesIdByCategory(categoryId: Long): Flow<List<Long>> {
        return notesRepository.getNotesIdByCategory(categoryId)
    }

    // Eliminar una categoría
    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            notesRepository.deleteCategory(category)
        }
    }

    // Actualizar una categoría
    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            notesRepository.updateCategory(category)
        }

    }


    // ---------------------------------------------


    fun linkNoteWithCategory(noteId: Long, categoryId: Long) {
        viewModelScope.launch {
            notesRepository.linkNoteWithCategory(noteId, categoryId)
        }
    }

    // Eliminar todas las relaciones de categorias con una nota
    fun deleteNoteCategories(noteId: Long) {
        viewModelScope.launch {
            notesRepository.deleteNoteCategories(noteId)
        }
    }

    fun deleteRelationNoteCategory(noteId: Long, categoryId: Long) {
        viewModelScope.launch {
            notesRepository.deleteRelationNoteCategory(noteId, categoryId)
        }
    }


    // -----------------------------------------------


    // Insertar una nueva carpeta
    fun addFolder(folder: FolderEntity, onFolderAdded: (Long) -> Unit) {
        viewModelScope.launch {
            val folderId = notesRepository.insertFolder(folder)
            onFolderAdded(folderId) // Ejecuta una acción cuando se inserta la carpeta
        }
    }

    val folders = notesRepository.getAllFolders()

    fun getSubfolders(parentId: Long): Flow<List<FolderWithSubfolders>> {
        return notesRepository.getSubfolders(parentId)
    }

    fun getNotesInFolder(folderId: Long): Flow<List<NoteEntity>> {
        return notesRepository.getNotesInFolder(folderId)
    }

    fun countSubfoldersAndNotes(
        folderId: Long,
        onResult: (Int, Int) -> Unit
    ) {
        viewModelScope.launch {
            // Recoge subcarpetas y notas de manera reactiva
            val subfoldersFlow = getSubfolders(folderId)
            val notesFlow = getNotesInFolder(folderId)

            val subfolders = subfoldersFlow.first() // Obtenemos la lista actual
            val notes = notesFlow.first() // Obtenemos la lista actual

            var totalSubfolders = subfolders.size
            var totalNotes = notes.size

            // Procesar subcarpetas recursivamente
            subfolders.forEach { subfolder ->
                countSubfoldersAndNotes(subfolder.folder.folderId) { subfolderCount, noteCount ->
                    totalSubfolders += subfolderCount
                    totalNotes += noteCount
                }
            }

            // Devuelve el resultado acumulado
            onResult(totalSubfolders, totalNotes)
        }
    }

    fun countTotalSubfoldersAndNotes(
        folderId: Long,
        onResult: (Int, Int) -> Unit
    ) {
        viewModelScope.launch {
            // Obtener subcarpetas y notas asociadas al folder actual
            val subfoldersFlow = getSubfolders(folderId)
            val notesFlow = getNotesInFolder(folderId)

            val subfolders = subfoldersFlow.first() // Obtener la lista de subcarpetas actual
            val notes = notesFlow.first() // Obtener la lista de notas actual

            // Inicializar los totales con los elementos de la capa actual
            var totalSubfolders = subfolders.size
            var totalNotes = notes.size

            // Procesar subcarpetas de manera recursiva
            subfolders.forEach { subfolder ->
                // Llamada recursiva para cada subcarpeta
                countTotalSubfoldersAndNotes(subfolder.folder.folderId) { subfolderCount, noteCount ->
                    // Sumar los resultados recursivos al total
                    totalSubfolders += subfolderCount
                    totalNotes += noteCount

                    // Devolver el resultado acumulado una vez terminado
                    onResult(totalSubfolders, totalNotes)
                }
            }

            // Si no hay subcarpetas, devolver directamente el resultado
            if (subfolders.isEmpty()) {
                onResult(totalSubfolders, totalNotes)
            }
        }
    }

    // Eliminar una carpeta
    fun deleteFolder(folder: FolderEntity) {
        viewModelScope.launch {
            notesRepository.deleteFolder(folder)
        }
    }

    // Actualizar una carpeta
    fun updateFolder(folder: FolderEntity) {
        viewModelScope.launch {
            notesRepository.updateFolder(folder)
        }
    }

    // ----------------------------- TAREAS --------------------------------

    // Insertar una nueva tarea
    fun addTask(task: TaskEntity, onTaskAdded: (Long) -> Unit) {
        viewModelScope.launch {
            val taskId = notesRepository.insertTask(task)
            onTaskAdded(taskId) // Ejecuta una acción cuando se inserta la tarea
        }
    }

    // Todos los folders
    val tasks = notesRepository.getAllTasks()

    val tasksWithCategories = notesRepository.getAllTasksWithCategories()

    fun getOccurrencesTasksByDate(dueDate: String): Flow<List<TaskOccurrenceEntity>> {
        return notesRepository.getOccurrencesTasksByDate(dueDate)
    }


    // ------


    fun addCategoryTask(category: CategoryTaskEntity) {
        viewModelScope.launch {
            notesRepository.insertCategoryTask(category)
        }
    }

    val categoriesTasks = notesRepository.getAllCategoriesTasks()

    // Eliminar una categoría
    fun deleteCategoryTasks(category: CategoryTaskEntity) {
        viewModelScope.launch {
            notesRepository.deleteCategoryTasks(category)
        }
    }

    // Actualizar una categoría
    fun updateCategoryTask(category: CategoryTaskEntity) {
        viewModelScope.launch {
            notesRepository.updateCategoryTasks(category)
        }

    }

    fun linkTaskWithCategory(taskId: Long, categoryId: Long) {
        viewModelScope.launch {
            notesRepository.linkTaskWithCategory(taskId, categoryId)
        }
    }


    fun deleteTaskCategories(taskId: Long) {
        viewModelScope.launch {
            notesRepository.deleteTaskCategories(taskId)
        }
    }

    // Eliminar todas las relaciones de una categoria
    fun deleteRelationTaskCategory(categoryId: Long) {
        viewModelScope.launch {
            notesRepository.deleteRelationTaskCategory(categoryId)
        }
    }

    // Ocurrencias
    fun insertOccurrence(occurrence: TaskOccurrenceEntity) {
        viewModelScope.launch {
            notesRepository.insertOccurrence(occurrence)
        }
    }

    fun updateOccurrence(occurrence: TaskOccurrenceEntity) {
        viewModelScope.launch {
            notesRepository.updateOccurrence(occurrence)
        }
    }

    fun deleteTaskWithOccurrences(taskId: Long) {
        viewModelScope.launch {
            val occurrences = notesRepository.getOccurrencesForTask(taskId).first() // Obtener ocurrencias asociadas
            occurrences.forEach { notesRepository.deleteOccurrence(it) } // Eliminar cada ocurrencia
            notesRepository.deleteTaskById(taskId) // Finalmente, eliminar la tarea
        }
    }

    fun updateTaskById(task: TaskEntity, onTaskUpdated: (Long) -> Unit) {
        viewModelScope.launch {
            notesRepository.deleteOccurrencesForTask(task.taskId)
            notesRepository.updateTask(task)
            onTaskUpdated(task.taskId)
        }
    }

    fun getAllOccurrences(): Flow<List<TaskOccurrenceEntity>> {
        return notesRepository.getAllOccurrences()
    }


    // ----------------------------------------------------------------------
    // -------------------------------------------------------------------------

    //                                  FINANZAS

    // LiveData para observar datos en la UI
    val categoriesFinance: Flow<List<CategoryFinanceEntity>> = notesRepository.getAllCategoriesFinance()
    val paymentMethods: Flow<List<PaymentMethodEntity>> = notesRepository.getAllPaymentMethods()
    val financesWithCategories: Flow<List<FinanceWithCategories>> = notesRepository.getAllFinancesWithCategories()
    val getFinancesByDate: (String) -> Flow<List<FinanceWithCategories>> = { date -> notesRepository.getFinancesByDate(date) }
    val getFinancesByMonth: (String) -> Flow<List<FinanceWithCategories>> = { month -> notesRepository.getFinancesByMonth(month)}

    // Agregar finanza, categoría, método de pago y relaciones
    fun addFinance(finance: FinanceEntity, onFinanceAdded: (Long) -> Unit) {
        viewModelScope.launch {
            val financeId = notesRepository.insertFinance(finance)
            onFinanceAdded(financeId)
        }
    }

    fun addCategoryFinance(category: CategoryFinanceEntity) {
        viewModelScope.launch {
            notesRepository.insertCategoryFinance(category)
        }
    }

    fun addPaymentMethod(paymentMethod: PaymentMethodEntity) {
        viewModelScope.launch {
            notesRepository.insertPaymentMethod(paymentMethod)
        }
    }

    fun linkFinanceToCategory(financeId: Long, categoryId: Long) {
        viewModelScope.launch {
            notesRepository.addFinanceToCategory(financeId, categoryId)
        }
    }

    // Actualizar datos
    fun updateFinance(finance: FinanceEntity, onFinanceUpdated: (Long) -> Unit) {
        viewModelScope.launch {
            notesRepository.updateFinance(finance)
            onFinanceUpdated(finance.financeId)
        }
    }

    fun updateCategoryFinance(category: CategoryFinanceEntity) {
        viewModelScope.launch {
            notesRepository.updateCategoryFinance(category)
        }
    }

    fun updatePaymentMethod(paymentMethod: PaymentMethodEntity) {
        viewModelScope.launch {
            notesRepository.updatePaymentMethod(paymentMethod)
        }
    }

    // Función para actualizar paymentId a null donde coincida con el argumento
    fun updatePaymentIdToNull(paymentId: Long) {
        viewModelScope.launch {
            notesRepository.updatePaymentIdToNull(paymentId)
        }
    }

    // Eliminar datos
    fun deleteFinance(finance: FinanceEntity) {
        viewModelScope.launch {
            notesRepository.deleteFinance(finance)
        }
    }

    fun deleteFinanceCategories(financeId: Long) {
        viewModelScope.launch {
            notesRepository.deleteFinanceCategories(financeId)
        }
    }

    // Eliminar todas las relaciones de una categoria
    fun deleteRelationFinanceCategory(categoryId: Long) {
        viewModelScope.launch {
            notesRepository.deleteRelationFinanceCategory(categoryId)
        }
    }

    fun deleteCategoryFinance(category: CategoryFinanceEntity) {
        viewModelScope.launch {
            notesRepository.deleteCategoryFinance(category)
        }
    }

    fun deletePaymentMethod(paymentMethod: PaymentMethodEntity) {
        viewModelScope.launch {
            notesRepository.deletePaymentMethod(paymentMethod)
        }
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    //                      HABITS
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    fun addHabit(habit: HabitEntity, onHabitAdded: (Long) -> Unit) {
        viewModelScope.launch {
            val habitId = notesRepository.insertHabit(habit)
            onHabitAdded(habitId)
        }
    }

    val habits = notesRepository.getAllHabits()

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            notesRepository.deleteHabit(habit)
        }
    }

    fun insertHabitOccurrence(habitOccurrence: HabitOccurrenceEntity) {
        viewModelScope.launch {
            notesRepository.insertHabitOccurrence(habitOccurrence)
        }
    }

    val habitsOccurrences = notesRepository.getAllHabitOccurrences()

    fun getHabitOccurrencesByDate(date: String): Flow<List<HabitOccurrenceEntity>> {
        return notesRepository.getHabitOccurrencesByDate(date)
    }

    fun updateHabitOccurrence(habitOccurrence: HabitOccurrenceEntity) {
        viewModelScope.launch {
            notesRepository.updateHabitOccurrence(habitOccurrence)
        }
    }

    fun deleteHabitOccurrence(habitOccurrence: HabitOccurrenceEntity) {
        viewModelScope.launch {
            notesRepository.deleteHabitOccurrence(habitOccurrence)
        }
    }

}