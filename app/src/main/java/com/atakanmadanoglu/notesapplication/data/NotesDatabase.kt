package com.atakanmadanoglu.notesapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atakanmadanoglu.notesapplication.data.NotesDatabase.Companion.VERSION
import com.atakanmadanoglu.notesapplication.data.local.NotesDao
import com.atakanmadanoglu.notesapplication.data.model.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = VERSION,
    exportSchema = false
)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun getNotesDao(): NotesDao

    companion object {
        const val VERSION = 1
        const val DB_NAME = "NotesDatabase.db"
    }
}