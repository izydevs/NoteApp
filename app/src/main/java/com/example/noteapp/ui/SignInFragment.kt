package com.example.noteapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentSignInBinding
import com.example.noteapp.models.DataState
import com.example.noteapp.models.UserSignInDetails
import com.example.noteapp.viewmodels.SharedViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var navController: NavController
    lateinit var fragmentSignInBinding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSignInBinding = FragmentSignInBinding.inflate(inflater, container, false)
        return fragmentSignInBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        fragmentSignInBinding.signInButton.setOnClickListener {
            startActivityForResult(
                sharedViewModel.signIn().signInIntent,
                sharedViewModel.RC_SIGN_IN
            )
        }

        observeSignInDetails()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == sharedViewModel.RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            sharedViewModel.handleSignInResult(task)
        }
    }

    fun observeSignInDetails() {
        sharedViewModel.mutableLivedataSignIndetails.observe(
            viewLifecycleOwner,
            Observer { dataState ->
                when (dataState) {
                    is DataState.Success<UserSignInDetails> -> {
                        // We are storing the email and name into the Activity view model and as it is single Activity app so that
                        // viewmodel will survive untill the Activity is killed so we don't need to store the email and name explictly in local database.
                        sharedViewModel.name = dataState.data.name.toString()
                        sharedViewModel.email = dataState.data.email.toString()
                        navController.navigate(R.id.action_signInFragment_to_homeFragment)
                    }
                    is DataState.Error -> {
                        // put something went wrong message
                        Toast.makeText(
                            requireActivity(),
                            "Sign in failed due to some technical error or network error.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (account != null) {
            // We are storing the email and name into the Activity view model and as it is single Activity app so that
            // viewmodel will survive untill the Activity is killed so we don't need to store the email and name explictly in local database.
            sharedViewModel.name = account.displayName.toString()
            sharedViewModel.email = account.email.toString()
            navController.navigate(R.id.action_signInFragment_to_homeFragment)
        }
    }

}