package com.example.greenspot.presentation.cleaner.sign

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.R
import com.example.greenspot.navgraph.GreenspotScreen

@Composable
fun LoginCleanerScreen(
    loginCleanerViewModel: LoginCleanerViewModel = viewModel(),
    applicationContext: Context,
    navController: NavHostController
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp, 100.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_trees),
                    contentDescription = "logo"
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            NormalTextComponent(value = "Login")
            HeadingTextComponent(value = "Welcome back")
            Spacer(
                modifier = Modifier
                    .height(40.dp)
            )

            //field for email
            MyTextFieldComponent(
                labelValue = "Email",
                painterResource = painterResource(id = R.drawable.message),
                onTextChanged = {
                    loginCleanerViewModel.onEvent(
                        LoginCleanerUIEvent.EmailChanged(it),
                        applicationContext = applicationContext
                    )
                },
                errorStatus = loginCleanerViewModel.loginCleanerUIState.value.emailError
            )

            //field for password
            PasswordTextFieldComponent(
                labelValue = "Password",
                painterResource = painterResource(id = R.drawable.ic_lock),
                onTextSelected = {
                    loginCleanerViewModel.onEvent(
                        LoginCleanerUIEvent.PasswordChanged(it),
                        applicationContext = applicationContext
                    )
                },
                errorStatus = loginCleanerViewModel.loginCleanerUIState.value.passwordError
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            UnderLinedTextComponent(value = "Forgot your password")

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            //login button
            ButtonComponent(
                value = "Login",
                onButtonClicked = {
                    loginCleanerViewModel.onEvent(
                        LoginCleanerUIEvent.LoginButtonClicked,
                        applicationContext = applicationContext
                    ) //used as a callback after the user inserts the data
                },
                isEnabled = loginCleanerViewModel.allValidationsPassed.value //if isEnabled is true then the register button is enabled
            )
            DividerTextComponent()

            //Register (as cleaner)  link
            ClickableLoginTextComponent(tryToLogin = false, onTextSelected = {
                navController.navigate(GreenspotScreen.SignUpCleaner.name) {
                    //Remove the login screen to move to the registration screen
                    popUpTo(navController.graph.findStartDestination().id)
                }
            })
        }
    }

    //If signUpInProgress is true then we can show the circular progress indicator
    if (loginCleanerViewModel.loginInProgress.value) {
        CircularProgressIndicator()
    }

}

/*@Preview
@Composable
fun DefaultPreviewOfLoginCleanerScreen(){
    LoginCleanerScreen(
        navController = rememberNavController()
    )
}*/