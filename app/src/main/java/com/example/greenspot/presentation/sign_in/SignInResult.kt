package com.example.greenspot.presentation.sign_in


data class SignInResult(
    val data: com.example.greenspot.presentation.sign_in.UserData?,   //Logged User data
    val errorMessage: String?
)

data class UserData(
    val userId : String = "",
    val username : String? = "",
    val profilePictureUrl: String? = "",
    val email: String? = "",         //Tell us if the user is a spotter or a cleaner
)