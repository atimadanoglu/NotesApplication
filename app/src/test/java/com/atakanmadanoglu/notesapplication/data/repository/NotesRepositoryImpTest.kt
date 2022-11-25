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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NotesRepositoryImpTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NotesRepository
    private lateinit var note: Note
    private lateinit var noteEntity: NoteEntity
    private lateinit var addNoteRequest: AddNoteRequest
    private val mapper = mockk<NoteEntityMapper>()
    private val notesDao = mockk<NotesDao>()

    @Before
    fun setup() {
        repository = NotesRepositoryImp(
            notesDao, mapper
        )
        addNoteRequest = NoteFactory.getMockAddNoteRequest()
        note = NoteFactory.getMockNote()
        noteEntity = NoteFactory.getMockNoteEntity()
    }

    @Test
    fun `call getNotes() and get a flow of list of Note models`() = runTest {
        // Given
        val noteList = listOf(note)
        val noteEntitiesList = listOf(noteEntity)
        every { notesDao.getNotes() } returns flow {
            emit(noteEntitiesList)
        }
        every { mapper.mapToNote(noteEntity) } returns note

        // When
        val result = repository.getNotes().first()

        // Then
        assertThat(result).isEqualTo(noteList)
        assertThat(result[0].id).isEqualTo(noteEntity.id)
        assertThat(result[0].title).isEqualTo(noteEntity.title)
        assertThat(result[0].description).isEqualTo(noteEntity.description)
        assertThat(result[0].createdAt).isEqualTo(noteEntity.createdAt)
        verify(exactly = 1) { notesDao.getNotes() }
        verify(exactly = 1) { mapper.mapToNote(noteEntity) }
    }

    @Test
    fun `call getNotes() and get a flow of empty list of Note models`() = runTest {
        // Given
        val noteList = emptyList<Note>()
        val noteEntitiesList = emptyList<NoteEntity>()
        every { notesDao.getNotes() } returns flow {
            emit(noteEntitiesList)
        }

        // When
        val result = repository.getNotes().first()

        // Then
        assertThat(result).isEqualTo(noteList)
        verify(exactly = 1) { notesDao.getNotes() }
        verify(exactly = 0) { mapper.mapToNote(noteEntity) }
    }

    @Test
    fun `call getNoteById() and get a note by id`() = runTest {
        // Given
        val id = 1
        every { notesDao.getNoteById(id) } returns flow {
            emit(noteEntity)
        }
        every { mapper.mapToNote(noteEntity) } returns note

        // When
        val result = repository.getNoteById(id).first()

        // Then
        assertThat(result).isEqualTo(note)
        assertThat(result.id).isEqualTo(noteEntity.id)
        assertThat(result.title).isEqualTo(noteEntity.title)
        assertThat(result.description).isEqualTo(noteEntity.description)
        assertThat(result.createdAt).isEqualTo(noteEntity.createdAt)
        verify(exactly = 1) { notesDao.getNoteById(id) }
        verify(exactly = 1) { mapper.mapToNote(noteEntity) }
    }

    @Test
    fun `call addNote() and check the addNote function of notesDao called`() = runTest {
        // Given
        every { mapper.mapToEntity(addNoteRequest) } returns noteEntity
        coJustRun { notesDao.addNote(noteEntity) }

        // When
        repository.addNote(addNoteRequest)

        // Then
        assertThat(addNoteRequest.title).isEqualTo(noteEntity.title)
        assertThat(addNoteRequest.description).isEqualTo(noteEntity.description)
        assertThat(addNoteRequest.createdAt).isEqualTo(noteEntity.createdAt)
        verify(exactly = 1) { mapper.mapToEntity(addNoteRequest) }
        coVerify(exactly = 1) { notesDao.addNote(noteEntity) }
    }
}