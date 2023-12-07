package com.example.greenspot.presentation.cleaner.sign

//When a user insert a data in the login screen, all of the events in the UIEvent will having the respective value
//if the user enter the email the event of the email will be triggered and will have the value
//and in the LoginCleanerViewModel we will just update the value that the user entered
sealed class LoginCleanerUIEvent { //used to define all the event that user perform in the signup screen
    data class EmailChanged(val email:String) : LoginCleanerUIEvent() //it is used if an user insert some data in the field
    data class PasswordChanged(val password:String) : LoginCleanerUIEvent()

    object LoginButtonClicked : LoginCleanerUIEvent()
}
