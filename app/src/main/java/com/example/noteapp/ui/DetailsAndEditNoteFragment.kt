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
import com.example.noteapp.databinding.FragmentDetailsAndEditNoteBinding
import com.example.noteapp.models.DataState
import com.example.noteapp.viewmodels.SharedViewModel

class DetailsAndEditNoteFragment : Fragment() {
    lateinit var fragmentCreateNewNoteBinding: FragmentDetailsAndEditNoteBinding
    val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentCreateNewNoteBinding =
            FragmentDetailsAndEditNoteBinding.inflate(inflater, container, false)
        fragmentCreateNewNoteBinding.sharedviewmodelSelectedTiteAndSelectedNote = sharedViewModel
        sharedViewModel.updateNoteLiveData.value = DataState.Idle
        return fragmentCreateNewNoteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        fragmentCreateNewNoteBinding.btUpdateNote.setOnClickListener {
            sharedViewModel.updateNote(SharedViewModel.MainStateEvent.GetUpdateEvent)
        }
        checkIfDataUpdatedSuccessfulyOrNot()
    }

    private fun checkIfDataUpdatedSuccessfulyOrNot() {
        sharedViewModel.updateNoteLiveData.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Int> -> {
                    fragmentCreateNewNoteBinding.progresbar.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Data updated successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigateUp()
                }
                is DataState.Error -> {
                    fragmentCreateNewNoteBinding.progresbar.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Data updation failed. please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is DataState.Loading -> {
                    if (!fragmentCreateNewNoteBinding.progresbar.isVisible)
                        fragmentCreateNewNoteBinding.progresbar.visibility = View.VISIBLE
                }
            }
        })
    }
}