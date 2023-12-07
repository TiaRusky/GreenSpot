package com.example.greenspot.presentation.cleaner.sign

import android.app.Application
import com.google.firebase.FirebaseApp

class LoginFlowApp : Application() {

    override fun onCreate() { //launching point of our application
        super.onCreate()

        //initialize the firebase app
        FirebaseApp.initializeApp(this)
    }
}