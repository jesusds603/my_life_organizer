package com.example.mylifeorganizer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [NoteEntity::class, CategoryEntity::class, NoteCategoryCrossRef::class, FolderEntity::class],
    version = 11, exportSchema = true
)
abstract class NoteDB : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var INSTANCE: NoteDB? = null

        fun getInstance(context: Context): NoteDB {

            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDB::class.java,
                        "notes.db"
                    )
//                        .addMigrations(MIGRATION_10_11)
                        .fallbackToDestructiveMigration()
                        .setJournalMode(JournalMode.AUTOMATIC)
                        .enableMultiInstanceInvalidation()
                        .build()
                }

                return instance
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Crear la nueva tabla de carpetas
                db.execSQL("""
            CREATE TABLE IF NOT EXISTS `folders` (
                `folderId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `parentFolderId` INTEGER,
                `createdAt` INTEGER NOT NULL
            )
        """)
            }
        }


    }
}
