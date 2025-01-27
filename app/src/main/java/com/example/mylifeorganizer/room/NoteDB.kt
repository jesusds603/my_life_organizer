package com.example.mylifeorganizer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        NoteEntity::class,
        CategoryEntity::class,
        NoteCategoryCrossRef::class,
        FolderEntity::class,
        TaskEntity::class,
        CategoryTaskEntity::class,
        TaskCategoryCrossRef::class,
   ],
    version = 20, exportSchema = true
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
                        .fallbackToDestructiveMigration()
                        .setJournalMode(JournalMode.AUTOMATIC)
                        .enableMultiInstanceInvalidation()
//                        .addCallback(object : RoomDatabase.Callback() {
//                            override fun onCreate(db: SupportSQLiteDatabase) {
//                                super.onCreate(db)
//                                // Insertar fila predeterminada en la tabla 'folders'
//                                insertDefaultFolder(context)
//                            }
//                        })
                        .build()
                }

                return instance
            }
        }

//        private fun insertDefaultFolder(context: Context) {
//            // Crear un CoroutineScope para manejar la inserción de datos
//            CoroutineScope(Dispatchers.IO).launch {
//                val database = getInstance(context)
//                val folderDao = database.noteDao()
//
//                // Crear la fila predeterminada con folderId = 0, name = "/", parentFolderId = 0
//                val defaultFolder = FolderEntity(
//                    folderId = 0,  // folderId debe ser 0 como mencionaste
//                    name = "/",
//                    parentFolderId = 0
//                )
//
//                // Insertar la fila en la tabla
//                folderDao.insertFolder(defaultFolder)
//            }
//        }

    }
}
