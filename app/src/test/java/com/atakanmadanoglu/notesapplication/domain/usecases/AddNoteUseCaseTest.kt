package com.atakanmadanoglu.notesapplication.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atakanmadanoglu.notesapplication.data.mapper.NoteEntityMapper
import com.atakanmadanoglu.notesapplication.data.repository.NotesRepository
import com.atakanmadanoglu.notesapplication.domain.AddNoteUseCase
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class AddNoteUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var addNoteUseCase: AddNoteUseCase
    private val notesRepository = mockk<NotesRepository>(relaxed = true)
    private val noteMapper = mockk<NoteEntityMapper>()

    @Before
    fun setup() {
        addNoteUseCase = AddNoteUseCase(notesRepository)
    }

    /* @Test
     fun `call invoke() and verify addNote method is called`() = runTest {
         // Given
         val noteEntity = UseCaseVariousNotesFactory.getMockNoteDomain()
         val noteDomain = noteMapper.mapToNoteDomain()
         coJustRun { notesRepository.addNote(noteDomain) }
         // When
         addNoteUseCase.invoke(
             inputTitle = noteDomain.title,
             inputDescription = noteDomain.description
         )
         // Then
         coVerify(exactly = 1) { notesRepository.addNote(noteDomain) }
     }*/
}