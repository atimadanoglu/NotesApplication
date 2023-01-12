package com.atakanmadanoglu.notesapplication.di

import com.atakanmadanoglu.notesapplication.data.NotesDatabase
import com.atakanmadanoglu.notesapplication.data.local.NotesDao
import com.atakanmadanoglu.notesapplication.data.repository.NotesRepository
import com.atakanmadanoglu.notesapplication.data.repository.NotesRepositoryImp
import com.atakanmadanoglu.notesapplication.domain.mapper.NoteUIMapper
import com.atakanmadanoglu.notesapplication.domain.usecases.EditNoteUseCase
import com.atakanmadanoglu.notesapplication.domain.usecases.GetNoteByIdUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotesModule {
    companion object {
        @Provides
        fun provideNotesDao(
            database: NotesDatabase
        ): NotesDao = database.getNotesDao()

        @Provides
        fun provideEditNotesUseCase(
            notesRepository: NotesRepository
        ) = EditNoteUseCase(notesRepository)

        @Provides
        fun provideGetNoteByIdUseCase(
            notesRepository: NotesRepository,
            noteUIMapper: NoteUIMapper
        ) = GetNoteByIdUseCase(notesRepository, noteUIMapper)
    }

    @Binds
    abstract fun provideNotesRepository(
        notesRepositoryImp: NotesRepositoryImp
    ): NotesRepository
}