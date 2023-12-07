package com.example.greenspot.presentation.cleaner.sign

data class LoginCleanerUIState (
    var email:String = "",
    var password:String = "",

    //error state variable
    var emailError : Boolean = false,
    var passwordError : Boolean = false
)