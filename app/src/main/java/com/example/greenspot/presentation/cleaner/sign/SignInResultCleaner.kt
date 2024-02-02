package com.example.greenspot.presentation.cleaner.sign


data class SignInResult(
    val data: com.example.greenspot.presentation.sign_in.UserData?,   //Logged User data
    val errorMessage: String?
)

data class CleanerData(
    val userId : String = "",
    val username : String? = "",
    val email : String? = "",
    val profilePictureUrl: String? = null
)