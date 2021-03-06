package com.example.localdatabase.util

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.localdatabase.model.UsersNoteEntity

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(entity: UsersNoteEntity) :Long

    @Delete
    suspend fun deleteItem(entity: UsersNoteEntity) :Int

    @Query("select * from  NotesTable Where email =:email")
    fun getListOfAllNotesOfSignedInUser(email:String): LiveData<List<UsersNoteEntity>>

    @Update
    suspend fun updateNote(entity: UsersNoteEntity) :Int
}