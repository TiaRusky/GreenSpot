package com.example.greenspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.greenspot.ui.theme.GreenspotTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.navgraph.SetupNavGraph
import com.example.greenspot.presentation.cleaner.sign.CleanerProfileScreenViewModel
import com.example.greenspot.presentation.sign_in.GoogleAuthUIClient
import com.example.greenspot.presentation.spotter.SpotterReportsViewModel


class MainActivity : ComponentActivity() {

    //Define the google client object to handle spotter's account login/logout
    private val googleAuthClient by lazy {
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = com.google.android.gms.auth.api.identity.Identity.getSignInClient(
                applicationContext
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GreenspotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background //to set the color based the background color(change in theme.kt)
                ) {

                    val navController: NavHostController = rememberNavController()
                    val applicationContext = applicationContext
                    val lifecycleScope = lifecycleScope

                    //val cleanerViewModel: CleanerProfileScreenViewModel by viewModels()     //used to collect the login data of a cleaner


                    SetupNavGraph(
                        navController = navController,
                        googleAuthClient = googleAuthClient,
                        lifecycleScope = lifecycleScope,
                        applicationContext = applicationContext,
                        //cleanerViewModel = cleanerViewModel
                    )

                }
            }
        }
    }
}


