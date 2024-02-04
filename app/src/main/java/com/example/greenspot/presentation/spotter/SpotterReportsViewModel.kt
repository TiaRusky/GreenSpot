package com.example.greenspot.presentation.spotter

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpotterReportsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SpotterReportsData())
    val uiState =  _uiState.asStateFlow()


    init{
        loadDataFromFirestore()
    }

    private fun loadDataFromFirestore(){
        GlobalScope.launch(Dispatchers.IO) {
            // Simulazione di un'operazione di lettura da Firestore
            val db = Firebase.firestore
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val fieldName = "spotterId"
            var resolvedReports = 0           //Used to count the resolved reports made by the user

            db.collection("reports")
                .whereEqualTo(fieldName,userId)     //Loads the reports made by the current user
                .get()
                .addOnSuccessListener { documents->
                    val count = documents.size()            //retrive the number of reports made by the current spotter
                    for(document in documents){
                        val data = document.data
                        if(data["resolved"] == true) resolvedReports += 1
                    }
                    _uiState.update {currentState->
                        currentState.copy(
                            userId = userId,
                            reportsMade = count,
                            resolvedReports = resolvedReports
                        )
                    }

                }
        }
    }
}