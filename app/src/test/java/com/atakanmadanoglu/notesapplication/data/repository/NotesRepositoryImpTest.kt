package com.atakanmadanoglu.notesapplication.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atakanmadanoglu.notesapplication.data.local.NotesDao
import com.atakanmadanoglu.notesapplication.data.mapper.NoteEntityMapper
import com.atakanmadanoglu.notesapplication.data.model.Note
import com.atakanmadanoglu.notesapplication.data.model.NoteEntity
import com.atakanmadanoglu.notesapplication.data.model.NoteFactory
import com.atakanmadanoglu.notesapplication.presentation.model.AddNoteRequest
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NotesRepositoryImpTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NotesRepository
    private lateinit var mapper: NoteEntityMapper
    private lateinit var note: Note
    private lateinit var noteEntity: NoteEntity
    private lateinit var addNoteRequest: AddNoteRequest
    private val notesDao = mockk<NotesDao>()

    @Before
    fun setup() {
        mapper = NoteEntityMapper()
        repository = NotesRepositoryImp(
            notesDao, mapper
        )
        addNoteRequest = NoteFactory.getMockAddNoteRequest()
        note = NoteFactory.getMockNote()
        noteEntity = NoteFactory.getMockNoteEntity()
    }

    @Test
    fun `call getNotes() and get a list of notes`() = runTest {
        // Given
        val noteList = listOf(note)
        val flowOfNoteEntitiesList = flowOf(listOf(noteEntity))
        every { notesDao.getNotes() } returns flowOfNoteEntitiesList

        // When
        val result = repository.getNotes().first()

        // Then
        assertThat(result).isEqualTo(noteList)
        verify(exactly = 1) { notesDao.getNotes() }
    }

    @Test
    fun `call getNoteById() and get a note by id`() = runTest {
        // Given
        val id = 1
        val flowOfNoteEntity = flowOf(noteEntity)
        every { notesDao.getNoteById(id) } returns flowOfNoteEntity

        // When
        val result = repository.getNoteById(id).first()

        // Then
        assertThat(result).isEqualTo(note)
        verify(exactly = 1) { notesDao.getNoteById(id) }
    }

    @Test
    fun `call addNote() and check the addNote function of notesDao called`() = runTest {
        // Given
        coJustRun { notesDao.addNote(noteEntity) }

        // When
        repository.addNote(addNoteRequest)

        // Then
        assertThat(note).isEqualTo(mapper.mapToNote(noteEntity))
        coVerify(exactly = 1) { notesDao.addNote(noteEntity) }
    }
}