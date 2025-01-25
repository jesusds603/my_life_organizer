package com.example.mylifeorganizer.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.room.FolderWithSubfolders
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

    // Obtener todas las notas con sus categorías
    val notesWithCategories = notesRepository.getAllNotesWithCategories()
    val notes = notesRepository.getAllNotes()
    val notesWithoutContentWithCategories = notesRepository.getAllNotesWithoutContentWithCategories()

    // Filtrar notas por una categoría específica
    fun getNotesByCategory(categoryId: Long): Flow<List<NoteEntity>> {
        return notesRepository.getNotesByCategory(categoryId)
    }

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

    // Método para actualizar una nota con sus categorías
    fun updateNoteWithCategories(note: NoteEntity, categoryIds: List<Long>) {
        viewModelScope.launch {
            notesRepository.updateNoteWithCategories(note, categoryIds)
        }
    }


    // ---------------------------------------------------------------


    fun addCategory(category: CategoryEntity) {
        viewModelScope.launch {
            notesRepository.insertCategory(category)
        }
    }

    // Obtener todas las categorías con sus notas
    val categoriesWithNotes = notesRepository.getAllCategoriesWithNotes()
    val categories = notesRepository.getAllCategories()

    // Filtrar categorías por una nota específica
    fun getCategoriesByNote(noteId: Long): Flow<List<CategoryEntity>> {
        return notesRepository.getCategoriesByNote(noteId)
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

    // Vincular una nota con una carpeta
    fun linkNoteWithFolder(noteId: Long, folderId: Long) {
        viewModelScope.launch {
            notesRepository.linkNoteWithFolder(noteId, folderId)
        }
    }

    // Vincular una carpeta con su subcarpeta
    fun linkFolderWithSubfolder(parentFolderId: Long, subfolderId: Long) {
        viewModelScope.launch {
            notesRepository.linkFolderWithSubfolder(parentFolderId, subfolderId)
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
}