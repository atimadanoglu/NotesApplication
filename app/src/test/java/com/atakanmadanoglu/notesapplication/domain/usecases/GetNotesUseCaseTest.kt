package com.atakanmadanoglu.notesapplication.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atakanmadanoglu.notesapplication.data.model.Note
import com.atakanmadanoglu.notesapplication.data.repository.NotesRepository
import com.atakanmadanoglu.notesapplication.domain.GetNotesUseCase
import com.atakanmadanoglu.notesapplication.domain.mapper.NoteUIAndRequestMapper
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
class GetNotesUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getNotesUseCase: GetNotesUseCase
    private val noteUIAndRequestMapper = mockk<NoteUIAndRequestMapper>()
    private val notesRepository = mockk<NotesRepository>()

    @Before
    fun setup() {
        getNotesUseCase = GetNotesUseCase(
            notesRepository, noteUIAndRequestMapper
        )
    }

    @Test
    fun `call invoke() and get a flow of list of noteUIs`() = runTest {
        // Given
        val note = UseCaseVariousNotesFactory.getMockNote()
        val noteUI = UseCaseVariousNotesFactory.getMockNoteUI()
        val listOfNotes = listOf(note)
        val listOfNoteUIs = listOf(noteUI)
        every { notesRepository.getNotes() } returns flow {
            emit(listOfNotes)
        }
        every { noteUIAndRequestMapper.mapToNoteUI(note) } returns noteUI
        // When
        val result = getNotesUseCase.invoke().first()
        // Then
        assertThat(result).isEqualTo(listOfNoteUIs)
        assertThat(result[0]).isEqualTo(noteUI)
        assertThat(result[0].id).isEqualTo(note.id)
        assertThat(result[0].title).isEqualTo(note.title)
        assertThat(result[0].description).isEqualTo(note.description)
        assertThat(result[0].createdAt).isEqualTo(note.createdAt)
        verify(exactly = 1) { notesRepository.getNotes() }
        verify(exactly = 1) { noteUIAndRequestMapper.mapToNoteUI(note) }
    }

    @Test
    fun `call invoke() and get a flow of empty list of noteUIs`() = runTest {
        // Given
        val note = UseCaseVariousNotesFactory.getMockNote()
        val listOfNotes = emptyList<Note>()
        every { notesRepository.getNotes() } returns flow {
            emit(listOfNotes)
        }
        // When
        val result = getNotesUseCase.invoke().first()
        // Then
        assertThat(result).isEmpty()
        verify(exactly = 1) { notesRepository.getNotes() }
        verify(exactly = 0) { noteUIAndRequestMapper.mapToNoteUI(note) }
    }

}