package com.atakanmadanoglu.notesapplication.di

import android.content.Context
import androidx.room.Room
import com.atakanmadanoglu.notesapplication.data.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    fun provideNotesDatabase(
        @ApplicationContext context: Context
    ): NotesDatabase = Room.databaseBuilder(
        context, NotesDatabase::class.java, NotesDatabase.DB_NAME
    ).build()

}