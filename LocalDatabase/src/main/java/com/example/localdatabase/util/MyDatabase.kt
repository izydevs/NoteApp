package com.example.localdatabase.util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.localdatabase.model.UsersNoteEntity

@Database(entities = [UsersNoteEntity::class] , version = 1,exportSchema = false)
abstract class MyDatabase :RoomDatabase(){
    abstract fun roomDao() : RoomDao
    companion object {
        const val DATABASE_NAME="cart_database"
    }
}