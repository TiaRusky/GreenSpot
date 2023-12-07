package com.example.greenspot.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.greenspot.navgraph.GreenspotScreen
import com.example.greenspot.presentation.sign_in.SignInState

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    navController : NavHostController,
    modifier : Modifier = Modifier
){
    val context = LocalContext.current

    //Used to show the errors during SignIn operations
    LaunchedEffect(key1 = state.signInError){   //Called each time key1 changes
        state.signInError?.let{error->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()

        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            WelcomeTextComponent(value = "Welcome")

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            
            //button for the Sign in with google
            ButtonComponent(onClick = onSignInClick, value = "Sign in with Google")

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            //button for the login of the cleaner
            ButtonComponent(onClick = {
                //Move to the login cleaner screen
                navController.navigate(GreenspotScreen.SignInCleaner.name) },
                value = "Sign in Cleaners"
            )
        }
    }

}