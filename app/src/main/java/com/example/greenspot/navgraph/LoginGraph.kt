package com.example.greenspot.navgraph

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.greenspot.presentation.cleaner.sign.LoginCleanerScreen
import com.example.greenspot.presentation.cleaner.sign.SignUpCleanerScreen
import com.example.greenspot.presentation.sign_in.GoogleAuthUIClient
import com.example.greenspot.presentation.sign_in.SignInViewModel
import com.example.greenspot.presentation.spotter.SpotterProfileScreen
import com.example.greenspot.ui.SignInScreen
import kotlinx.coroutines.launch

fun NavGraphBuilder.loginGraph(
    navController: NavHostController,
    googleAuthClient: GoogleAuthUIClient,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context
){
    navigation(
        startDestination = GreenspotScreen.SignIn.name,
        route = WELCOME
    ){
        composable(route = GreenspotScreen.SignIn.name){
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsState()

            //Check if the user is already logged in
            LaunchedEffect(key1 = Unit){
                if(googleAuthClient.getSignedInUser() != null){
                    navController.navigate(GreenspotScreen.SpotterProfile.name){

                        //Once logged in the system, remove the login screen from the stack
                        popUpTo(navController.graph.findStartDestination().id){
                            inclusive = true
                        }
                    }
                }
            }

            //The function to verify that the login gone well
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = {result->
                    if(result.resultCode == ComponentActivity.RESULT_OK){
                        lifecycleScope.launch {
                            val signInResult = googleAuthClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignResult(signInResult)        //Update state
                        }
                    }
                }
            )

            //The coroutine that displays a success message when login gone good
            LaunchedEffect(key1 = state.isSignInSuccessful){
                if(state.isSignInSuccessful){
                    Toast.makeText(
                        applicationContext,
                        "SignIn successfully",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(GreenspotScreen.SpotterProfile.name)
                    viewModel.resetState()                      //Reset the state about the user when he logout
                }
            }

            SignInScreen(
                state = state,
                navController = navController,
                onSignInClick =  {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )

        }

        //The main page when a spotter is logged
        composable(route = GreenspotScreen.SpotterProfile.name){
            SpotterProfileScreen(
                navController = navController,
                userData = googleAuthClient.getSignedInUser(),
                onSignOut = {
                    lifecycleScope.launch{
                        googleAuthClient.signOut()
                        Toast.makeText(
                            applicationContext,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                    }.invokeOnCompletion {
                        navController.popBackStack()
                    }
                }
            )
        }

        //Render the login page for cleaner
        composable(route = GreenspotScreen.SignInCleaner.name){
            LoginCleanerScreen(
                navController = navController,
                applicationContext = applicationContext
            )
        }

        //Render the registration page for cleaner
        composable(route = GreenspotScreen.SignUpCleaner.name){
            SignUpCleanerScreen(
                navController = navController,
                applicationContext = applicationContext
            )
        }
    }
}