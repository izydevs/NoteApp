package com.example.noteapp.repository

import com.example.localdatabase.model.UsersNoteEntity
import com.example.localdatabase.util.RoomDao
import com.example.noteapp.models.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class BusinessLogicClass
@Inject
constructor(val roomDao: RoomDao) {

    suspend fun insertData(usersNoteEntity: UsersNoteEntity): Flow<DataState<Long>> = flow {
        emit(DataState.Loading)
        try {
            val longValueAfterInsert = roomDao.insertItem(usersNoteEntity)
            if (longValueAfterInsert > 0)
                emit(DataState.Success(longValueAfterInsert))
            else
                emit(DataState.Error)
        } catch (e: Exception) {
            emit(DataState.Error)
        }
    }

    suspend fun updateNote(usersNoteEntity: UsersNoteEntity): Flow<DataState<Int>> = flow {
        emit(DataState.Loading)
        try {
            val longValueAfterInsert = roomDao.updateNote(usersNoteEntity)
            if (longValueAfterInsert > 0)
                emit(DataState.Success(longValueAfterInsert))
            else
                emit(DataState.Error)
        } catch (e: Exception) {
            emit(DataState.Error)
        }
    }

    suspend fun deleteData(entity: UsersNoteEntity): Flow<DataState<Int>> = flow {
        emit(DataState.Loading)
        val intValueAfterDelete = roomDao.deleteItem(entity)
        emit(DataState.Success(intValueAfterDelete))
    }
}