package com.example.mylifeorganizer.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // Insertar una nueva nota
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    // Insertar una nueva categoría
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    // Vincular una nota con una categoría
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteCategoryCrossRef(crossRef: NoteCategoryCrossRef)

    // Obtener todas las notas con sus categorías
    @Transaction
    @Query("SELECT * FROM notes")
    fun getAllNotesWithCategories(): Flow<List<NoteWithCategories>>

//    // Obtener todas las notas sin su contenido (para la screen)
//    @Transaction
//    @Query("SELECT noteId, title, createdAt, updatedAt, isFavorite, isArchived FROM notes")
//    fun getAllNotesDescription(): Flow<List<NoteSummaryWithCategories>>

    // Obtener todas las categorías con sus notas
    @Transaction
    @Query("SELECT * FROM categories")
    fun getAllCategoriesWithNotes(): Flow<List<CategoryWithNotes>>


    // Filtrar notas por una categoría específica
    @Transaction
    @Query("""
        SELECT * FROM notes 
        INNER JOIN note_category_cross_ref 
        ON notes.noteId = note_category_cross_ref.noteId 
        WHERE note_category_cross_ref.categoryId = :categoryId
    """)
    fun getNotesByCategory(categoryId: Long): Flow<List<NoteEntity>>

    // Obtener todas las categorías asociadas a una nota específica
    @Transaction
    @Query("""
    SELECT * FROM categories 
    INNER JOIN note_category_cross_ref 
    ON categories.categoryId = note_category_cross_ref.categoryId 
    WHERE note_category_cross_ref.noteId = :noteId
""")
    fun getCategoriesByNote(noteId: Long): Flow<List<CategoryEntity>>


    // Eliminar una nota
    @Delete
    suspend fun deleteNote(note: NoteEntity)

    // Eliminar una categoría
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    // Actualizar una nota
    @Update
    suspend fun updateNote(note: NoteEntity)

    // Actualizar una categoría
    @Update
    suspend fun updateCategory(category: CategoryEntity)
}
