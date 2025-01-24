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
    val selectedNote = mutableStateOf<NoteEntity?>(null)
    fun changeSelectedNote(note: NoteEntity) {
        selectedNote.value = note
    }

    val selectedCategory = mutableStateOf<CategoryEntity?>(null)
    fun changeSelectedCategory(category: CategoryEntity?) {
        selectedCategory.value = category
    }
}