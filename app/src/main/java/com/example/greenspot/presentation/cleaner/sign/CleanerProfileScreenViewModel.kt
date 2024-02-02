package com.example.greenspot.presentation.cleaner.sign

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.greenspot.presentation.sign_in.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CleanerProfileScreenViewModel : ViewModel(){
    var cleanerData = mutableStateOf(CleanerData())

    fun updateUserData(id:String) {
        var db = Firebase.firestore
        val ref = db.collection("users").document(id)
        ref.get().addOnSuccessListener { document ->
            cleanerData.value = cleanerData.value.copy(
                username = document.getString("companyName"),
                email = document.getString("email")
            )
        }
    }
}