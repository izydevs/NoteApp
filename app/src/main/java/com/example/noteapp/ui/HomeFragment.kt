package com.example.noteapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localdatabase.model.UsersNoteEntity
import com.example.localdatabase.util.RoomDao
import com.example.noteapp.R
import com.example.noteapp.adapter.NotesListAdapter
import com.example.noteapp.databinding.FragmentHomeBinding
import com.example.noteapp.models.DataState
import com.example.noteapp.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), NotesListAdapter.AdapterClickEventInterface {

    @Inject
    lateinit var roomDao: RoomDao
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var navController: NavController
    var itemListAdapter: NotesListAdapter? = null
    var adapterSet = false
    val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        adapterSet = false
        itemListAdapter = null
        sharedViewModel.deleteNoteLiveData.value = DataState.Idle
        sharedViewModel.mutableLivedataSignIndetails.value = DataState.Idle
        fragmentHomeBinding.btClick.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_createNewNoteFragment)
        }
        fragmentHomeBinding.btLogout.setOnClickListener {
            sharedViewModel.signOut()
            observeSignOutStatus()
        }
        observeData()
        checkSuccessfullDeletionOfItem()
    }

    fun observeData() {
        fragmentHomeBinding.progressbar.visibility = View.VISIBLE
        roomDao.getListOfAllNotesOfSignedInUser(sharedViewModel.email)
            .observe(viewLifecycleOwner, Observer {
                sharedViewModel.cleantNoteAnNoteTitleEditText()
                setAdapter(it)
            })
    }

    fun checkSuccessfullDeletionOfItem() {

        sharedViewModel.deleteNoteLiveData.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Int> -> {
                    fragmentHomeBinding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Data deleted successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is DataState.Error -> {
                    Toast.makeText(
                        requireActivity(),
                        "Something went wrong , data not deleted.",
                        Toast.LENGTH_LONG
                    ).show()
                    fragmentHomeBinding.progressbar.visibility = View.GONE
                }
                is DataState.Loading -> {
                    if (!fragmentHomeBinding.progressbar.isVisible)
                        fragmentHomeBinding.progressbar.visibility = View.VISIBLE
                }
            }
        })
    }

    fun setAdapter(listOfAllItem: List<UsersNoteEntity>) {
        sharedViewModel.listOfItems.clear()
        sharedViewModel.listOfItems.addAll(listOfAllItem)
        fragmentHomeBinding.progressbar.visibility = View.GONE
        if (itemListAdapter == null && !adapterSet) {
            val linearLayoutManager = LinearLayoutManager(requireActivity())
            fragmentHomeBinding.recyclerview.setLayoutManager(linearLayoutManager)
            itemListAdapter = NotesListAdapter(
                requireActivity(),
                sharedViewModel.listOfItems,
                this
            )
            fragmentHomeBinding.recyclerview.adapter = itemListAdapter
        } else {
            fragmentHomeBinding.recyclerview.adapter?.notifyDataSetChanged()
        }
        adapterSet = true
    }

    override fun deleteNote(entity: UsersNoteEntity) {
        fragmentHomeBinding.progressbar.visibility = View.VISIBLE
        sharedViewModel.deleteItemFromCart(SharedViewModel.MainStateEvent.GetDeleteEvent, entity)
    }

    override fun viewDetailorEdit(position: Int) {
        sharedViewModel.selectedTite =
            sharedViewModel.listOfItems.get(position).noteTitle.toString()
        sharedViewModel.selectedNote = sharedViewModel.listOfItems.get(position).note.toString()
        sharedViewModel.selectedPosition = position
        navController.navigate(R.id.action_homeFragment_to_detailsAndEditNoteFragment)
    }

    fun observeSignOutStatus() {
        sharedViewModel.mutableLivedataSignOutdetails.observe(viewLifecycleOwner, Observer {
            if (it) {
                navController.navigate(R.id.action_homeFragment_to_signInFragment)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Something went wrong , couldn't logout.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}