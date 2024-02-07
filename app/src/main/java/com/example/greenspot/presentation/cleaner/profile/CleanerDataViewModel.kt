package com.example.greenspot.presentation.cleaner.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.greenspot.presentation.cleaner.sign.CleanerData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.update


class CleanerDataViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(CleanerData())
    val uiState = _uiState.asStateFlow()

    init{
        updateCleanerData()
    }

    private fun updateCleanerData(){
        GlobalScope.launch(Dispatchers.IO) {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            var db = Firebase.firestore
            val ref = db.collection("users").document(userId)
            ref.get().addOnSuccessListener {document->
                val data = document.data
                Log.i("cleaner","data: $data")
                _uiState.update{ currentState->
                    currentState.copy(
                        userId = userId,
                        username = data?.get("companyName").toString(),
                        email = data?.get("email").toString()
                    )
                }
            }
        }


    }
}