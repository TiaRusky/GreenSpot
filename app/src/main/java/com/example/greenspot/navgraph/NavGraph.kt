package com.example.greenspot.navgraph

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.greenspot.presentation.cleaner.sign.CleanerProfileScreenViewModel
import com.example.greenspot.presentation.cleaner.sign.LoginCleanerViewModel
import com.example.greenspot.presentation.sign_in.GoogleAuthUIClient

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    googleAuthClient: GoogleAuthUIClient,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context,
    loginCleanerViewModel: LoginCleanerViewModel = viewModel(),
){
    NavHost(
        navController = navController,
        startDestination = WELCOME,
        route = ROOT
    ){

        loginGraph(
            navController = navController,
            googleAuthClient = googleAuthClient,
            lifecycleScope = lifecycleScope,
            applicationContext = applicationContext,
        )

        spotterGraph(
            navController = navController,
            googleAuthClient = googleAuthClient,
            lifecycleScope = lifecycleScope,
            applicationContext = applicationContext
        )

        cleanerGraph(
            navController = navController,
            lifecycleScope = lifecycleScope,
            applicationContext = applicationContext,
            loginCleanerViewModel = loginCleanerViewModel,
        )

    }
}