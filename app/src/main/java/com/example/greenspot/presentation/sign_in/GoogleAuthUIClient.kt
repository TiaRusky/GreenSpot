package com.example.greenspot.presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.greenspot.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
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
            ).await()   //Wait untile the routine is completed
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
            profilePictureUrl = photoUrl?.toString()
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