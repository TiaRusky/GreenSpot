package com.example.greenspot.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.greenspot.presentation.sign_in.SignInState

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        Button(onClick = onSignInClick) {
            Text(text = "Sign in with Google")
        }
    }
}