package com.atakanmadanoglu.notesapplication.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atakanmadanoglu.notesapplication.data.repository.NotesRepository
import com.atakanmadanoglu.notesapplication.domain.GetNotesByCreatedAtUseCase
import com.atakanmadanoglu.notesapplication.domain.mapper.NoteUIMapper
import com.atakanmadanoglu.notesapplication.domain.model.NoteDomain
import com.atakanmadanoglu.notesapplication.domain.model.UseCaseVariousNotesFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetNotesByCreatedAtUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getNotesByCreatedAtUseCase: GetNotesByCreatedAtUseCase
    private val noteUIMapper = mockk<NoteUIMapper>()
    private val notesRepository = mockk<NotesRepository>()

    @Before
    fun setup() {
        getNotesByCreatedAtUseCase = GetNotesByCreatedAtUseCase(
            notesRepository, noteUIMapper
        )
    }

    @Test
    fun `call invoke() and get a flow of list of noteUIs`() = runTest {
        // Given
        val note = UseCaseVariousNotesFactory.getMockNoteDomain()
        val noteUI = UseCaseVariousNotesFactory.getMockNoteUI()
        val listOfNotes = listOf(note)
        val listOfNoteUIs = listOf(noteUI)
        every { notesRepository.getNotesByCreatedAt() } returns flow {
            emit(listOfNotes)
        }
        every { noteUIMapper.mapToNoteUI(note) } returns noteUI
        // When
        val result = getNotesByCreatedAtUseCase.invoke().first()
        // Then
        assertThat(result).isEqualTo(listOfNoteUIs)
        assertThat(result[0]).isEqualTo(noteUI)
        assertThat(result[0].id).isEqualTo(note.id)
        assertThat(result[0].title).isEqualTo(note.title)
        assertThat(result[0].description).isEqualTo(note.description)
        assertThat(result[0].createdAt).isEqualTo(note.createdAt)
        verify(exactly = 1) { notesRepository.getNotesByCreatedAt() }
        verify(exactly = 1) { noteUIMapper.mapToNoteUI(note) }
    }

    @Test
    fun `call invoke() and get a flow of empty list of noteUIs`() = runTest {
        // Given
        val note = UseCaseVariousNotesFactory.getMockNoteDomain()
        val noteDomainList = emptyList<NoteDomain>()
        every { notesRepository.getNotesByCreatedAt() } returns flow {
            emit(noteDomainList)
        }
        // When
        val result = getNotesByCreatedAtUseCase.invoke().first()
        // Then
        assertThat(result).isEmpty()
        verify(exactly = 1) { notesRepository.getNotesByCreatedAt() }
        verify(exactly = 0) { noteUIMapper.mapToNoteUI(note) }
    }

}