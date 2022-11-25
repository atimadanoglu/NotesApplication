package com.atakanmadanoglu.notesapplication.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atakanmadanoglu.notesapplication.data.repository.NotesRepository
import com.atakanmadanoglu.notesapplication.domain.GetNoteByIdUseCase
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
class GetNoteByIdUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getNoteByIdUseCase: GetNoteByIdUseCase
    private val notesRepository = mockk<NotesRepository>()
    private val noteUIMapper = mockk<NoteUIAndRequestMapper>()

    @Before
    fun setup() {
        getNoteByIdUseCase = GetNoteByIdUseCase(
            notesRepository, noteUIMapper
        )
    }

    @Test
    fun `call invoke and get a flow of noteUI model`() = runTest {
        // Given
        val id = 1
        val note = UseCaseVariousNotesFactory.getMockNote()
        val noteUI = UseCaseVariousNotesFactory.getMockNoteUI()
        every { notesRepository.getNoteById(id) } returns flow {
            emit(note)
        }
        every { noteUIMapper.mapToNoteUI(note) } returns noteUI

        // When
        val result = getNoteByIdUseCase.invoke(id).first()
        // Then
        assertThat(result).isEqualTo(noteUI)
        assertThat(result.id).isEqualTo(note.id)
        assertThat(result.title).isEqualTo(note.title)
        assertThat(result.description).isEqualTo(note.description)
        assertThat(result.createdAt).isEqualTo(note.createdAt)
        verify(exactly = 1) { notesRepository.getNoteById(id) }
        verify(exactly = 1) { noteUIMapper.mapToNoteUI(note) }
    }
}