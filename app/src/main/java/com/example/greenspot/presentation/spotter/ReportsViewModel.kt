package com.example.greenspot.presentation.spotter


import android.util.Log


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class ListItemData(
    val id: String,
    val date: String,
    val validated: Boolean,
    val location: String,
    val imageUrl: String,
    val votes: Int,
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
                id = "",
                date = data.get("date").toString(),
                validated = data.get("resolved").toString().toBoolean(),
                location = data.get("position").toString(),
                imageUrl = data.get("imageURL").toString(),
                votes = (data.get("votes").toString()).toInt()
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