package com.example.greenspot.navgraph

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.greenspot.WELCOME
import com.example.greenspot.presentation.sign_in.GoogleAuthUIClient

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    googleAuthClient: GoogleAuthUIClient,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context
){
    NavHost(
        navController = navController,
        startDestination = WELCOME
    ){
        loginGraph(
            navController = navController,
            googleAuthClient = googleAuthClient,
            lifecycleScope = lifecycleScope,
            applicationContext = applicationContext
        )
    }
}