package com.example.mylifeorganizer.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.NoteEntity

class AppViewModel: ViewModel() {
    // Language state
    val isLangEng = mutableStateOf(true)

    fun toggleLanguage() {
        isLangEng.value = !isLangEng.value
    }

    // Tab state
    var selectedTab = mutableStateOf("Home")

    fun changeTab(newTab: String) {
        selectedTab.value = newTab
    }

    // Estado de activación de las tarjetas de inicio

    val isAddingNote = mutableStateOf(false)
    fun toggleAddingNote() {
        isAddingNote.value = !isAddingNote.value
    }
    val idFolderForAddingNote = mutableStateOf<Long?>(null)
    fun changeIdFolderForAddingNote(folderId: Long?) {
        idFolderForAddingNote.value = folderId
    }
    val idFolderForAddingSubFolder = mutableStateOf<Long?>(null)
    fun changeIdFolderForAddingSubFolder(folderId: Long?) {
        idFolderForAddingSubFolder.value = folderId
    }

    val isAddingDaily = mutableStateOf(false)
    fun toggleAddingDaily() {
        isAddingDaily.value = !isAddingDaily.value
    }

    // Estado para visualizar una nota
    val isShowingNote = mutableStateOf(false)
    fun toggleShowingNote() {
        isShowingNote.value = !isShowingNote.value
    }

    // -------------------------------------------------
    // NOTAS
    val selectedNoteId = mutableStateOf<Long?>(null)
    fun changeSelectedNoteId(noteId: Long?) {
        selectedNoteId.value = noteId
    }

    val selectedCategory = mutableStateOf<CategoryEntity?>(null)
    fun changeSelectedCategory(category: CategoryEntity?) {
        selectedCategory.value = category
    }
}