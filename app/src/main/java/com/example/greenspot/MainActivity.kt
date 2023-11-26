package com.example.greenspot

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greenspot.ui.theme.GreenspotTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.presentation.sign_in.GoogleAuthUIClient
import com.example.greenspot.presentation.sign_in.SignInViewModel
import com.example.greenspot.ui.SignInScreen
import com.example.greenspot.presentation.spotter.SpotterProfileScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    //Define the google client object to handle spotter's account login/logout
    private val googleAuthClient by lazy{
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = com.google.android.gms.auth.api.identity.Identity.getSignInClient(applicationContext)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreenspotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController: NavHostController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination =GreenspotScreen.SignIn.name
                    ){
                        composable(route = GreenspotScreen.SignIn.name){
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsState()

                            //Check if the user is already logged in
                            LaunchedEffect(key1 = Unit){
                                if(googleAuthClient.getSignedInUser() != null){
                                    navController.navigate(GreenspotScreen.SpotterProfile.name)
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = {result->
                                    if(result.resultCode == RESULT_OK){
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignResult(signInResult)        //Update state
                                        }
                                    }
                                }
                            )
                            
                            LaunchedEffect(key1 = state.isSignInSuccessful){
                                if(state.isSignInSuccessful){
                                    Toast.makeText(
                                        applicationContext,
                                        "SignIn successfully",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate(GreenspotScreen.SpotterProfile.name)
                                    viewModel.resetState()      //Reset the state about the user when he logout
                                }
                            }
                            
                            SignInScreen(
                                state = state,
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

                        composable(route = GreenspotScreen.SpotterProfile.name){
                            SpotterProfileScreen(
                                userData = googleAuthClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch{
                                        googleAuthClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


enum class GreenspotScreen(){
    SignIn,
    SpotterProfile
}
