package com.example.mylifeorganizer.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


// Entidad para las notas
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId") // Renombramos la columna a noteId
    val noteId: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    @ColumnInfo(name = "folderId") // Carpeta asociada, null si no tiene
    val folderId: Long
)

// Entidad para las categorías
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId") // Renombramos la columna a categoryId
    val categoryId: Long = 0,
    val name: String,
    val bgColor: String,
)

// Tabla intermedia para la relación muchos a muchos
@Entity(
    primaryKeys = ["noteId", "categoryId"], // Relacionamos con las columnas renombradas
    tableName = "note_category_cross_ref"
)
data class NoteCategoryCrossRef(
    @ColumnInfo(name = "noteId") // Referencia a la columna "noteId" de NoteEntity
    val noteId: Long,
    @ColumnInfo(name = "categoryId") // Referencia a la columna "categoryId" de CategoryEntity
    val categoryId: Long
)

// Relación completa entre las notas y las categorías
data class NoteWithCategories(
    @Embedded
    val note: NoteEntity,
    @Relation(
        parentColumn = "noteId", // Usamos el nuevo nombre de columna "noteId"
        entityColumn = "categoryId", // Usamos el nuevo nombre de columna "categoryId"
        associateBy = androidx.room.Junction(NoteCategoryCrossRef::class)
    )
    val categories: List<CategoryEntity>
)

data class CategoryWithNotes(
    @Embedded
    val category: CategoryEntity,
    @Relation(
        parentColumn = "categoryId", // Usamos el nuevo nombre de columna "categoryId"
        entityColumn = "noteId", // Usamos el nuevo nombre de columna "noteId"
        associateBy = androidx.room.Junction(NoteCategoryCrossRef::class)
    )
    val notes: List<NoteEntity>
)


data class NoteWithoutContent(
    val noteId: Long,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isFavorite: Boolean,
    val isArchived: Boolean,
    val folderId: Long
)

data class NoteWithoutContentWithCategories(
    @Embedded
    val note: NoteWithoutContent,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "categoryId",
        associateBy = androidx.room.Junction(NoteCategoryCrossRef::class)
    )
    val categories: List<CategoryEntity>
)


// ------------------------------ Para carpetas ----------------------------------


@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey
    @ColumnInfo(name = "folderId") // Renombramos la columna a folderId
    val folderId: Long,
    val name: String,
    @ColumnInfo(name = "parentFolderId") // Renombramos la columna a parentFolderId
    val parentFolderId: Long,
    val createdAt: Long = System.currentTimeMillis(),
)

// Relación de una carpeta con sus subcarpetas
data class FolderWithSubfolders(
    @Embedded
    val folder: FolderEntity,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "parentFolderId"
    )
    val subfolders: List<FolderEntity>
)


// ----------------------- NOTAS DIARIAS --------------------

@Entity(tableName = "daily_notes")
data class DailyNoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "dailyNoteId")
    val dailyNoteId: Long = 0,
    val day: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val quote: String,
    val hadGoodDream: Int,
    val morningNote: String,
    val afternoonNote: String,
    val eveningNote: String,
    val nightNote: String,
    val noteForTomorrow: String,
    val isFavorite: Boolean = false,
    val amountStars: Int = 0,
    val lessonLearned: String,
    val bestOfTheDay: String,
    val improvementPlan: String,
)


// ------------------   TASKS --------------------------

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskId")
    val taskId: Long = 0,
    val title: String,
    val description: String = "",

    val isRecurring: Boolean = false,
    val recurrencePattern: String = "", // "daily", "weekly", "monthly", "yearly", "custom"
    val numDays: Int = 0, // Número de días para el daily
    val recurrenceWeekDays: String = "", // indices del 0 al 67
    val numWeeks: Int = 0, // Número de semanas para el weekly
    val recurrenceMonthDays: String = "", // [1, 15, -1] (último día)
    val numMonths: Int = 0, // Número de meses para el monthly
    val recurrenceYearDays: String = "", // ["MM/DD", "MM/DD"]
    val numYears: Int = 0, // Número de años para el yearly
    val recurrenceInterval: Int = 0, // Para "custom", en días
    val numTimes: Int = 0, // Número de veces para el custom

    val priority: Int = 0,
    val isReminderSet: Boolean = false,
    val reminderTime: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)


@Entity(
    tableName = "task_occurrences",
    foreignKeys = [ForeignKey(entity = TaskEntity::class, parentColumns = ["taskId"], childColumns = ["taskId"], onDelete = CASCADE)]
)
data class TaskOccurrenceEntity(
    @PrimaryKey(autoGenerate = true)
    val occurrenceId: Long = 0,
    val taskId: Long,  // Relación con `tasks`
    val dueDate: String, // Fecha específica de la ocurrencia (YYYY-MM-DD)
    val dueTime: String = "", // Hora de la tarea...
    val isCompleted: Boolean = false,
    val progress: Int = 0, // 0 - 100%
    val isReminderActive: Boolean = false,
    val reminderTime: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "task_history",
    foreignKeys = [ForeignKey(entity = TaskOccurrenceEntity::class, parentColumns = ["occurrenceId"], childColumns = ["occurrenceId"], onDelete = CASCADE)]
)
data class TaskHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val historyId: Long = 0,
    val occurrenceId: Long, // Referencia a `task_occurrences`
    val action: String, // "completed", "updated", "deleted"
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = "" // Opcional: detalles de la acción
)



@Entity(tableName = "categories_tasks")
data class CategoryTaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId")
    val categoryId: Long = 0,
    val name: String,
    val bgColor: String,
)

@Entity(
    primaryKeys = ["taskId", "categoryId"],
    tableName = "task_category_cross_ref"
)
data class TaskCategoryCrossRef(
    @ColumnInfo(name = "taskId")
    val taskId: Long,
    @ColumnInfo(name = "categoryId")
    val categoryId: Long
)

data class TaskWithCategories(
    @Embedded
    val task: TaskEntity,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "categoryId",
        associateBy = androidx.room.Junction(TaskCategoryCrossRef::class)
    )
    val categories: List<CategoryTaskEntity>
)

data class CategoryTaskWithTasks(
    @Embedded
    val category: CategoryTaskEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "taskId",
        associateBy = androidx.room.Junction(TaskCategoryCrossRef::class)
    )
    val tasks: List<TaskEntity>
)


// ------------------- GOALS -----------------------------------

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goalId")
    val goalId: Long = 0,
    val title: String,
    val description: String = "",
    val priority: Int = 0,
    val progress: Int = 0,
    val dueDate: Long,
    val isCompleted: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val color: String,
    val isRecurring: Boolean = false,
    val recurrencePattern: String = "", // "daily", "weekly", "monthly", "yearly", "custom"
    val recurrenceInterval: Int = 0, // en caso de que recurrence sea custom
    val recurrenceEndDate: Long = 0
)


// ---------------------- FINANCE -------------------------

@Entity(tableName = "finance")
data class FinanceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "financeId")
    val financeId: Long = 0,
    val title: String,
    val description: String = "",
    val type: String, // expense or income
    val amount: Double,
    val date: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "categories_finance")
data class CategoryFinanceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId")
    val categoryId: Long = 0,
    val name: String,
    val bgColor: String,
)

@Entity(
    primaryKeys = ["financeId", "categoryId"],
    tableName = "finance_category_cross_ref"
)
data class FinanceCategoryCrossRef(
    @ColumnInfo(name = "financeId")
    val financeId: Long,
    @ColumnInfo(name = "categoryId")
    val categoryId: Long
)

data class FinanceWithCategories(
    @Embedded
    val finance: FinanceEntity,
    @Relation(
        parentColumn = "financeId",
        entityColumn = "categoryId",
        associateBy = androidx.room.Junction(FinanceCategoryCrossRef::class)
    )
    val categories: List<CategoryFinanceEntity>
)

data class CategoryFinanceWithFinances(
    @Embedded
    val category: CategoryFinanceEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "financeId",
        associateBy = androidx.room.Junction(FinanceCategoryCrossRef::class)
    )
    val finances: List<FinanceEntity>
)