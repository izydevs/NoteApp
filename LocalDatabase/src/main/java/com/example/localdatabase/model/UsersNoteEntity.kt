package com.example.localdatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotesTable")
class UsersNoteEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id : Int=0 ,
    @ColumnInfo(name="username")
    var username:String,
    @ColumnInfo(name="email")
    var email:String?,
    @ColumnInfo(name="time")
    var time:String?,
    @ColumnInfo(name="noteTitle")
    var noteTitle:String? ,
    @ColumnInfo(name="note")
    var note:String?
)