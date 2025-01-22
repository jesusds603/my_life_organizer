package com.example.mylifeorganizer.repositories

import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.CategoryWithNotes
import com.example.mylifeorganizer.room.NoteCategoryCrossRef
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.room.NoteWithCategories

class NotesRepository (val noteDB: NoteDB) {

    private val noteDAO = noteDB.noteDao()

    // Insertar una nueva nota
    suspend fun insertNote(note: NoteEntity): Long {
        return noteDAO.insertNote(note)
    }

    // Insertar una nueva categoría
    suspend fun insertCategory(category: CategoryEntity) {
        noteDAO.insertCategory(category)
    }

    // Vincular una nota con una categoría
    suspend fun linkNoteWithCategory(noteId: Long, categoryId: Long) {
        noteDAO.insertNoteCategoryCrossRef(NoteCategoryCrossRef(noteId, categoryId))
    }

    // Obtener todas las notas
    fun getNotesWithCategories() = noteDAO.getNotesWithCategories()

    // Obtener todas las categorías con sus notas
    fun getCategoriesWithNotes() = noteDAO.getCategoriesWithNotes()

    // Filtrar notas por una categoría específica
    fun getNotesByCategory(categoryId: Int) = noteDAO.getNotesByCategory(categoryId)

    // Filtrar categorías por una nota específica
    fun getCategoriesByNote(noteId: Int) = noteDAO.getCategoriesByNote(noteId)

    // Eliminar una nota
    suspend fun deleteNote(note: NoteEntity) {
        noteDAO.deleteNote(note)
    }

    // Eliminar una categoría
    suspend fun deleteCategory(category: CategoryEntity) {
        noteDAO.deleteCategory(category)
    }

    // Actualizar una nota
    suspend fun updateNote(note: NoteEntity) {
        noteDAO.updateNote(note)
    }

    // Actualizar una categoría
    suspend fun updateCategory(category: CategoryEntity) {
        noteDAO.updateCategory(category)
    }
}