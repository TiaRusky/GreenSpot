package com.example.greenspot.presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.greenspot.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

//Class to handle FireBase Google Login
class GoogleAuthUIClient(
    private val context : Context,
    private val oneTapClient : SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn():IntentSender?{
        val result = try{
            oneTapClient.beginSignIn(
                buildSingInRequest()
            ).await()   //Wait untill the routine is completed
        }catch (e: Exception){
            e.printStackTrace()
            if ( e is CancellationException) throw e
            null
        }

        return result?.pendingIntent?.intentSender   //The intent contains the infos about the signed google account
    }


    suspend fun signInWithIntent(intent: Intent):SignInResult{
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken,null)

        return try{
            val user = auth.signInWithCredential(googleCredentials).await().user

            createUserInFirebase(user?.uid,user?.email)

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null

            )
        }catch (e: Exception){
            e.printStackTrace()
            if ( e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    //Insert user in firebase database
    private fun createUserInFirebase(uid:String? ,email: String?) {
        var duid = uid ?: "0"
        var db = Firebase.firestore
        //The data to save in firestore
        val userMap = hashMapOf(
            "uid" to uid,
            "email" to email,
            "type" to "spotter"
        )

        //Verify if the user is already there
        val userRef = db.collection("users").document(duid)
        val usersCollection = db.collection("users")

        usersCollection.whereEqualTo("uid", duid)       //check if the user is already stored
            .get()
            .addOnSuccessListener { querySnapshot ->
                val itExists = !querySnapshot.isEmpty
                if(!itExists){  //If not present add it
                    userRef.set(userMap)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Salvataggio utente andanto a buon fine")
                        }
                        .addOnFailureListener { e ->
                            Log.d("Firestore", "Errore nel salvataggio dell'utente: $e")
                        }
                }
            }
            .addOnFailureListener{e->
                Log.d("Firestore", "Errore nella ricerca dell'utente: $e")
            }


        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                // Controlla se il documento esiste
                val isUserLoaded = documentSnapshot.exists()
                //If the user is not loaded add it to Firestore
                if(!isUserLoaded){
                    userRef.set(userMap)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Salvataggio utente andanto a buon fine")
                        }
                        .addOnFailureListener { e ->
                            Log.d("Firestore", "Errore nel salvataggio dell'utente: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.d("Firestore", "Errore nella query dell'utente: $e")
            }
    }

    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser():UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString(),
            email = email
        )
    }

    private fun buildSingInRequest():BeginSignInRequest{
        return BeginSignInRequest.Builder()     //Return the request to login with a google account
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)   //All google accounts can access
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)  //Auto select the account (if only one avaible)
            .build()
    }
}