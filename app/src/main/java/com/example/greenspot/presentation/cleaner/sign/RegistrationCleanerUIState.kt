package com.example.greenspot.presentation.cleaner.sign

data class RegistrationCleanerUIState (
    var companyName:String = "",
    var email:String = "",
    var password:String = "",

    //error state variable
    var companyNameError : Boolean = false,
    var emailError : Boolean = false,
    var passwordError : Boolean = false
)