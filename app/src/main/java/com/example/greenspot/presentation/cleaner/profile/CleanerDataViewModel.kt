package com.example.greenspot.presentation.cleaner.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenspot.presentation.cleaner.sign.CleanerData
import com.example.greenspot.presentation.spotter.reports.ListItemData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await


class CleanerDataViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CleanerData())
    val uiState = _uiState.asStateFlow()

    private val _listItems = MutableStateFlow(emptyList<ListItemData>())
    val listItems: StateFlow<List<ListItemData>> = _listItems.asStateFlow()

    init {
        updateCleanerData()
    }

    private fun updateCleanerData() {
        GlobalScope.launch(Dispatchers.IO) {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            var db = Firebase.firestore
            val ref = db.collection("users").document(userId)
            ref.get().addOnSuccessListener { document ->
                val data = document.data
                Log.i("cleaner", "data: $data")
                _uiState.update { currentState ->
                    currentState.copy(
                        userId = userId,
                        username = data?.get("companyName").toString(),
                        email = data?.get("email").toString()
                    )
                }
            }
        }
    }

    fun searchReports(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _listItems.value = emptyList<ListItemData>()
            val newItems = getReportsFromCity(city)
            _listItems.value = newItems
        }
    }

    //This function is used to loads the reports in one city
    private suspend fun getReportsFromCity(city: String): List<ListItemData> {
        val newItems: MutableList<ListItemData> = mutableListOf()
        val db = Firebase.firestore
        val fieldName = "city"
        var query = db.collection("reports")
            .orderBy("votes")
            .whereEqualTo(fieldName, city)              //Loads the reports made by the current user
            .whereEqualTo("resolved",false)   //Not load the resolved reports

        val documents = query.get().await()

        if(documents.isEmpty) {
            /*TODO: Aggiungere notifica a schermo*/
            Log.i("cleaner","No reports found for city $city")
        }

        for (document in documents) {
            val data = document.data

            val report = ListItemData(
                id = "",
                date = data["date"] as Timestamp,
                validated = data["resolved"].toString().toBoolean(),
                location = data["position"] as GeoPoint,
                imageUrl = data["imageURL"].toString(),
                votes = (data["votes"].toString()).toInt(),
                city = data["city"].toString(),
                region = data["region"].toString(),
                description = data["description"].toString()
            )

            newItems += report
        }

        return newItems
    }
}