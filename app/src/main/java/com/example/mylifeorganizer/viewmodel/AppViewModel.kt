package com.example.mylifeorganizer.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

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
}