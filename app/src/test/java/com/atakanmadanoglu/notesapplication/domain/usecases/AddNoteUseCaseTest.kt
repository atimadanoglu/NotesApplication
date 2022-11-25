package com.atakanmadanoglu.notesapplication.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atakanmadanoglu.notesapplication.data.repository.NotesRepository
import com.atakanmadanoglu.notesapplication.domain.AddNoteUseCase
import com.atakanmadanoglu.notesapplication.domain.model.UseCaseVariousNotesFactory
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddNoteUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var addNoteUseCase: AddNoteUseCase
    private val notesRepository = mockk<NotesRepository>()

    @Before
    fun setup() {
        addNoteUseCase = AddNoteUseCase(notesRepository)
    }

     @Test
     fun `call invoke() and verify addNote method is called`() = runTest {
         // Given
         val addNoteRequest = UseCaseVariousNotesFactory
             .getMockAddNoteRequest()
         coJustRun { notesRepository.addNote(addNoteRequest) }
         // When
         addNoteUseCase.invoke(addNoteRequest)
         // Then
         coVerify(exactly = 1) { notesRepository.addNote(addNoteRequest) }
     }
}