package com.atakanmadanoglu.notesapplication.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.atakanmadanoglu.notesapplication.data.NotesDatabase
import com.atakanmadanoglu.notesapplication.data.model.NoteEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@ExperimentalCoroutinesApi
class NotesDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var notesDao: NotesDao
    private lateinit var db: NotesDatabase
    private lateinit var noteEntity1: NoteEntity
    private lateinit var noteEntity2: NoteEntity

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).allowMainThreadQueries().build()
        notesDao = db.getNotesDao()
        noteEntity1 = getNoteEntity(1, 1000)
        noteEntity2 = getNoteEntity(2, 2000)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun call_Get_Notes_By_CreatedAt_And_Get_a_Flow_of_NoteEntity_List_in_Descending_Order() = runTest {
        // Given
        val noteEntityList = listOf(noteEntity2, noteEntity1)
        notesDao.addNote(noteEntity1)
        notesDao.addNote(noteEntity2)

        // When
        val result = notesDao.getNotesByCreatedAt().first()

        // Then
        assertThat(result).isEqualTo(noteEntityList)
    }

    @Test
    fun call_Get_Note_By_Id_And_Get_a_Flow_of_Note_Entity() = runTest {
        // Given
        val id = 1
        val noteEntity = getNoteEntity(id, 1000)
        notesDao.addNote(noteEntity)
        // When
        val result = notesDao.getNoteById(id).first()
        // Then
        assertThat(result).isEqualTo(noteEntity)
    }

    @Test
    fun call_Add_Note_And_Check_Note_is_Added() = runTest {
        // Given
        notesDao.addNote(noteEntity1)
        // When
        val result = notesDao.getNotesByCreatedAt().first()
        // Then
        assertThat(result).contains(noteEntity1)
    }

    @Test
    fun call_Edit_Note_And_Check_Note_is_Edited() = runTest {
        // Given
        val editedNote = getNoteEntity(noteEntity1.id, 3000)
        notesDao.addNote(noteEntity1)
        // When
        notesDao.editNote(editedNote)
        val result = notesDao.getNoteById(noteEntity1.id).first()
        // Then
        assertThat(result.createdAt).isEqualTo(editedNote.createdAt)
        assertThat(result.createdAt).isNotEqualTo(noteEntity1.createdAt)
        assertThat(result).isEqualTo(editedNote)
    }
}

private fun getNoteEntity(
    id: Int,
    createdAt: Long
) = NoteEntity(
    id = id,
    title = "",
    description = "",
    createdAt = createdAt
)