package com.example.mylifeorganizer.repositories

import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.room.FolderWithSubfolders
import com.example.mylifeorganizer.room.NoteCategoryCrossRef
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.room.TaskCategoryCrossRef
import com.example.mylifeorganizer.room.TaskEntity
import com.example.mylifeorganizer.room.TaskHistoryEntity
import com.example.mylifeorganizer.room.TaskOccurrenceEntity
import com.example.mylifeorganizer.room.TaskWithCategories
import kotlinx.coroutines.flow.Flow

class NotesRepository (val noteDB: NoteDB) {

    private val noteDAO = noteDB.noteDao()

    // Insertar una nueva nota
    suspend fun insertNote(note: NoteEntity): Long {
        return noteDAO.insertNote(note)
    }

    // Obtener todas las notas
    fun getAllNotesWithCategories() = noteDAO.getAllNotesWithCategories()
    fun getAllNotes() = noteDAO.getAllNotes()

    fun getAllNotesWithoutContentWithCategories() = noteDAO.getAllNotesWithoutContentWithCategories()

    // Filtrar notas por una categoría específica
    fun getNotesByCategory(categoryId: Long) = noteDAO.getNotesByCategory(categoryId)

    fun getNoteWithCategoriesById(noteId: Long) = noteDAO.getNoteWithCategoriesById(noteId)

    // Eliminar una nota
    suspend fun deleteNote(note: NoteEntity) {
        noteDAO.deleteNote(note)
    }

    // Actualizar una nota
    suspend fun updateNote(note: NoteEntity) {
        noteDAO.updateNote(note)
    }

    // Método para actualizar una nota con sus categorías
    suspend fun updateNoteWithCategories(note: NoteEntity, categoryIds: List<Long>) {
        noteDAO.updateNoteWithCategories(note, categoryIds)
    }


    // -------------------------------------------------


    // Insertar una nueva categoría
    suspend fun insertCategory(category: CategoryEntity) {
        noteDAO.insertCategory(category)
    }

    // Obtener todas las categorías con sus notas
    fun getAllCategoriesWithNotes() = noteDAO.getAllCategoriesWithNotes()
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

    // Obtener todas las notas de una carpeta sin contenido
    fun getNotesInFolderWithoutContent(folderId: Long) = noteDAO.getNotesInFolderWithoutContent(folderId)

    // Vincular una nota con una carpeta
    suspend fun linkNoteWithFolder(noteId: Long, folderId: Long) {
        noteDAO.updateNoteFolderId(noteId, folderId)
    }

    // Vincular una carpeta con su subcarpeta
    suspend fun linkFolderWithSubfolder(parentFolderId: Long, subfolderId: Long) {
        noteDAO.updateFolderParentId(subfolderId, parentFolderId)
    }

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


    fun getTaskById(taskId: Long): TaskWithCategories = noteDAO.getTaskById(taskId)


    // Obtener todas las tareas con la misma prioridad
    fun getTasksByPriority(priority: Int) = noteDAO.getTasksByPriority(priority)

    // Eliminar una tarea
    suspend fun deleteTask(task: TaskEntity) {
        noteDAO.deleteTask(task)
    }

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

    // Método para actualizar una tarea con sus categorías
    suspend fun updateTaskWithCategories(task: TaskEntity, categoryIds: List<Long>) {
        noteDAO.updateTaskWithCategories(task, categoryIds)
    }

    suspend fun deleteTaskCategories(taskId: Long) {
        noteDAO.deleteTaskCategories(taskId)
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

    // 📌 ───────── Historial ─────────
    suspend fun insertTaskHistory(history: TaskHistoryEntity) = noteDAO.insertTaskHistory(history)

    fun getHistoryForOccurrence(occurrenceId: Long): Flow<List<TaskHistoryEntity>> = noteDAO.getHistoryForOccurrence(occurrenceId)




}