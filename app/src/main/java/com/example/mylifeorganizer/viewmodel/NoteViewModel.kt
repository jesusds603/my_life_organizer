package com.example.mylifeorganizer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.room.NoteWithCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(val notesRepository: NotesRepository) : ViewModel() {
    fun addNote(note: NoteEntity, onNoteAdded: (Long) -> Unit) {
        viewModelScope.launch {
            val noteId = notesRepository.insertNote(note)
            onNoteAdded(noteId) // Ejecuta una acción cuando se inserta la nota
        }
    }

    fun addCategory(category: CategoryEntity) {
        viewModelScope.launch {
            notesRepository.insertCategory(category)
        }
    }

    fun linkNoteWithCategory(noteId: Long, categoryId: Long) {
        viewModelScope.launch {
            notesRepository.linkNoteWithCategory(noteId, categoryId)
        }
    }

    // Obtener todas las notas con sus categorías
    val notesWithCategories = notesRepository.getNotesWithCategories()

    // Obtener todas las categorías con sus notas
    val categoriesWithNotes = notesRepository.getCategoriesWithNotes()

    // Filtrar notas por una categoría específica
    fun getNotesByCategory(categoryId: Int): Flow<List<NoteEntity>> {
        return notesRepository.getNotesByCategory(categoryId)
    }

    // Filtrar categorías por una nota específica
    fun getCategoriesByNote(noteId: Int): Flow<List<CategoryEntity>> {
        return notesRepository.getCategoriesByNote(noteId)
    }

    // Eliminar una nota
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
        }
    }

    // Eliminar una categoría
    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            notesRepository.deleteCategory(category)
        }
    }

    // Actualizar una nota
    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
        }
    }

    // Actualizar una categoría
    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            notesRepository.updateCategory(category)
        }

    }
}