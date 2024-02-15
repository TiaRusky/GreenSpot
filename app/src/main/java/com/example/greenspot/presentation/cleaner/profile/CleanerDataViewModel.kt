package com.example.greenspot.presentation.cleaner.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenspot.presentation.cleaner.sign.CleanerData
import com.example.greenspot.presentation.spotter.reports.ListItemData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    fun searchReports(city: String,onNoReports:()->Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _listItems.value = emptyList<ListItemData>()
            val newItems = getReportsFromCity(city,onNoReports)
            _listItems.value = newItems
        }
    }

    //This function is used to loads the reports in one city
    private suspend fun getReportsFromCity(city: String,onNoReports: () -> Unit): List<ListItemData> {
        val newItems: MutableList<ListItemData> = mutableListOf()
        val db = Firebase.firestore
        val fieldName = "city"
        val capitalizedCity = capitalizeWords(city)
        var query = db.collection("reports")
            .orderBy("votes")
            .whereEqualTo(fieldName, capitalizedCity)              //Loads the reports made by the current user
            .whereEqualTo("resolved",false)             //Not load the resolved reports

        val documents = query.get().await()

        if(documents.isEmpty) {
            onNoReports()
            Log.i("cleaner","No reports found for city $city")
        }

        for (document in documents) {
            val data = document.data

            val report = ListItemData(
                id = document.id,
                date = data["date"] as Timestamp,
                validated = data["resolved"].toString().toBoolean(),
                location = data["position"] as GeoPoint,
                imageUrl = data["imageURL"].toString(),
                votes = (data["votes"].toString()).toInt(),
                city = data["city"].toString(),
                region = data["region"].toString(),
                description = data["description"].toString(),
                resolvedByName = data["resolvedByName"]?.toString() ?: null     //If it is not resolved
            )

            newItems += report
        }

        return newItems
    }

    //The cities in the database are stored with first capital letter
    private fun capitalizeWords(input: String): String {
        return input.split(" ").joinToString(" ") { it.capitalize() }
    }


    //The function used to "clear" a spotter's report
    fun resolveReport(reportId: String,onValidated:()->Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            resolve(reportId,onValidated)
        }
    }

    private suspend fun resolve(reportId: String,onValidated:()->Unit){
        val db = Firebase.firestore
        val reportReference = db.collection("reports").document(reportId)       //Access the report

        //Set the new data for the report
        val updates = hashMapOf(
            "resolved" to true,
            "resolvedById" to  uiState.value.userId,
            "resolvedByName" to uiState.value.username,
        ).toMap()

        reportReference
            .update(updates)
            .addOnSuccessListener {
                _listItems.value = removeItemById(listItems.value,reportId)         //Remove the report from the UI
                onValidated()
                Log.i("cleaner","report $reportId updated successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("cleaner","Error updating report: $e")
            }

    }

    private fun removeItemById(list: List<ListItemData>, idToRemove: String): List<ListItemData> {
        return list.filter { it.id != idToRemove }
    }

}



