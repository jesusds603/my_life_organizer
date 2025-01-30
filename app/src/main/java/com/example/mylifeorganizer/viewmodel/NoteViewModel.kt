package com.example.mylifeorganizer.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.CategoryEntity
import com.example.mylifeorganizer.room.CategoryTaskEntity
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.room.FolderWithSubfolders
import com.example.mylifeorganizer.room.NoteEntity
import com.example.mylifeorganizer.room.NoteWithCategories
import com.example.mylifeorganizer.room.NoteWithoutContentWithCategories
import com.example.mylifeorganizer.room.TaskEntity
import com.example.mylifeorganizer.room.TaskHistoryEntity
import com.example.mylifeorganizer.room.TaskOccurrenceEntity
import com.example.mylifeorganizer.room.TaskWithCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    // Obtener todas las notas de una carpeta sin contenido
    fun getNotesInFolderWithoutContent(folderId: Long): Flow<List<NoteWithoutContentWithCategories>> {
        return notesRepository.getNotesInFolderWithoutContent(folderId)
    }

    // Mapa que rastrea las notas de carpetas expandidas
    private val _expandedFolderNotes = mutableStateMapOf<Long, List<NoteWithoutContentWithCategories>>()
    val expandedFolderNotes: Map<Long, List<NoteWithoutContentWithCategories>> get() = _expandedFolderNotes

    // Función para cargar las notas de una carpeta cuando se expande
    fun loadNotesForExpandedFolder(folderId: Long) {
        viewModelScope.launch {
            getNotesInFolderWithoutContent(folderId).collect { notes ->
                _expandedFolderNotes[folderId] = notes
            }
        }
    }

    fun countSubfoldersAndNotes(
        folderId: Long,
        onResult: (Int, Int) -> Unit
    ) {
        viewModelScope.launch {
            // Recoge subcarpetas y notas de manera reactiva
            val subfoldersFlow = getSubfolders(folderId)
            val notesFlow = getNotesInFolder(folderId)

            val subfolders = subfoldersFlow.first() // Obtenemos la lista actual
            val notes = notesFlow.first() // Obtenemos la lista actual

            var totalSubfolders = subfolders.size
            var totalNotes = notes.size

            // Procesar subcarpetas recursivamente
            subfolders.forEach { subfolder ->
                countSubfoldersAndNotes(subfolder.folder.folderId) { subfolderCount, noteCount ->
                    totalSubfolders += subfolderCount
                    totalNotes += noteCount
                }
            }

            // Devuelve el resultado acumulado
            onResult(totalSubfolders, totalNotes)
        }
    }

    fun countTotalSubfoldersAndNotes(
        folderId: Long,
        onResult: (Int, Int) -> Unit
    ) {
        viewModelScope.launch {
            // Obtener subcarpetas y notas asociadas al folder actual
            val subfoldersFlow = getSubfolders(folderId)
            val notesFlow = getNotesInFolder(folderId)

            val subfolders = subfoldersFlow.first() // Obtener la lista de subcarpetas actual
            val notes = notesFlow.first() // Obtener la lista de notas actual

            // Inicializar los totales con los elementos de la capa actual
            var totalSubfolders = subfolders.size
            var totalNotes = notes.size

            // Procesar subcarpetas de manera recursiva
            subfolders.forEach { subfolder ->
                // Llamada recursiva para cada subcarpeta
                countTotalSubfoldersAndNotes(subfolder.folder.folderId) { subfolderCount, noteCount ->
                    // Sumar los resultados recursivos al total
                    totalSubfolders += subfolderCount
                    totalNotes += noteCount

                    // Devolver el resultado acumulado una vez terminado
                    onResult(totalSubfolders, totalNotes)
                }
            }

            // Si no hay subcarpetas, devolver directamente el resultado
            if (subfolders.isEmpty()) {
                onResult(totalSubfolders, totalNotes)
            }
        }
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

    // ----------------------------- TAREAS --------------------------------

    // Insertar una nueva tarea
    fun addTask(task: TaskEntity, onTaskAdded: (Long) -> Unit) {
        viewModelScope.launch {
            val taskId = notesRepository.insertTask(task)
            onTaskAdded(taskId) // Ejecuta una acción cuando se inserta la tarea
        }
    }

    // Todos los folders
    val tasks = notesRepository.getAllTasks()

    val tasksWithCategories = notesRepository.getAllTasksWithCategories()

    fun getTaskById(taskId: Long): TaskWithCategories {
        return notesRepository.getTaskById(taskId)
    }


    // Obtener todas las tareas con la misma prioridad
    fun getTasksByPriority(priority: Int): Flow<List<TaskEntity>> {
        return notesRepository.getTasksByPriority(priority)
    }


    // Eliminar una tarea
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            notesRepository.deleteTask(task)
        }
    }

    // Actualizar una tarea
    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            notesRepository.updateTask(task)
        }
    }

    // ------


    fun addCategoryTask(category: CategoryTaskEntity) {
        viewModelScope.launch {
            notesRepository.insertCategoryTask(category)
        }
    }

    val categoriesTasks = notesRepository.getAllCategoriesTasks()

    // Eliminar una categoría
    fun deleteCategoryTasks(category: CategoryTaskEntity) {
        viewModelScope.launch {
            notesRepository.deleteCategoryTasks(category)
        }
    }

    // Actualizar una categoría
    fun updateCategoryTask(category: CategoryTaskEntity) {
        viewModelScope.launch {
            notesRepository.updateCategoryTasks(category)
        }

    }

    fun linkTaskWithCategory(taskId: Long, categoryId: Long) {
        viewModelScope.launch {
            notesRepository.linkTaskWithCategory(taskId, categoryId)
        }
    }

    // Método para actualizar una nota con sus categorías
    fun updateTaskWithCategories(task: TaskEntity, categoryIds: List<Long>) {
        viewModelScope.launch {
            notesRepository.updateTaskWithCategories(task, categoryIds)
        }
    }

    // Ocurrencias
    fun insertOccurrence(occurrence: TaskOccurrenceEntity) {
        viewModelScope.launch {
            notesRepository.insertOccurrence(occurrence)
        }
    }

    fun updateOccurrence(occurrence: TaskOccurrenceEntity) {
        viewModelScope.launch {
            notesRepository.updateOccurrence(occurrence)
        }
    }

    fun deleteOccurrence(occurrence: TaskOccurrenceEntity) {
        viewModelScope.launch {
            notesRepository.deleteOccurrence(occurrence)
        }
    }

    fun deleteTaskWithOccurrences(taskId: Long) {
        viewModelScope.launch {
            val occurrences = notesRepository.getOccurrencesForTask(taskId).first() // Obtener ocurrencias asociadas
            occurrences.forEach { notesRepository.deleteOccurrence(it) } // Eliminar cada ocurrencia
            notesRepository.deleteTaskById(taskId) // Finalmente, eliminar la tarea
        }
    }


    fun getAllOccurrences(): Flow<List<TaskOccurrenceEntity>> {
        return notesRepository.getAllOccurrences()
    }

    fun getOccurrencesForTask(taskId: Long): Flow<List<TaskOccurrenceEntity>> {
        return notesRepository.getOccurrencesForTask(taskId)
    }

    fun getOccurrencesByDate(date: String): Flow<List<TaskOccurrenceEntity>> {
        return notesRepository.getOccurrencesByDate(date)
    }

    // Historial
    fun insertTaskHistory(history: TaskHistoryEntity) {
        viewModelScope.launch {
            notesRepository.insertTaskHistory(history)
        }
    }

    fun getHistoryForOccurrence(occurrenceId: Long): Flow<List<TaskHistoryEntity>> {
        return notesRepository.getHistoryForOccurrence(occurrenceId)
    }

}