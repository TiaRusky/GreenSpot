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
fun SignUpCleanerScreen(
    navController: NavHostController,
    applicationContext: Context,
    signupCleanerViewModel: SignupCleanerViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Surface (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Box(
                    modifier = Modifier
                        .size(100.dp,100.dp),
                ){
                    Image(
                        painter = painterResource(id = R.drawable.ic_trees),
                        contentDescription = "logo"
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                NormalTextComponent(value = "Hello there,")
                HeadingTextComponent(value = "Create an account")
                Spacer(
                    modifier = Modifier
                        .height(40.dp)
                )

                //field for company name
                MyTextFieldComponent(
                    labelValue = "Cleaner company name",
                    painterResource = painterResource(id = R.drawable.profile),
                    onTextChanged = {
                        signupCleanerViewModel.onEvent(SignupCleanerUIEvent.CompanyNameChanged(it), applicationContext = applicationContext, navController = navController)
                    },
                    errorStatus = signupCleanerViewModel.registrationCleanerUIState.value.companyNameError //if there is any in company name, we update it in the composable
                )

                //field for email
                MyTextFieldComponent(
                    labelValue = "Email",
                    painterResource = painterResource(id = R.drawable.message),
                    onTextChanged = {
                        signupCleanerViewModel.onEvent(SignupCleanerUIEvent.EmailChanged(it), applicationContext = applicationContext, navController = navController)
                    },
                    errorStatus = signupCleanerViewModel.registrationCleanerUIState.value.emailError //if there is any in email, we update it in the composable
                )

                //field for the password
                PasswordTextFieldComponent(
                    labelValue = "Password",
                    painterResource = painterResource(id = R.drawable.ic_lock),
                    onTextSelected = {
                        signupCleanerViewModel.onEvent(SignupCleanerUIEvent.PasswordChanged(it), applicationContext = applicationContext, navController = navController)
                    },
                    errorStatus = signupCleanerViewModel.registrationCleanerUIState.value.passwordError //if there is any in password, we update it in the composable
                )

                Spacer(
                    modifier = Modifier
                        .height(100.dp)
                )

                //register button
                ButtonComponent(
                    value = "Register",
                    onButtonClicked = {
                        signupCleanerViewModel.onEvent(SignupCleanerUIEvent.RegisterButtonClicked, applicationContext = applicationContext, navController = navController) //used as a callback after the user inserts the data
                    },
                    isEnabled = signupCleanerViewModel.allValidationsPassed.value //if isEnabled is true then the register button is enabled
                )
                DividerTextComponent()
                ClickableLoginTextComponent(tryToLogin = true, onTextSelected = {
                    navController.navigate(GreenspotScreen.SignInCleaner.name){
                        popUpTo(navController.graph.findStartDestination().id)
                    }
                })
            }

        }

        //If signUpInProgress is true then we can show the circular progress indicator
        if(signupCleanerViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }


}

/*@Preview
@Composable
fun DefaultPreviewOfSignUpCleanerScreen(){
    SignUpCleanerScreen(
        navController = rememberNavController()
    )
}*/
