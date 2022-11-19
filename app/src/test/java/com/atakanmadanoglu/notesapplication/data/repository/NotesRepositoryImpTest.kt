package com.atakanmadanoglu.notesapplication.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atakanmadanoglu.notesapplication.data.local.NotesDao
import com.atakanmadanoglu.notesapplication.data.mapper.NoteMapper
import com.atakanmadanoglu.notesapplication.data.model.NoteFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NotesRepositoryImpTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NotesRepository
    private val notesDao = mockk<NotesDao>()
    private lateinit var mapper: NoteMapper

    @Before
    fun setup() {
        mapper = NoteMapper()
        repository = NotesRepositoryImp(
            notesDao, mapper
        )
    }

    @Test
    fun `call getNotes() and get a list of notes`() = runTest {
        // Given
        val noteEntity = NoteFactory.getMockNoteEntity()
        val flowOfNoteEntitiesList = flowOf(listOf(noteEntity))

        every { notesDao.getNotes() } returns flowOfNoteEntitiesList
        val noteList = flowOfNoteEntitiesList.map {
            it.map { entity ->
                mapper.mapToNote(entity)
            }
        }.first()

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
        val noteEntity = NoteFactory.getMockNoteEntity()
        val flowOfNoteEntity = flowOf(noteEntity)

        every { notesDao.getNoteById(id) } returns flowOfNoteEntity
        val note = flowOfNoteEntity.map {
            mapper.mapToNote(it)
        }.first()

        // When
        val result = repository.getNoteById(id).first()
        // Then
        assertThat(result).isEqualTo(note)
        verify(exactly = 1) { notesDao.getNoteById(id) }
    }

    @Test
    fun `call addNote() and check the addNote function of notesDao called`() = runTest {
        // Given
        val note = NoteFactory.getMockNote()
        val noteEntity = mapper.mapToEntity(note)
        coJustRun { notesDao.addNote(noteEntity) }
        // When
        repository.addNote(mapper.mapToNote(noteEntity))
        // Then
        coVerify(exactly = 1) { notesDao.addNote(noteEntity) }
    }
}