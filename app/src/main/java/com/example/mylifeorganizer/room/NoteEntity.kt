package com.example.mylifeorganizer.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

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
    val folderId: Long? = null
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
    val folderId: Long?
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "folderId") // Renombramos la columna a folderId
    val folderId: Long = 0,
    val name: String,
    @ColumnInfo(name = "parentFolderId") // Renombramos la columna a parentFolderId
    val parentFolderId: Long? = null,
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

// Relación para obtener las notas de una carpeta
data class FolderWithNotes(
    @Embedded
    val folder: FolderEntity,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "folderId",
    )
    val notes: List<NoteEntity>
)