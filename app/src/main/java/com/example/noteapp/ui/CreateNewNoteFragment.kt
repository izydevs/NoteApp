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
import com.example.noteapp.databinding.FragmentCreateNewNoteBinding
import com.example.noteapp.models.DataState
import com.example.noteapp.viewmodels.SharedViewModel

class CreateNewNoteFragment : Fragment() {
    val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var fragmentCreateNewNoteBinding: FragmentCreateNewNoteBinding
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentCreateNewNoteBinding =
            FragmentCreateNewNoteBinding.inflate(inflater, container, false)
        fragmentCreateNewNoteBinding.sharedViewModelData = sharedViewModel
        sharedViewModel.insertNoteLiveData.value = DataState.Idle
        return fragmentCreateNewNoteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        fragmentCreateNewNoteBinding.btCreatenew.setOnClickListener {
            sharedViewModel.addNoteInDatabase(SharedViewModel.MainStateEvent.GetInsertEvent)
        }
        checkIfDataInsertedSuccessfullyOrNot()
    }

    private fun checkIfDataInsertedSuccessfullyOrNot() {
        sharedViewModel.insertNoteLiveData.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Long> -> {
                    fragmentCreateNewNoteBinding.progresbar.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Data added successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigateUp()
                }
                is DataState.Error -> {
                    fragmentCreateNewNoteBinding.progresbar.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Data deletion failed.",
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