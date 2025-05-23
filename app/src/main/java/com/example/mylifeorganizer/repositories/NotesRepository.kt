package com.example.mylifeorganizer.repositories

import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.CategoryFinanceEntity
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.room.FinanceCategoryCrossRef
import com.example.mylifeorganizer.room.FinanceEntity
import com.example.mylifeorganizer.room.FinanceWithCategories
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.room.HabitEntity
import com.example.mylifeorganizer.room.HabitOccurrenceEntity
import com.example.mylifeorganizer.room.NoteCategoryCrossRef
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.room.PaymentMethodEntity
import com.example.mylifeorganizer.room.SettingsEntity
import com.example.mylifeorganizer.room.TaskCategoryCrossRef
import com.example.mylifeorganizer.room.TaskEntity
import com.example.mylifeorganizer.room.TaskOccurrenceEntity
import com.example.mylifeorganizer.room.TaskWithCategories
import kotlinx.coroutines.flow.Flow

class NotesRepository (noteDB: NoteDB) {

    private val noteDAO = noteDB.noteDao()

    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    //                      SETTINGS
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    suspend fun insertSettings(settings: SettingsEntity) {
        noteDAO.insertSettings(settings)
    }

    fun getSettings() = noteDAO.getSettings()

    suspend fun updateSettings(settings: SettingsEntity) {
        noteDAO.updateSettings(settings)
    }


    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    //                      NOTAS
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------

    // Insertar una nueva nota
    suspend fun insertNote(note: NoteEntity): Long {
        return noteDAO.insertNote(note)
    }

    fun getAllNotes() = noteDAO.getAllNotes()

    fun getAllNotesWithoutContentWithCategories() = noteDAO.getAllNotesWithoutContentWithCategories()

    // Filtrar notas por una categoría específica
    fun getNotesIdByCategory(categoryId: Long) = noteDAO.getNotesIdByCategory(categoryId)

    fun getNoteWithCategoriesById(noteId: Long) = noteDAO.getNoteWithCategoriesById(noteId)

    // Eliminar una nota
    suspend fun deleteNote(note: NoteEntity) {
        noteDAO.deleteNote(note)
    }

    // Actualizar una nota
    suspend fun updateNote(note: NoteEntity) {
        noteDAO.updateNote(note)
    }

    // -------------------------------------------------


    // Insertar una nueva categoría
    suspend fun insertCategory(category: CategoryEntity) {
        noteDAO.insertCategory(category)
    }

    fun getAllCategories() = noteDAO.getAllCategories()

    // Filtrar categorías por una nota específica
    fun getCategoriesByNote(noteId: Long) = noteDAO.getCategoriesByNote(noteId)

    // Eliminar una categoría
    suspend fun deleteCategory(category: CategoryEntity) {
        noteDAO.deleteCategory(category)
    }

    // Actualizar una categoría
    suspend fun updateCategory(category: CategoryEntity) {
        noteDAO.updateCategory(category)
    }


    // --------------------------------------


    // Vincular una nota con una categoría
    suspend fun linkNoteWithCategory(noteId: Long, categoryId: Long) {
        noteDAO.insertNoteCategoryCrossRef(NoteCategoryCrossRef(noteId, categoryId))
    }

    suspend fun deleteNoteCategories(noteId: Long) {
        noteDAO.deleteNoteCategories(noteId)
    }

    suspend fun deleteRelationNoteCategory(noteId: Long, categoryId: Long) {
        noteDAO.deleteRelationNoteCategory(noteId, categoryId)
    }


    // --------------------------------------


    // Insertar una nueva carpeta
    suspend fun insertFolder(folder: FolderEntity): Long {
        return noteDAO.insertFolder(folder)
    }

    // Obtener todas las carpetas
    fun getAllFolders() = noteDAO.getAllFolders()

    // Obtener los subfolders de una carpeta
    fun getSubfolders(parentId: Long) = noteDAO.getSubfolders(parentId)

    // Obtener las notas dentro de una carpeta
    fun getNotesInFolder(folderId: Long) = noteDAO.getNotesInFolder(folderId)

    // Eliminar una carpeta
    suspend fun deleteFolder(folder: FolderEntity) {
        noteDAO.deleteFolder(folder)
    }

    // Actualizar una carpeta
    suspend fun updateFolder(folder: FolderEntity) {
        noteDAO.updateFolder(folder)
    }


    // ----------------------- TASKS --------------------------------

    // Insertar una nueva tarea
    suspend fun insertTask(task: TaskEntity): Long {
        return noteDAO.insertTask(task)
    }

    // Obtener todas las tareas
    fun getAllTasks() = noteDAO.getAllTasks()

    // Obtener todas las tareas con categorías
    fun getAllTasksWithCategories() = noteDAO.getAllTasksWithCategories()

    fun getOccurrencesTasksByDate(dueDate: String): Flow<List<TaskOccurrenceEntity>> = noteDAO.getOccurrencesTasksByDate(dueDate)

    fun getTaskById(taskId: Long): TaskWithCategories = noteDAO.getTaskById(taskId)

    suspend fun deleteTaskById(taskId: Long) {
        noteDAO.deleteTaskById(taskId)
    }

    // Actualizar una tarea
    suspend fun updateTask(task: TaskEntity) {
        noteDAO.updateTask(task)
    }

    // -----


    // Insertar una nueva categoría
    suspend fun insertCategoryTask(category: CategoryTaskEntity) {
        noteDAO.insertCategoryTask(category)
    }

    // Obtener todas las categorías
    fun getAllCategoriesTasks() = noteDAO.getAllCategoriesTasks()

    // Eliminar una categoría
    suspend fun deleteCategoryTasks(category: CategoryTaskEntity) {
        noteDAO.deleteCategoryTasks(category)
    }

    // Actualizar una categoría
    suspend fun updateCategoryTasks(category: CategoryTaskEntity) {
        noteDAO.updateCategoryTasks(category)
    }

    // Vincular una tarea con una categoría
    suspend fun linkTaskWithCategory(taskId: Long, categoryId: Long) {
        noteDAO.insertTaskCategoryCrossRef(TaskCategoryCrossRef(taskId, categoryId))
    }

    suspend fun deleteTaskCategories(taskId: Long) {
        noteDAO.deleteTaskCategories(taskId)
    }

    // Eliminar todas las relaciones de una categoria
    suspend fun deleteRelationTaskCategory(categoryId: Long) {
        noteDAO.deleteRelationTaskCategory(categoryId)
    }


    // 📌 ───────── Ocurrencias ─────────
    suspend fun insertOccurrence(occurrence: TaskOccurrenceEntity) = noteDAO.insertOccurrence(occurrence)

    suspend fun updateOccurrence(occurrence: TaskOccurrenceEntity) = noteDAO.updateOccurrence(occurrence)

    suspend fun deleteOccurrence(occurrence: TaskOccurrenceEntity) = noteDAO.deleteOccurrence(occurrence)

    suspend fun deleteOccurrencesForTask(taskId: Long) {
        noteDAO.deleteOccurrencesForTask(taskId)
    }

    fun getAllOccurrences(): Flow<List<TaskOccurrenceEntity>> = noteDAO.getAllOccurrences()

    fun getOccurrencesForTask(taskId: Long): Flow<List<TaskOccurrenceEntity>> = noteDAO.getOccurrencesForTask(taskId)

    fun getOccurrencesByDate(date: String): Flow<List<TaskOccurrenceEntity>> = noteDAO.getOccurrencesByDate(date)


    // ----------------------------------------------------------------------
    // -------------------------------------------------------------------------

    //                                  FINANZAS


    // Insertar datos
    suspend fun insertFinance(finance: FinanceEntity): Long = noteDAO.insertFinance(finance)

    suspend fun insertCategoryFinance(category: CategoryFinanceEntity): Long = noteDAO.insertCategoryFinance(category)

    suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity): Long = noteDAO.insertPaymentMethod(paymentMethod)

    // Relacionar finanza con categorías y métodos de pago
    suspend fun addFinanceToCategory(financeId: Long, categoryId: Long) {
        noteDAO.insertFinanceCategoryCrossRef(FinanceCategoryCrossRef(financeId, categoryId))
    }

    fun getFinancesByDate(date: String): Flow<List<FinanceWithCategories>> = noteDAO.getFinancesByDate(date)

    fun getFinancesByMonth(month: String): Flow<List<FinanceWithCategories>> = noteDAO.getFinancesByMonth(month)

    fun getAllCategoriesFinance(): Flow<List<CategoryFinanceEntity>> = noteDAO.getAllCategoriesFinance()

    fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>> = noteDAO.getAllPaymentMethods()

    // Obtener finanzas con detalles
    fun getAllFinancesWithCategories(): Flow<List<FinanceWithCategories>> = noteDAO.getAllFinancesWithCategories()

    // Obtener finanzas filtradas
    fun getFinancesByCategory(categoryId: Long): Flow<List<FinanceEntity>> = noteDAO.getFinancesByCategory(categoryId)

    // Actualizar datos
    suspend fun updateFinance(finance: FinanceEntity) = noteDAO.updateFinance(finance)

    suspend fun updateCategoryFinance(category: CategoryFinanceEntity) = noteDAO.updateCategoryFinance(category)

    suspend fun updatePaymentMethod(paymentMethod: PaymentMethodEntity) = noteDAO.updatePaymentMethod(paymentMethod)

    // Función para actualizar paymentId a null donde coincida con el argumento
    suspend fun updatePaymentIdToNull(paymentId: Long): Int = noteDAO.updatePaymentIdToNull(paymentId)

    // Eliminar datos
    suspend fun deleteFinance(finance: FinanceEntity) = noteDAO.deleteFinance(finance)

    // Eliminar todas las relaciones categorías relacionadas con una finanza
    suspend fun deleteFinanceCategories(financeId: Long) {
        noteDAO.deleteFinanceCategories(financeId)
    }

    // Eliminar todas las relaciones de una categoria
    suspend fun deleteRelationFinanceCategory(categoryId: Long) {
        noteDAO.deleteRelationFinanceCategory(categoryId)
    }

    suspend fun deleteCategoryFinance(category: CategoryFinanceEntity) = noteDAO.deleteCategoryFinance(category)

    suspend fun deletePaymentMethod(paymentMethod: PaymentMethodEntity) = noteDAO.deletePaymentMethod(paymentMethod)


    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    //                      HABITS
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    suspend fun insertHabit(habit: HabitEntity): Long = noteDAO.insertHabit(habit)

    fun getAllHabits(): Flow<List<HabitEntity>> = noteDAO.getAllHabits()

    fun getHabitById(habitId: Long): HabitEntity = noteDAO.getHabitById(habitId)

    suspend fun deleteHabit(habit: HabitEntity) = noteDAO.deleteHabit(habit)

    suspend fun insertHabitOccurrence(habitOccurrence: HabitOccurrenceEntity): Long = noteDAO.insertHabitOccurrence(habitOccurrence)

    fun getAllHabitOccurrences(): Flow<List<HabitOccurrenceEntity>> = noteDAO.getAllHabitOccurrences()

    fun getHabitOccurrencesByDate(date: String): Flow<List<HabitOccurrenceEntity>> = noteDAO.getHabitOccurrencesByDate(date)

    suspend fun updateHabitOccurrence(habitOccurrence: HabitOccurrenceEntity) = noteDAO.updateHabitOccurrence(habitOccurrence)

    suspend fun deleteHabitOccurrence(habitOccurrence: HabitOccurrenceEntity) = noteDAO.deleteHabitOccurrence(habitOccurrence)



}