package com.example.greenspot.presentation.cleaner.sign

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.greenspot.navgraph.GreenspotScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.firestore.firestore

class SignupCleanerViewModel : ViewModel() {

    var registrationCleanerUIState = mutableStateOf(RegistrationCleanerUIState())
    var allValidationsPassed = mutableStateOf(false) //used to check all the validation
    var signUpInProgress = mutableStateOf(false) //used to see the circular progress bar

    fun onEvent(event: SignupCleanerUIEvent, applicationContext: Context, navController: NavHostController) { //used when user perform any event inside the signup screen

        validateDataWithRules() // if a user insert the valid value, immediately update the field

        when(event) {

            //for company name
            is SignupCleanerUIEvent.CompanyNameChanged -> { //when user enter the company name we will update the value in the registrationUIState
                registrationCleanerUIState.value = registrationCleanerUIState.value.copy(
                    companyName = event.companyName
                )
            }

            //for email
            is SignupCleanerUIEvent.EmailChanged -> {
                registrationCleanerUIState.value = registrationCleanerUIState.value.copy(
                    email = event.email
                )
            }

            //for password
            is SignupCleanerUIEvent.PasswordChanged -> {
                registrationCleanerUIState.value = registrationCleanerUIState.value.copy(
                    password = event.password
                )
            }

            //When the button is clicked we add some logic to validate the previous fields
            is SignupCleanerUIEvent.RegisterButtonClicked -> {
                signUp(applicationContext, navController = navController)
            }
        }
    }

    //function used to create user in firebase database
    private fun signUp(applicationContext: Context, navController: NavHostController) {
        createUserInFirebase(companyName = registrationCleanerUIState.value.companyName, email = registrationCleanerUIState.value.email, password = registrationCleanerUIState.value.password, applicationContext = applicationContext, navController = navController)
    }

    //function to call the validator and validate the strings that user insert in the signup screen
    private fun validateDataWithRules() {

        //call the validator and pass the companyName that the user inserted in the company name field
        val companyNameResult = Validator.validateCompanyName(
            companyName = registrationCleanerUIState.value.companyName
        )

        //call the validator and pass the email that the user inserted in the email field
        val emailResult = Validator.validateEmail(
            email = registrationCleanerUIState.value.email
        )

        //call the validator and pass the password that the user inserted in the password field
        val passwordResult = Validator.validatePassword(
            password = registrationCleanerUIState.value.password
        )

        //if there is any error in company name, email and password
        registrationCleanerUIState.value = registrationCleanerUIState.value.copy(
            companyNameError = companyNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        //if all the value are valid then allValidationsPassed become true otherwise remains false
        allValidationsPassed.value = companyNameResult.status && emailResult.status && passwordResult.status
    }

    //Insert user in firebase database
    private fun createUserInFirebase(companyName: String, email: String, password: String, applicationContext: Context, navController: NavHostController) {
        var db = Firebase.firestore
        val userMap = hashMapOf(
            "companyName" to companyName,
            "email" to email,
            "password" to password,
            "type" to "cleaner"
        )

        signUpInProgress.value = true //when click on register, we see the circular progress indicator
        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { // callback method that if everything is completed we will just add the user
                if(it.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid //to obtain userId of the authentication
                    db.collection("users").document(userId).set(userMap) //to store cleaners in firestore with specific userId
                    signUpInProgress.value = false
                    Toast.makeText(
                        applicationContext,
                        "SignUp Successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(GreenspotScreen.CleanerProfile.name)
                }

            }
            .addOnFailureListener { //function for the error
                Toast.makeText(
                    applicationContext,
                    "SignUp Error",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    //Logout function
    private fun logout(applicationContext: Context) { //inserire questa funzione nella homepage del cleaner
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener = AuthStateListener {//inside this we are getting firebase authentication

            if(it.currentUser == null) { //this means that signOut is successful
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