package com.example.mylifeorganizer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        NoteEntity::class,
        CategoryEntity::class,
        NoteCategoryCrossRef::class,
        FolderEntity::class,
        DailyNoteEntity::class,
        TaskEntity::class,
        TaskOccurrenceEntity::class,
        CategoryTaskEntity::class,
        TaskCategoryCrossRef::class,
        FinanceEntity::class,
        CategoryFinanceEntity::class,
        FinanceCategoryCrossRef::class,
        PaymentMethodEntity::class,
   ],
    version = 30, exportSchema = true
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
                        .build()
                }

                return instance
            }
        }


    }
}
