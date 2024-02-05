package com.example.greenspot.presentation.cleaner.sign



import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CleanerProfileScreenViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(CleanerData())
    val uiState: StateFlow<CleanerData> = _uiState.asStateFlow()

    init{
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        updateCleanerData(userId)
    }

    //Load the data of the cleaner from firestore
    private fun updateCleanerData(id:String) {

        var db = Firebase.firestore
        val ref = db.collection("users").document(id)
        ref.get().addOnSuccessListener { document ->
            _uiState.update { currentState ->
                currentState.copy(
                    userId = id,
                    username = document.getString("companyName"),
                    email = document.getString("email")
                )
            }
        }


    }


}