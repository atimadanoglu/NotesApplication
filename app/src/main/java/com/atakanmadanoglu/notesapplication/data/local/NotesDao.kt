package com.atakanmadanoglu.notesapplication.data.local

import androidx.room.*
import com.atakanmadanoglu.notesapplication.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM note ORDER BY created_at DESC")
    fun getNotesByCreatedAt(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteById(id: Int): Flow<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: NoteEntity)

    @Update
    fun editNote(noteEntity: NoteEntity)
}