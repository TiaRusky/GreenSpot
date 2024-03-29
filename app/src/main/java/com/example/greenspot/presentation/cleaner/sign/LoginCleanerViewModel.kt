package com.example.greenspot.presentation.cleaner.sign

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.greenspot.navgraph.GreenspotScreen
import com.example.greenspot.presentation.cleaner.sign.LoginCleanerUIEvent
import com.example.greenspot.presentation.cleaner.sign.LoginCleanerUIState
import com.example.greenspot.presentation.cleaner.sign.Validator
import com.google.firebase.auth.FirebaseAuth

class LoginCleanerViewModel() : ViewModel() {

    var loginCleanerUIState = mutableStateOf(LoginCleanerUIState())
    var allValidationsPassed = mutableStateOf(false) //used to check all the validation
    var loginInProgress = mutableStateOf(false) //used to see the circular progress bar

    fun onEvent(event: LoginCleanerUIEvent, applicationContext: Context, navController: NavHostController) {

        when(event) {

            is LoginCleanerUIEvent.EmailChanged -> {
                loginCleanerUIState.value = loginCleanerUIState.value.copy(
                    email = event.email
                )
            }

            is LoginCleanerUIEvent.PasswordChanged -> {
                loginCleanerUIState.value = loginCleanerUIState.value.copy(
                    password = event.password
                )
            }

            is LoginCleanerUIEvent.LoginButtonClicked -> {
                login(applicationContext, navController = navController)
            }
        }
        validateLoginDataWithRules()
    }

    //function to call the validator and validate the strings that user insert in the login screen
    private fun validateLoginDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginCleanerUIState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = loginCleanerUIState.value.password
        )

        //if there is any error in email and password
        loginCleanerUIState.value = loginCleanerUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        //if all the value are valid then allValidationsPassed become true otherwise remains false
        allValidationsPassed.value = emailResult.status && passwordResult.status
    }

    //Login function
    private fun login(applicationContext: Context, navController: NavHostController) {

        loginInProgress.value = true
        val email = loginCleanerUIState.value.email
        val password = loginCleanerUIState.value.password

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { // callback method that if everything is completed we will access to the homepage

                if(it.isSuccessful) {
                    loginInProgress.value = false
                    Toast.makeText(
                        applicationContext,
                        "SignIn Successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(GreenspotScreen.CleanerProfile.name)
                }
            }
            .addOnFailureListener {
                loginInProgress.value = false //if the user insert a wrong value, the progress indicator has to hide
                Toast.makeText(
                    applicationContext,
                    "SignIn Error",
                    Toast.LENGTH_LONG
                ).show()
            }
    }


     fun logout(applicationContext: Context) {
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener =
            FirebaseAuth.AuthStateListener {     //inside this we are getting firebase authentication

                if (it.currentUser == null) {   //this means that signOut is successful
                    Toast.makeText(
                        applicationContext,
                        "Signed out",
                        Toast.LENGTH_LONG
                    ).show()
                    //se va a buon fine devo far andare l'utente alla login page
                }
            }

        firebaseAuth.addAuthStateListener(authStateListener)
    }



}