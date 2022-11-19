package com.atakanmadanoglu.notesapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atakanmadanoglu.notesapplication.model.Note

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: Long
)

fun NoteEntity.asExternalModel() = Note(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt
)