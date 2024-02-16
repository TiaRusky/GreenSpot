package com.example.greenspot.presentation.spotter.addReport


import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AddReportViewModel : ViewModel() {

    //This function save the image on firebase storage and then save the report in firestore
    fun sendReport(imageUri: Uri, location: Location,description:String,city:String,region:String,onSuccess:()->Unit, onFail:()->Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imageRef = storageRef.child("images/" + imageUri.lastPathSegment)   //Gets only the filename of the image

            val uploadTask = imageRef.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->           //Image loaded
                imageRef.downloadUrl.addOnSuccessListener { uri ->      //Gets the url to the image uploaded on firebase storage
                    val imageUrl = uri.toString()
                    Log.i("loadImage","LoadImage:$imageUrl")

                    loadReport(imageUrl,location,description,city,region,onSuccess,onFail)
                }
            }
        }

    }

    //Create the report document in the firestore database
    private fun loadReport(imageUrl:String,location:Location,description: String,city:String,region:String,onSuccess: () -> Unit,onFail: () -> Unit){
        val db = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reportRef = db.collection("reports")

        val data = hashMapOf(           //Prepare the data to save
            "date" to Timestamp.now(),
            "imageURL" to imageUrl,
            "position" to GeoPoint(location.latitude, location.longitude),
            "resolved" to false,
            "spotterId" to userId,
            "votes" to 0,
            "description" to description,
            "city" to city,
            "region" to region
        )

        reportRef.add(data)
            .addOnSuccessListener {
                Log.i("addReport","Report loaded on firestore with success!")
                onSuccess()

                //update votes using python anywhere
                val url = URL("https://ilcameriere.pythonanywhere.com/api/data?id=${it.id}&lat=${location.latitude}&log=${location.longitude}")

                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                }

            }
            .addOnFailureListener { e ->
                Log.e("addReport","Failed firestore loading: $e")
                onFail()
            }
    }

}