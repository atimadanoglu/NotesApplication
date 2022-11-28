package com.atakanmadanoglu.notesapplication.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atakanmadanoglu.notesapplication.data.local.NotesDao
import com.atakanmadanoglu.notesapplication.data.mapper.NoteEntityMapper
import com.atakanmadanoglu.notesapplication.data.model.NoteEntity
import com.atakanmadanoglu.notesapplication.data.model.NoteFactory
import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
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
    private lateinit var noteDomain: NoteDomain
    private lateinit var noteEntity: NoteEntity
    private val mapper = mockk<NoteEntityMapper>()
    private val notesDao = mockk<NotesDao>()

    @Before
    fun setup() {
        repository = NotesRepositoryImp(
            notesDao, mapper
        )
        noteDomain = NoteFactory.getMockNoteDomain()
        noteEntity = NoteFactory.getMockNoteEntity()
    }

    @Test
    fun `call getNotes() and get a flow of list of Note models`() = runTest {
        // Given
        val noteList = listOf(noteDomain)
        val noteEntitiesList = listOf(noteEntity)
        every { notesDao.getNotesByCreatedAt() } returns flow {
            emit(noteEntitiesList)
        }
        every { mapper.mapToNoteDomain(noteEntity) } returns noteDomain

        // When
        val result = repository.getNotesByCreatedAt().first()

        // Then
        assertThat(result).isEqualTo(noteList)
        assertThat(result[0].id).isEqualTo(noteEntity.id)
        assertThat(result[0].title).isEqualTo(noteEntity.title)
        assertThat(result[0].description).isEqualTo(noteEntity.description)
        assertThat(result[0].createdAt).isEqualTo(noteEntity.createdAt)
        verify(exactly = 1) { notesDao.getNotesByCreatedAt() }
        verify(exactly = 1) { mapper.mapToNoteDomain(noteEntity) }
    }

    @Test
    fun `call getNotes() and get a flow of empty list of Note models`() = runTest {
        // Given
        val noteDomainList = emptyList<NoteDomain>()
        val noteEntitiesList = emptyList<NoteEntity>()
        every { notesDao.getNotesByCreatedAt() } returns flow {
            emit(noteEntitiesList)
        }

        // When
        val result = repository.getNotesByCreatedAt().first()

        // Then
        assertThat(result).isEqualTo(noteDomainList)
        verify(exactly = 1) { notesDao.getNotesByCreatedAt() }
        verify(exactly = 0) { mapper.mapToNoteDomain(noteEntity) }
    }

    @Test
    fun `call getNoteById() and get a note by id`() = runTest {
        // Given
        val id = 1
        every { notesDao.getNoteById(id) } returns flow {
            emit(noteEntity)
        }
        every { mapper.mapToNoteDomain(noteEntity) } returns noteDomain

        // When
        val result = repository.getNoteById(id).first()

        // Then
        assertThat(result).isEqualTo(noteDomain)
        assertThat(result.id).isEqualTo(noteEntity.id)
        assertThat(result.title).isEqualTo(noteEntity.title)
        assertThat(result.description).isEqualTo(noteEntity.description)
        assertThat(result.createdAt).isEqualTo(noteEntity.createdAt)
        verify(exactly = 1) { notesDao.getNoteById(id) }
        verify(exactly = 1) { mapper.mapToNoteDomain(noteEntity) }
    }

    @Test
    fun `call addNote() and check the addNote function of notesDao called`() = runTest {
        // Given
        every { mapper.mapToNoteEntity(noteDomain) } returns noteEntity
        coJustRun { notesDao.addNote(noteEntity) }

        // When
        repository.addNote(noteDomain)

        // Then
        assertThat(noteDomain.title).isEqualTo(noteEntity.title)
        assertThat(noteDomain.description).isEqualTo(noteEntity.description)
        assertThat(noteDomain.createdAt).isEqualTo(noteEntity.createdAt)
        verify(exactly = 1) { mapper.mapToNoteEntity(noteDomain) }
        coVerify(exactly = 1) { notesDao.addNote(noteEntity) }
    }
}