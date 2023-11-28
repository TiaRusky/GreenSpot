package com.example.greenspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.greenspot.ui.theme.GreenspotTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.navgraph.SetupNavGraph
import com.example.greenspot.presentation.sign_in.GoogleAuthUIClient


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
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController: NavHostController = rememberNavController()
                    val applicationContext = applicationContext
                    val lifecycleScope = lifecycleScope


                    SetupNavGraph(
                        navController = navController,
                        googleAuthClient = googleAuthClient,
                        lifecycleScope = lifecycleScope,
                        applicationContext = applicationContext
                    )

                }
            }
        }
    }
}


