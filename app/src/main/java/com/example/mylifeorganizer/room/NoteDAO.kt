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

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Transaction
    @Query("""
    SELECT noteId, title, createdAt, updatedAt, isFavorite, isArchived 
    FROM notes
""")
    fun getAllNotesWithoutContentWithCategories(): Flow<List<NoteWithoutContentWithCategories>>


    // Obtener todas las categorías con sus notas
    @Transaction
    @Query("SELECT * FROM categories")
    fun getAllCategoriesWithNotes(): Flow<List<CategoryWithNotes>>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>


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

    @Transaction
    @Query("""
    SELECT * 
    FROM notes
    WHERE noteId = :noteId
""")
    fun getNoteWithCategoriesById(noteId: Long): Flow<NoteWithCategories>


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


    //-----------------------------------------------------------------------

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

}
