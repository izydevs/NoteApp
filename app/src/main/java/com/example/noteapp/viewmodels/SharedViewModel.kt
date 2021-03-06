package com.example.noteapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.localdatabase.model.UsersNoteEntity
import com.example.noteapp.models.UserSignInDetails
import com.example.noteapp.models.DataState
import com.example.noteapp.repository.BusinessLogicClass
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SharedViewModel
@ViewModelInject
constructor(application: Application, val businessLogicClass: BusinessLogicClass) :
    AndroidViewModel(application) {
    var globalApplication = application
    val RC_SIGN_IN = 0
    var listOfItems = ArrayList<UsersNoteEntity>()
    var mutableLivedataSignIndetails: MutableLiveData<DataState<UserSignInDetails>> =
        MutableLiveData()
    var mutableLivedataSignOutdetails: MutableLiveData<Boolean> = MutableLiveData()
    var deleteNoteLiveData = MutableLiveData<DataState<Int>>()
    var insertNoteLiveData = MutableLiveData<DataState<Long>>()
    var updateNoteLiveData = MutableLiveData<DataState<Int>>()
    var selectedPosition = -1
    var noteTitle: String = ""
    var note: String = ""
    var name: String = ""
    var email: String = ""

    var selectedTite: String = ""
    var selectedNote: String = ""
    fun signIn(): GoogleSignInClient {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(globalApplication, gso)
        return mGoogleSignInClient
    }

    fun signOut() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(globalApplication, gso)
        mGoogleSignInClient.signOut().addOnCompleteListener {
            mutableLivedataSignOutdetails.value = true
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            // updateUI(account)
            val acct = GoogleSignIn.getLastSignedInAccount(globalApplication)
            if (acct != null) {
                val personName = acct.displayName
                val personEmail = acct.email
                mutableLivedataSignIndetails.value = DataState.Success(
                    UserSignInDetails(
                        personName,
                        personEmail
                    )
                )
            }
        } catch (e: ApiException) {
            mutableLivedataSignIndetails.value = DataState.Error
        }
    }


    @ExperimentalCoroutinesApi
    fun addNoteInDatabase(mainStateEvent: MainStateEvent) {
        if (noteTitle.isEmpty() || note.isEmpty()) {
            Toast.makeText(
                globalApplication,
                "All fields are mandatory.",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetInsertEvent -> {
                    businessLogicClass.insertData(
                        UsersNoteEntity(
                            0,
                            name,
                            email,
                            SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.getDefault()).format(
                                Date()
                            ),
                            noteTitle,
                            note
                        )
                    ).onEach { dataState ->
                        insertNoteLiveData.value = dataState
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun updateNote(mainStateEvent: MainStateEvent) {
        if (selectedTite.isEmpty() || selectedNote.isEmpty()) {
            Toast.makeText(
                globalApplication,
                "All fields are mandatory.",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetUpdateEvent -> {
                    val usersNoteEntity = listOfItems.get(selectedPosition)
                    usersNoteEntity.noteTitle = selectedTite
                    usersNoteEntity.note = selectedNote
                    businessLogicClass.updateNote(usersNoteEntity).onEach { dataState ->
                        updateNoteLiveData.value = dataState
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun deleteItemFromCart(mainStateEvent: MainStateEvent, entity: UsersNoteEntity) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetDeleteEvent -> {
                    businessLogicClass.deleteData(entity).onEach { dataState ->
                        deleteNoteLiveData.value = dataState
                    }.launchIn(viewModelScope)
                }
            }
        }
    }


    fun cleantNoteAnNoteTitleEditText() {
        noteTitle = ""
        note = ""
    }

    sealed class MainStateEvent {
        object GetInsertEvent : MainStateEvent()
        object GetDeleteEvent : MainStateEvent()
        object GetUpdateEvent : MainStateEvent()
    }
}