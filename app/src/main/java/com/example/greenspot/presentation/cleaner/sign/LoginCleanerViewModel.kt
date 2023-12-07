package com.example.greenspot.presentation.cleaner.sign

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginCleanerViewModel : ViewModel() {

    var loginCleanerUIState = mutableStateOf(LoginCleanerUIState())
    var allValidationsPassed = mutableStateOf(false) //used to check all the validation
    var loginInProgress = mutableStateOf(false) //used to see the circular progress bar

    fun onEvent(event:LoginCleanerUIEvent) {

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
                login()
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
    private fun login() {

        loginInProgress.value = true
        val email = loginCleanerUIState.value.email
        val password = loginCleanerUIState.value.password

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { // callback method that if everything is completed we will access to the homepage

                loginInProgress.value = false
                //se va a buon fine devo far andare l'utente alla login page e mostrare il popup "signUp successfully"
            }
            .addOnFailureListener {
                //ERROR
                loginInProgress.value = false //if the user insert a wrong value, the progress indicator has to hide
            }
    }

}