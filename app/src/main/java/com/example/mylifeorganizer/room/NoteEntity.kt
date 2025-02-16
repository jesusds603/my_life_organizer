package com.example.mylifeorganizer.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation

// Entidad para los ajustes
@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "settingId")
    val settingId: Long = 0,
    val isThemeDark: Boolean = true,
    val isLangEng: Boolean = true,
    val orderNotes: String = "updatedDescending",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)


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
    val day: String,  // "yyyy/MM/dd"
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
    val dueDate: String, // Fecha unica o inicial de la tarea (YYYY/MM/DD)
    val dueTime: String = "", // Hora de la tarea...

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


// ------------------- HABITS -----------------------------------

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "habitId")
    val habitId: Long = 0,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val color: String,
    val doItAt: String, // "morning", "afternoon", "night", "any" or "hh:mm" ("custom")

    val recurrencePattern: String, // "daily", "weekly", "monthly", "yearly",
    val isWeeklyAnytime: Boolean = false, // Si es weekly puede ser en cualquier día
    val numDaysForWeekly: Int = 1, // Si es anytime en weekly seleccionar el numero de veces entre 1 y 7
    val recurrenceWeekDays: String = "", // Si no es anytime entonces una lista de los indices de los dias de 0 a 6
    val isMonthlyAnytime: Boolean = false, // Si es monthly puede ser cualquier dia
    val numDaysForMonthly: Int = 1, // Si es anytime en monthly seleccionar el numero de veces (entre 1 y 31)
    val recurrenceMonthDays: String = "", // Si no es anytime entonces una lista de los indices de los dias entre 0 y 31
    val isYearlyAnytime: Boolean = false, // Si es yearly puede ser cualquier día
    val numDaysForYearly: Int = 1, // Si es anytime en yearly seleccionar el numero de veces entre 1 y 366
    val recurrenceYearDays: String = "", // Si no es anytime entonces una lista de los dias en formato MM/dd

    val isFinished: Boolean = false, // Para terminarlo definitivamente
    val duration: Int = 0, // En minutos
)

@Entity(tableName = "habits_occurrences")
data class HabitOccurrenceEntity(
    @PrimaryKey(autoGenerate = true)
    val occurrenceId: Long = 0,
    val habitId: Long,
    val isCompleted: Boolean = false,
    val date: String, // "yyyy/MM/dd" o un rango "yyyy/MM/dd-yyyy/MM/dd"
    val time: String = "", // "hh:mm", // "any", "morning", "afternoon", "night"
    val progress: Int = 0, // 0 - 100% según la duración del hábito en minutos
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
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
    val date: String, // "yyyy/MM/dd"
    val paymentId: Long? = null,
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


@Entity(tableName = "payment_methods")
data class PaymentMethodEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "paymentId")
    val paymentId: Long = 0,
    val name: String = "",
    val bgColor: String,
)


data class FinanceWithCategories(
    @Embedded
    val finance: FinanceEntity,
    @Relation(
        parentColumn = "financeId",
        entityColumn = "categoryId",
        associateBy = androidx.room.Junction(FinanceCategoryCrossRef::class)
    )
    val categories: List<CategoryFinanceEntity>,
)
