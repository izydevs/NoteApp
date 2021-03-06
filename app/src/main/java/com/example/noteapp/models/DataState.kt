package com.example.noteapp.models

sealed class DataState <out R>{
    data class Success<out T>(val data: T) : DataState<T>()
    object Error : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    object Idle : DataState<Nothing>()
}