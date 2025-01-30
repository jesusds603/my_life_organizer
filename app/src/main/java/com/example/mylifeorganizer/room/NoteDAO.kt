package com.example.mylifeorganizer.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Transaction
    @Query("SELECT * FROM notes")
    fun getAllNotesWithCategories(): Flow<List<NoteWithCategories>>

    @Transaction
    @Query("""
    SELECT noteId, title, createdAt, updatedAt, isFavorite, isArchived, folderId 
    FROM notes
    """)
    fun getAllNotesWithoutContentWithCategories(): Flow<List<NoteWithoutContentWithCategories>>

    @Transaction
    @Query("""
        SELECT * FROM notes 
        INNER JOIN note_category_cross_ref 
        ON notes.noteId = note_category_cross_ref.noteId 
        WHERE note_category_cross_ref.categoryId = :categoryId
    """)
    fun getNotesByCategory(categoryId: Long): Flow<List<NoteEntity>>

    @Transaction
    @Query("""
    SELECT * 
    FROM notes
    WHERE noteId = :noteId
    """)
    fun getNoteWithCategoriesById(noteId: Long): Flow<NoteWithCategories>

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)


    // -----------------------------------------------------------------------


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Transaction
    @Query("SELECT * FROM categories")
    fun getAllCategoriesWithNotes(): Flow<List<CategoryWithNotes>>

    @Transaction
    @Query("""
    SELECT * FROM categories 
    INNER JOIN note_category_cross_ref 
    ON categories.categoryId = note_category_cross_ref.categoryId 
    WHERE note_category_cross_ref.noteId = :noteId
    """)
    fun getCategoriesByNote(noteId: Long): Flow<List<CategoryEntity>>

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)


    // -----------------------------------------------------------------------


    // Vincular una nota con una categoría
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteCategoryCrossRef(crossRef: NoteCategoryCrossRef)

    // Eliminar todas las categorías relacionadas con una nota
    @Query("DELETE FROM note_category_cross_ref WHERE noteId = :noteId")
    suspend fun deleteNoteCategories(noteId: Long)

    // Insertar relaciones en lote
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteCategories(noteCategoryCrossRefs: List<NoteCategoryCrossRef>)

    // Método para actualizar una nota y sus categorías
    @Transaction
    suspend fun updateNoteWithCategories(note: NoteEntity, categoryIds: List<Long>) {
        // Actualizar la nota
        updateNote(note)

        // Eliminar las relaciones existentes de la nota con categorías
        deleteNoteCategories(note.noteId)

        // Insertar las nuevas relaciones de la nota con categorías
        val noteCategoryCrossRefs = categoryIds.map { categoryId ->
            NoteCategoryCrossRef(noteId = note.noteId, categoryId = categoryId)
        }
        insertNoteCategories(noteCategoryCrossRefs)
    }


    // ---------------------------  FOLDERS --------------------------------------------


    // Insertar una nueva carpeta
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: FolderEntity): Long

    // Obtener todas las carpetas
    @Query("SELECT * FROM folders")
    fun getAllFolders(): Flow<List<FolderEntity>>

    // Obtener las subcarpetas de una carpeta específica
    @Transaction
    @Query("SELECT * FROM folders WHERE parentFolderId = :parentId")
    fun getSubfolders(parentId: Long): Flow<List<FolderWithSubfolders>>

    // Obtener todas las notas de una carpeta
    @Transaction
    @Query("SELECT * FROM notes WHERE folderId = :folderId")
    fun getNotesInFolder(folderId: Long): Flow<List<NoteEntity>>

    // Obtener todas las notas de una carpeta sin contenido
    @Transaction
    @Query("""
    SELECT noteId, title, createdAt, updatedAt, isFavorite, isArchived, folderId 
    FROM notes
    WHERE folderId = :folderId
    """)
    fun getNotesInFolderWithoutContent(folderId: Long): Flow<List<NoteWithoutContentWithCategories>>

    // Insertar la relación de nota con carpeta
    @Query("UPDATE notes SET folderId = :folderId WHERE noteId = :noteId")
    suspend fun updateNoteFolderId(noteId: Long, folderId: Long)

    // Actualizar la relación de carpeta con subcarpeta
    @Query("UPDATE folders SET parentFolderId = :parentFolderId WHERE folderId = :subfolderId")
    suspend fun updateFolderParentId(subfolderId: Long, parentFolderId: Long)

    // Eliminar una carpeta
    @Delete
    suspend fun deleteFolder(folder: FolderEntity)

    // Actualizar una carpeta
    @Update
    suspend fun updateFolder(folder: FolderEntity)

    // --------------------------- DAILY ---------------------------


    // ---------------------------- TASKS -----------------------------

    // Insertar una nueva tarea
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    // Obtener todas las tareas
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    // Obtener todas las tareas con sus categorías
    @Transaction
    @Query("SELECT * FROM tasks")
    fun getAllTasksWithCategories() : Flow<List<TaskWithCategories>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    fun getTaskById(taskId: Long): TaskWithCategories

    // Obtener todas lsa tareas con la misma prioridad
    @Query("SELECT * FROM tasks WHERE priority = :priority")
    fun getTasksByPriority(priority: Int): Flow<List<TaskEntity>>

    // Eliminar una tarea
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE taskId = :taskId")
    suspend fun deleteTaskById(taskId: Long)

    // Actualizar una tarea
    @Update
    suspend fun updateTask(task: TaskEntity)

    // ------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryTask(category: CategoryTaskEntity): Long

    @Query("SELECT * FROM categories_tasks")
    fun getAllCategoriesTasks(): Flow<List<CategoryTaskEntity>>

    @Delete
    suspend fun deleteCategoryTasks(category: CategoryTaskEntity)

    @Update
    suspend fun updateCategoryTasks(categpry: CategoryTaskEntity)

    // Vincular una tarea con una categoría
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCategoryCrossRef(crossRef: TaskCategoryCrossRef)

    // Eliminar todas las categorías relacionadas con una tarea
    @Query("DELETE FROM task_category_cross_ref WHERE taskId = :taskId")
    suspend fun deleteTaskCategories(taskId: Long)

    // Insertar relaciones en lote
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCategories(taskCategoryCrossRefs: List<TaskCategoryCrossRef>)

    // Método para actualizar una tarea y sus categorías
    @Transaction
    suspend fun updateTaskWithCategories(task: TaskEntity, categoryIds: List<Long>) {
        // Actualizar la tarea
        updateTask(task)

        // Eliminar las relaciones existentes de la tarea con categorías
        deleteTaskCategories(task.taskId)

        // Insertar las nuevas relaciones de la tarea con categorías
        val taskCategoryCrossRefs = categoryIds.map { categoryId ->
            TaskCategoryCrossRef(taskId = task.taskId, categoryId = categoryId)
        }
        insertTaskCategories(taskCategoryCrossRefs)
    }


    // 📌 ───────── Ocurrencias de Tareas ─────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccurrence(occurrence: TaskOccurrenceEntity): Long

    @Update
    suspend fun updateOccurrence(occurrence: TaskOccurrenceEntity)

    @Delete
    suspend fun deleteOccurrence(occurrence: TaskOccurrenceEntity)

    @Query("SELECT * FROM task_occurrences")
    fun getAllOccurrences(): Flow<List<TaskOccurrenceEntity>>

    @Query("SELECT * FROM task_occurrences WHERE taskId = :taskId ORDER BY dueDate ASC")
    fun getOccurrencesForTask(taskId: Long): Flow<List<TaskOccurrenceEntity>>

    @Query("SELECT * FROM task_occurrences WHERE dueDate = :date ORDER BY dueTime ASC")
    fun getOccurrencesByDate(date: String): Flow<List<TaskOccurrenceEntity>>

    // 📌 ───────── Historial de Tareas ─────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskHistory(history: TaskHistoryEntity)

    @Query("SELECT * FROM task_history WHERE occurrenceId = :occurrenceId ORDER BY timestamp DESC")
    fun getHistoryForOccurrence(occurrenceId: Long): Flow<List<TaskHistoryEntity>>


}


