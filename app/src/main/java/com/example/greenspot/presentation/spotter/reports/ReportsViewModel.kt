package com.example.greenspot.presentation.spotter.reports


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


//The report data
data class ListItemData(
    val id: String,
    val date: Timestamp,
    val validated: Boolean,
    val location: GeoPoint,
    val imageUrl: String,
    val votes: Int,
    val city: String,
    val region: String,
    val description: String,
    val resolvedByName: String?,
)


class ReportsViewModel : ViewModel() {
    private val _listItems = MutableStateFlow(emptyList<ListItemData>())
    val listItems: StateFlow<List<ListItemData>> = _listItems.asStateFlow()

    private val _lastVisibleItem = MutableStateFlow<DocumentSnapshot?>(null)
    val lastVisibleItem: StateFlow<DocumentSnapshot?> = _lastVisibleItem.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_isLoading.value) {
                _isLoading.value = true
                val newItems = fetchNextPage()
                _listItems.value += newItems
                _isLoading.value = false
            }
        }
    }


    private suspend fun fetchNextPage(): List<ListItemData> {
        val newItems: MutableList<ListItemData> = mutableListOf()
        val lastVisibleItem = _lastVisibleItem.value

        val db = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val fieldName = "spotterId"

        var query = db.collection("reports")
            .orderBy("date")
            .whereEqualTo(fieldName, userId) //Loads the reports made by the current user
            .limit(10)

        // If there's a last visible item, start after it       -> loads only the document not already loaded
        lastVisibleItem?.let { lastVisibleItem ->
            query = query.startAfter(lastVisibleItem)
        }

        val documents = query.get().await()

        val newLastVisibleItem = documents.lastOrNull()


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

        //If I loaded new data, then set the new end report
        if(newLastVisibleItem != null){                      //If i load all the reports, newLastVisibleItem will be null, allowing the load
            _lastVisibleItem.value = newLastVisibleItem      // of the same reports again and again
        }

        return newItems
    }

}