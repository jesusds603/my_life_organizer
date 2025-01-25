package com.example.mylifeorganizer.repositories

import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.room.FolderWithSubfolders
import com.example.mylifeorganizer.room.NoteCategoryCrossRef
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.room.NoteEntity

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

}