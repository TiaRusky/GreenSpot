package com.example.greenspot.presentation.cleaner.sign



import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class CleanerProfileScreenViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(CleanerData())
    val uiState: StateFlow<CleanerData> = _uiState.asStateFlow()
    fun updateCleanerData(id:String) {
        var db = Firebase.firestore
        val ref = db.collection("users").document(id)
        ref.get().addOnSuccessListener { document ->
            _uiState.update { currentState ->
                currentState.copy(
                    username = document.getString("companyName"),
                    email = document.getString("email")
                )
            }
        }
    }
}