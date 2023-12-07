package com.example.greenspot.presentation.cleaner.sign

//When a user insert a data in the signup screen, all of the events in the UIEvent will having the respective value
//if the user enter the company name the event of the company name will be triggered and will have the value
//and in the LoginCleanerViewModel we will just update the value that the user entered
sealed class SignupCleanerUIEvent { //used to define all the event that user perform in the signup screen
    data class CompanyNameChanged(val companyName:String) : SignupCleanerUIEvent() //it is used if an user insert some data in the field
    data class EmailChanged(val email:String) : SignupCleanerUIEvent()
    data class PasswordChanged(val password:String) : SignupCleanerUIEvent()

    object RegisterButtonClicked : SignupCleanerUIEvent()
}
