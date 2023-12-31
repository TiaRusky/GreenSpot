package com.example.greenspot.navgraph

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.greenspot.presentation.sign_in.GoogleAuthUIClient
import com.example.greenspot.presentation.spotter.SpotterProfileScreen
import com.example.greenspot.presentation.spotter.SpotterReports
import kotlinx.coroutines.launch

fun NavGraphBuilder.spotterGraph(
    navController : NavHostController,
    googleAuthClient: GoogleAuthUIClient,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context
){
    navigation(
        startDestination = LoggedSpotterScreens.MyHome.route,
        route = SPOTTER
    ){
        //Home for logged Spotter
        composable(route = LoggedSpotterScreens.MyHome.route){
            SpotterProfileScreen(
                navController = navController,
                userData = googleAuthClient.getSignedInUser(),
                onSignOut = {
                    signOut(
                        navController = navController,
                        googleAuthClient = googleAuthClient,
                        lifecycleScope = lifecycleScope,
                        applicationContext = applicationContext
                    )
                }
            )
        }

        //Personal reports
        composable(route = LoggedSpotterScreens.MyReports.route){
            SpotterReports(
                navController = navController,
                onSignOut = {
                    signOut(
                        navController = navController,
                        googleAuthClient = googleAuthClient,
                        lifecycleScope = lifecycleScope,
                        applicationContext = applicationContext
                    )
                }
            )
        }
    }
}


private fun signOut(
    navController : NavHostController,
    googleAuthClient: GoogleAuthUIClient,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context
){
    lifecycleScope.launch{
        googleAuthClient.signOut()
        Toast.makeText(
            applicationContext,
            "Signed out",
            Toast.LENGTH_LONG
        ).show()
    }.invokeOnCompletion {
        navController.navigate(GreenspotScreen.SignIn.name){//Once logged out return to login screen
            popUpTo(GreenspotScreen.SignIn.name){//Clear the screen stack to lighten the app
                inclusive = true
            }
            //Clear the state of Logged Composable
            restoreState = true
        }
    }
}